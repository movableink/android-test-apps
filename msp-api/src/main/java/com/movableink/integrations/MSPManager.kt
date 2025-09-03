package com.movableink.integrations

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.selectedMspTypeStore: DataStore<Preferences> by preferencesDataStore(name = "msp_settings")
val SELECTED_MSP_TYPE_KEY = stringPreferencesKey("selected_msp_type")

object MSPManager {
    private const val TAG = "MSPManager" // Use a consistent tag for logs

    private lateinit var applicationContext: Context
    private var currentProvider: MSPInitializer = DefaultMSPInitializer() // Initialized with a safe default
    private val mspManagerScope = CoroutineScope(Dispatchers.IO + SupervisorJob()) // Long-lived scope for background tasks

    lateinit var selectedMspTypeFlow: Flow<MSPType>

    /**
     * Initializes the MSPManager and the currently selected MSP.
     * Called once from Application.onCreate().
     */
    fun initialize(app: Application) {
        Log.d(TAG, "initialize() called. Is already initialized: ${::applicationContext.isInitialized}")
        if (::applicationContext.isInitialized) {
            Log.w(TAG, "MSPManager already initialized. Skipping re-initialization.")
            return
        }
        applicationContext = app.applicationContext // Use applicationContext to avoid leaks

        // Initialize the Flow first, so it's ready for observation
        selectedMspTypeFlow =
            applicationContext.selectedMspTypeStore.data
                .onEach { prefs -> Log.d(TAG, "DataStore RAW EMISSION: ${prefs[SELECTED_MSP_TYPE_KEY]}") }
                // Debug: See raw DataStore changes
                .map { prefs ->
                    val typeString = prefs[SELECTED_MSP_TYPE_KEY] ?: MSPType.NONE.name
                    val mspType =
                        try {
                            MSPType.valueOf(typeString)
                        } catch (e: IllegalArgumentException) {
                            Log.e(TAG, "Invalid MSPType string in DataStore: '$typeString'. Defaulting to NONE.", e)
                            MSPType.NONE // Default to NONE if stored string is invalid or unexpected
                        }
                    Log.d(TAG, "DataStore MAP: Read '$typeString', mapped to $mspType") // Debug: See mapped value
                    mspType
                }.distinctUntilChanged() // Only emit if the MSPType actually changes (e.g., BRAZE -> MOENGAGE)
                .onEach { distinctType -> Log.d(TAG, "Flow AFTER distinctUntilChanged: Emitting $distinctType") }
                // Debug: See what distinctUntilChanged emits
                .shareIn(
                    mspManagerScope, // Share this flow within the long-lived mspManagerScope
                    SharingStarted.Eagerly, // Start collecting immediately when initialized
                    replay = 1, // Replay the last emitted value to new subscribers (like the initial runBlocking)
                )

        Log.d(TAG, "selectedMspTypeFlow initialized. Starting initial runBlocking collect.")

        // Use runBlocking to synchronously load the initial MSP provider on app startup.
        // This ensures the correct provider is set up before any activities start interacting with MSPManager.
        runBlocking {
            val initialMspType = selectedMspTypeFlow.first() // Get the current (first) value from the flow
            Log.d(TAG, "runBlocking: Initial MSP type from DataStore: $initialMspType")
            setAndInitializeProvider(initialMspType) // This will set up the first provider
            Log.d(TAG, "runBlocking: Initial MSP provider set.")
        }

        // Now, we also start observing the flow for subsequent changes.
        // This collector will remain active throughout the app's lifecycle to handle hot-swaps.
        mspManagerScope.launch {
            Log.d(TAG, "Starting COLLECT for selectedMspTypeFlow in mspManagerScope (for hot-swaps).")
            try {
                selectedMspTypeFlow.collect { newMspType ->
                    Log.d(TAG, "COLLECTOR TRIGGERED: Detected new MSP type from DataStore: $newMspType")
                    // Simplify logic: distinctUntilChanged() ensures we only get a new value if it's genuinely different.
                    // If the flow emits a new value, it means a switch is intended, so we proceed to set it up.
                    Log.d(TAG, "COLLECTOR ACTION: New MSP type ($newMspType). Switching provider.")
                    setAndInitializeProvider(newMspType)
                }
            } catch (e: Exception) {
                Log.e(TAG, "COLLECTOR CRASHED! selectedMspTypeFlow collector failed: ${e.message}", e)
                // If this collector crashes, subsequent updates won't be handled. Monitor this in logs.
            }
            Log.d(TAG, "COLLECTOR FINISHED: selectedMspTypeFlow collect block exited. (Should not happen during app lifecycle)")
        }
    }

