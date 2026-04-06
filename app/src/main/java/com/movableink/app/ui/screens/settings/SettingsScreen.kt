@file:Suppress("ktlint:standard:function-naming")

package com.movableink.app.ui.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.app.R
import com.movableink.inked.MIClient
import com.salesforce.marketingcloud.MarketingCloudSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val PREFS_NAME = "settings_prefs"
private const val KEY_MIU = "mi_u"
private const val DEBOUNCE_MS = 500L

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsBottomSheet(
    content: @Composable (() -> Unit) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val scope = rememberCoroutineScope()

    val showSheet: () -> Unit = { scope.launch { sheetState.show() } }
    val hideSheet: () -> Unit = { scope.launch { sheetState.hide() } }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            SettingsScreen(onDismiss = hideSheet)
        },
    ) {
        content(showSheet)
    }
}

@Composable
fun SettingsScreen(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    var fcmToken by remember { mutableStateOf<String?>(null) }
    var contactKey by remember { mutableStateOf<String?>(null) }
    var deviceId by remember { mutableStateOf<String?>(null) }
    var pushToken by remember { mutableStateOf<String?>(null) }

    // Load persisted MIU or generate + persist a UUID on first boot
    var miu by remember {
        val saved = prefs.getString(KEY_MIU, null)
        val initial = if (saved.isNullOrEmpty()) {
            val generated = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_MIU, generated).apply()
            generated
        } else {
            saved
        }
        mutableStateOf(initial)
    }

    LaunchedEffect(Unit) {
        fcmToken = try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }

        MarketingCloudSdk.requestSdk { sdk ->
            contactKey = sdk.registrationManager.contactKey
            deviceId = sdk.registrationManager.deviceId
            pushToken = sdk.pushMessageManager.pushToken
        }
    }

    // Debounced: fires 500ms after miu stops changing
    LaunchedEffect(miu) {
        val currentMiu = miu
        delay(DEBOUNCE_MS)
        prefs.edit().putString(KEY_MIU, currentMiu).apply()
        MIClient.setMIU(currentMiu)
        SFMCSdk.requestSdk { sfmcSdk ->
            sfmcSdk.identity.setProfileId(currentMiu)
        }
    }

    LazyColumn(modifier = Modifier.padding(bottom = 32.dp)) {

        // --- User section ---
        item {
            SettingsSectionHeader(title = stringResource(R.string.settings_user_header))
        }
        item {
            OutlinedTextField(
                value = miu,
                onValueChange = { miu = it },
                label = { Text(stringResource(R.string.settings_miu_placeholder)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Text(
                text = stringResource(R.string.settings_miu_footer),
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }

        // --- FCM section ---
        item {
            SettingsSectionHeader(title = stringResource(R.string.settings_fcm_token_header))
        }
        item {
            SettingsRow(
                label = stringResource(R.string.settings_push_token_label),
                value = fcmToken ?: stringResource(R.string.settings_none),
                copyable = fcmToken != null,
                context = context,
            )
        }

        // --- SFMC Attributes section ---
        item {
            SettingsSectionHeader(title = stringResource(R.string.settings_sfmc_header))
        }
        item {
            SettingsRow(
                label = stringResource(R.string.settings_contact_key_label),
                value = contactKey ?: "-",
                copyable = contactKey != null,
                context = context,
            )
        }
        item {
            SettingsRow(
                label = stringResource(R.string.settings_device_id_label),
                value = deviceId ?: "-",
                copyable = deviceId != null,
                context = context,
            )
        }
        item {
            SettingsRow(
                label = stringResource(R.string.settings_push_token_label),
                value = pushToken ?: "-",
                copyable = pushToken != null,
                context = context,
            )
        }

    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.caption,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 6.dp),
    )
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
    copyable: Boolean = false,
    context: Context? = null,
) {
    val modifier = if (copyable && context != null) {
        Modifier
            .fillMaxWidth()
            .clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(label, value))
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
    Divider(color = Color.LightGray, thickness = 0.5.dp)
}
