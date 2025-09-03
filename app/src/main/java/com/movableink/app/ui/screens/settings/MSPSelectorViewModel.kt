package com.movableink.app.ui.screens.settings

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.movableink.integrations.MSPManager
import com.movableink.integrations.MSPType
import com.movableink.integrations.SELECTED_MSP_TYPE_KEY
import com.movableink.integrations.selectedMspTypeStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:no-consecutive-comments")
private val TAG = "MSPSelectorVM"

@Suppress("ktlint:standard:no-consecutive-comments")
class MSPSelectorViewModel(
    context: Context,
) : ViewModel() {
    private val _triggerEspActivation = MutableSharedFlow<Unit>(replay = 0)
    val triggerEspActivation: SharedFlow<Unit> = _triggerEspActivation.asSharedFlow()

    companion object {
        private val SELECTED_MSP_KEY = SELECTED_MSP_TYPE_KEY
    }

    val availableProviders =
        com.movableink.integrations.MSPType // Assuming this is your enum
            .values()
            .filter { it != MSPType.NONE } // Filter out NONE if it's a special internal type

    private val dataStore = context.selectedMspTypeStore // Access DataStore via context extension

    private val _selectedMsp = MutableStateFlow("") // Stores the currently selected MSP type string
    val selectedMsp: StateFlow<String> = _selectedMsp.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d(TAG, "Starting DataStore collection for _selectedMsp in ViewModel.")
            dataStore.data
                .map { prefs ->
                    val mspTypeString = prefs[SELECTED_MSP_KEY] ?: MSPType.NONE.name
                    Log.d(TAG, "DataStore collect (VM): Read '$mspTypeString' from prefs.")
                    mspTypeString
                }.collect { mspTypeString ->
                    Log.d(TAG, "DataStore collect (VM): New MSP type: $mspTypeString. Updating _selectedMsp StateFlow.")
                    _selectedMsp.value = mspTypeString
                }
        }
    }

    private fun loadSelectedMsp() {
        // Initial load is now covered by the `collect` block in init{}
        // This method can be removed or simplified if desired, but keeping it for clarity
        viewModelScope.launch {
            val initialMsp =
                dataStore.data
                    .map { prefs -> prefs[SELECTED_MSP_KEY] ?: MSPType.NONE.name }
                    .firstOrNull() // Get the first value and then complete
            initialMsp?.let { _selectedMsp.value = it }
        }
    }

    fun updateSelectedMsp(mspTypeString: String) {
        Log.d(TAG, "updateSelectedMsp called with: $mspTypeString")
        viewModelScope.launch {
            // Update DataStore. This will trigger the MSPManager's Flow collection.
            dataStore.edit { prefs ->
                prefs[SELECTED_MSP_KEY] = mspTypeString
            }
            Log.d(TAG, "DataStore updated to: $mspTypeString from ViewModel.")

            // Explicitly call MSPManager to ensure it gets the signal,
            // although the DataStore flow should also trigger it.
            try {
                val mspType = MSPType.valueOf(mspTypeString)
                Log.d(TAG, "Calling MSPManager.selectMspType(${mspType.name})")
                MSPManager.selectMspType(mspType)
                _triggerEspActivation.emit(Unit)
            } catch (e: IllegalArgumentException) {
                android.util.Log.e(TAG, "Invalid MSP type string for enum conversion: $mspTypeString", e)
            }
        }
    }
}

class MSPSelectorViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MSPSelectorViewModel(context.applicationContext) as T
}