    /**
     * Handles switching the current MSP provider: deinitializes the old one and initializes the new.
     */
    private fun setAndInitializeProvider(newMspType: MSPType) {
        Log.d(TAG, "setAndInitializeProvider called for type: $newMspType")

        // CRITICAL FIX: ALWAYS deinitialize the old provider BEFORE initializing the new one.
        // This is crucial for resource management and preventing conflicts.
        Log.d(TAG, "Calling deinitialize on currentProvider (${currentProvider.javaClass.simpleName})...")
        currentProvider.deinitialize()
        Log.d(TAG, "Old MSP provider deinitialized.")

        // Load the new provider based on the selected MSPType
        val newLoadedProvider: MSPInitializer =
            when (newMspType) {
                MSPType.BRAZE -> loadProvider("com.movableink.braze.BrazeProvider", applicationContext)
                MSPType.MOENGAGE -> loadProvider("com.movableInk.moengage.MoEngageProvider", applicationContext)
                MSPType.AIRSHIP -> loadProvider("com.movableInk.airship.AirShipProvider", applicationContext)
                MSPType.APPSFLYER -> loadProvider("com.movableInk.appsFlyer.AppsFlyerProvider", applicationContext)
                MSPType.XTREMEPUSH -> loadProvider("com.movableInk.xtremepush.XtremePushProvider", applicationContext)
                MSPType.NONE -> { // Handle NONE and DEFAULT explicitly by loading the DefaultMSPInitializer
                    Log.d(TAG, "No specific MSP selected (type: $newMspType). Loading DefaultMSPInitializer.")
                    DefaultMSPInitializer()
                }
            }

        Log.d(TAG, "New provider loaded: ${newLoadedProvider.javaClass.simpleName}")

        // Initialize the newly loaded provider
        newLoadedProvider.initialize(applicationContext as Application) // Cast to Application for initialize() method
        Log.d(TAG, "New MSP provider initialized: ${newLoadedProvider.javaClass.simpleName}")

        currentProvider = newLoadedProvider // Update the currentProvider reference to the new active provider
        Log.d(TAG, "currentProvider reference updated to: ${currentProvider.javaClass.simpleName}")
    }

    /**
     * Call this method from your UI (e.g., a settings screen) when the user
     * selects a new MSP type. This updates DataStore, which then triggers the Flow.
     */
    fun selectMspType(mspType: MSPType) {
        Log.d(TAG, "selectMspType called with: ${mspType.name}")
        if (!::applicationContext.isInitialized) {
            Log.e(TAG, "MSPManager not initialized. Call initialize() from Application.onCreate() first.")
            return
        }
        mspManagerScope.launch {
            applicationContext.selectedMspTypeStore.edit { prefs ->
                prefs[SELECTED_MSP_TYPE_KEY] = mspType.name
            }
            Log.d(TAG, "Saved new MSP selection to DataStore: ${mspType.name}")
            // The flow collection in 'initialize' will automatically handle the actual switching
        }
    }

    /**
     * Dynamically loads an MSPInitializer class by its fully qualified name.
     */
    private fun loadProvider(
        className: String,
        context: Context,
    ): MSPInitializer {
        Log.d(TAG, "loadProvider called for className: $className")
        return try {
            val clazz = Class.forName(className)
            val instance = clazz.getDeclaredConstructor().newInstance() as MSPInitializer
            Log.d(TAG, "Provider class loaded successfully: $className")
            instance
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "Class not found for provider $className. Check module dependencies and package names.", e)
            DefaultMSPInitializer() // Return a safe default fallback if class not found
        } catch (e: Exception) {
            Log.e(TAG, "Error loading provider $className: ${e.message}", e)
            DefaultMSPInitializer() // Return a safe default fallback for other errors
        }
    }

    /**
     * Provides the currently active MSPInitializer instance for external use (e.g., tracking events).
     */
    fun get(): MSPInitializer = currentProvider

    // --- Activity Lifecycle Callbacks (Ensure your Application class forwards these) ---
    fun onActivityResumed(activity: Activity) {
        Log.v(TAG, "onActivityResumed for ${activity.javaClass.simpleName}. Calling provider: ${currentProvider.javaClass.simpleName}")
        currentProvider.onActivityResumed(activity)
    }

    fun onActivityCreated(activity: Activity) {
        Log.v(TAG, "onActivityCreated for ${activity.javaClass.simpleName}. Calling provider: ${currentProvider.javaClass.simpleName}")
        currentProvider.onActivityCreated(activity)
    }

    // Helper to get the currently loaded MSP type (for debugging and consistency check)
    private fun getCurrentMspTypeInternal(provider: MSPInitializer): MSPType =
        when (provider.javaClass.simpleName) {
            "BrazeProvider" -> MSPType.BRAZE
            "MoEngageProvider" -> MSPType.MOENGAGE
            "AirShipProvider" -> MSPType.AIRSHIP
            "AppsFlyerProvider" -> MSPType.APPSFLYER
            "XtremePushProvider" -> MSPType.XTREMEPUSH
            "DefaultMSPInitializer" -> MSPType.NONE
            else -> MSPType.NONE // Unknown loaded provider, potentially an error state
        }

    // For external consumption to know the current active type
    fun getCurrentActiveMspType(): MSPType = getCurrentMspTypeInternal(currentProvider)
}

// A simple default/no-op implementation for safety
class DefaultMSPInitializer : MSPInitializer {
    override fun initialize(application: Application) {
        Log.d("DefaultMSP", "Default MSP Initialized.")
    }

    override fun onActivityResumed(activity: Activity) { /* no-op */ }

    override fun onActivityCreated(activity: Activity) { /* no-op */ }

    override fun deinitialize() {
        Log.d("DefaultMSP", "Default MSP De-Initialized.")
    }
//    override fun trackEvent(eventName: String, properties: Map<String, Any>) { Log.d("DefaultMSP", "Tracking default event: $eventName") }
}
