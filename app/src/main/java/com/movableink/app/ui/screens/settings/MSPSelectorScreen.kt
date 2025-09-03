package com.movableink.app.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.movableink.app.R
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

@Suppress("ktlint:standard:function-naming")
@Composable
fun MSPSelectorScreen(
    currentSelection: String,
    onSelectionChange: (String) -> Unit,
    onBackPressed: () -> Unit,
    viewModel: MSPSelectorViewModel,
) {
    val selectedMsp by viewModel.selectedMsp.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
    ) {
        NavigationBar(
            title = "MSP",
            onBackClicked = onBackPressed,
        )
//        MSPSelector(onSelectionChange, viewModel)
        MSPSelector(viewModel)
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MSPSelector(viewModel: MSPSelectorViewModel) {
    val selectedMsp by viewModel.selectedMsp.collectAsState()
    val context = LocalContext.current

    // Listen for ESP activation trigger
    LaunchedEffect(Unit) {
        viewModel.triggerEspActivation.collect {
            delay(500)
            restartApp(context)
        }
    }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(
            text = "Select your MSP",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        viewModel.availableProviders.forEach { provider ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updateSelectedMsp(provider.name)
                        }.padding(vertical = 8.dp),
            ) {
                RadioButton(
                    selected = selectedMsp == provider.name,
                    onClick = {
                        viewModel.updateSelectedMsp(provider.name)
                    },
                )

                Image(
                    painter = painterResource(id = provider.iconRes),
                    contentDescription = provider.displayName,
                    modifier =
                        Modifier
                            .size(24.dp)
                            .padding(start = 8.dp),
                )

                Text(
                    text = provider.displayName,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)

    if (context is Activity) {
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        context.finish()
    }
    exitProcess(0)
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    title: String?,
    onBackClicked: () -> Unit,
) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = Color.Black,
            elevation = 0.dp,
        ) {
            IconButton(
                onClick = onBackClicked,
                modifier =
                    Modifier
                        .padding(all = 8.dp)
                        .size(24.dp, 24.dp)
                        .align(Alignment.CenterVertically),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.app_name),
                )
            }
            Text(
                text = "$title",
                style = MaterialTheme.typography.h4,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .align(Alignment.CenterVertically),
            )
        }
    }
}
