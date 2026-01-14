@file:Suppress("ktlint:standard:function-naming")

package com.movableink.app.ui.screens.carousel

import android.util.Log
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.tabs.TabLayout
import com.movableink.inked.ui.carousel.CarouselListener
import com.movableink.inked.ui.template.DVTemplateView

private const val TAG = "CarouselDemoScreen"

@Composable
fun CarouselDemoScreen(upPress: () -> Unit) {
    val templateJson = remember { getSampleTemplateJson() }
    var title by remember { mutableStateOf("Template Demo") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 60.dp))
                    )
                )
                TemplateViewContainer(
                    jsonPayload = templateJson,
                    onSubjectLoaded = { subject -> title = subject },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            CarouselDemoTopBar(title = title, onBackClicked = upPress)
        }
    }
}

@Composable
private fun CarouselDemoTopBar(title: String, modifier: Modifier = Modifier, onBackClicked: () -> Unit) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = Color(0xFF138565),
            contentColor = Color.Black,
            elevation = 0.dp
        ) {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .size(24.dp, 24.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back"
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun TemplateViewContainer(jsonPayload: String, onSubjectLoaded: (String) -> Unit, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL

                val templateView = DVTemplateView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1f
                    )
                }

                val tabLayout = TabLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    tabGravity = TabLayout.GRAVITY_CENTER
                    tabMode = TabLayout.MODE_FIXED
                }

                addView(templateView)
                addView(tabLayout)

                templateView.setCarouselListener(object : CarouselListener {
                    override fun onPageChanged(position: Int) {
                        Log.d(TAG, "Page changed to: $position")
                    }
                })

                templateView.loadFromJson(jsonPayload).onSuccess {
                    templateView.subject?.let { subject ->
                        onSubjectLoaded(subject)
                    }
                }.onFailure { error ->
                    Log.e(TAG, "Failed to load template", error)
                }

                templateView.attachIndicator(tabLayout)
            }
        },
        modifier = modifier
    )
}

private fun getSampleTemplateJson(): String = """
{
  "subject": "The Impeccably-Styled Commando Sweater",
  "preheader": "100% lambswool with shoulder and elbow patches made for the field and town.",
  "static_content_blocks": {
    "html": "<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"640\" class=\"contenttable\"><tr><td align=\"center\" style=\"padding:0; text-align: center;\"><a href=\"https://www.llbean.com\" target=\"_blank\"><img height=\"auto\" src=\"https://www.llbean.com/epromo/f25/260108_EM_QA_Promo_MCReact_SUPC_Hosting/images/260108_MCReactivationSUPC_15.jpg\" style=\"border:0;display:block;outline:none;text-decoration:none;width:100%;height:auto\" width=\"640\" alt=\"Cardmember exclusive! 15% Off one full-price item.\"></a></td></tr></table>"
  },
  "content_blocks": [
    {"html": "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" role=\"presentation\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;max-width:640px;\"><tr><td align=\"center\" style=\"padding: 0px 0 15px 0;\"><a href=\"https://click.llbean.com\" target=\"_blank\"><img src=\"https://www.llbean.com/epromo/s24/240115_Omni_C_Handoff_Images/images/240115_EM_C_CommandSweater_M.gif\" width=\"640\" style=\"display:block;\" alt=\"Commando Sweaters\"></a></td></tr></table><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" role=\"presentation\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;max-width:640px;\"><tr><td valign=\"top\" align=\"center\" style=\"width:100%;\" bgcolor=\"#ffffff\"><table width=\"90%\" border=\"0\" cellpadding=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#ffffff\"><tr><td valign=\"top\" align=\"center\" bgcolor=\"#ffffff\" width=\"100%\" style=\"font-family: Montserrat, Verdana, Arial, sans-serif; font-weight: 600; font-size: 16px; color: #000; text-align: center; letter-spacing: 0.5px; line-height: 1.4; padding-bottom: 5px;\">Commando Sweater</td></tr></table></td></tr><tr><td valign=\"top\" align=\"center\" style=\"width:100%;\" bgcolor=\"#ffffff\"><table width=\"90%\" border=\"0\" cellpadding=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#ffffff\"><tr><td valign=\"top\" align=\"center\" bgcolor=\"#ffffff\" width=\"100%\" style=\"font-family: Montserrat, Verdana, Arial, sans-serif; font-weight: 500; font-size: 42px; color: #000000; text-align: center; letter-spacing: 1px; line-height: 1.2; padding-bottom: 10px;\">Inspired by the Field, at Ease Everywhere</td></tr></table></td></tr><tr><td valign=\"top\" align=\"center\" style=\"width:100%;\" bgcolor=\"#ffffff\"><table width=\"90%\" border=\"0\" cellpadding=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#ffffff\"><tr><td valign=\"top\" align=\"center\" bgcolor=\"#ffffff\" width=\"100%\" style=\"font-family: Montserrat, Verdana, Arial, sans-serif; font-weight: 400; font-size: 18px; color: #000; text-align: center; letter-spacing: 0.5px; line-height: 1.4; padding-bottom: 20px;\">These great-looking, super-comfortable sweaters are inspired by those worn in World War II by British commandos.</td></tr></table></td></tr><tr><td style=\"text-align: center;\" width=\"100%\"><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr><td align=\"center\" bgcolor=\"#000000\" style=\"border-radius: 2px;\"><a href=\"https://click.llbean.com\" style=\"padding: 12px 25px; background: #000000; color: #ffffff; font-family: Montserrat, Verdana, Arial; font-size: 18px; font-weight: 500; text-decoration: none; text-transform: uppercase; display: inline-block;\">Shop Men's</a></td></tr></table></td></tr></table>"},
    {"html": "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" role=\"presentation\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;max-width:640px;\"><tr><td align=\"center\" style=\"padding: 0px 0 15px 0;\"><a href=\"https://click.llbean.com\" target=\"_blank\"><img src=\"https://www.llbean.com/epromo/f22/221010_EM_Omni_V/images/221010_EM_V_DownComforters.jpg\" width=\"640\" style=\"display:block;\" alt=\"Down Comforters.\"></a></td></tr></table><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" width=\"100%\" role=\"presentation\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;max-width:640px;\"><tr><td valign=\"top\" align=\"center\" style=\"width:100%;\" bgcolor=\"#ffffff\"><table width=\"90%\" border=\"0\" cellpadding=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#ffffff\"><tr><td valign=\"top\" align=\"center\" bgcolor=\"#ffffff\" width=\"100%\" style=\"font-family: Montserrat, Verdana, Arial, sans-serif; font-weight: 500; font-size: 42px; color: #000000; text-align: center; letter-spacing: 1px; line-height: 1.2; padding-bottom: 10px;\">Our Dreamiest Comforters</td></tr></table></td></tr><tr><td valign=\"top\" align=\"center\" style=\"width:100%;\" bgcolor=\"#ffffff\"><table width=\"90%\" border=\"0\" cellpadding=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#ffffff\"><tr><td valign=\"top\" align=\"center\" bgcolor=\"#ffffff\" width=\"100%\" style=\"font-family: Montserrat, Verdana, Arial, sans-serif; font-weight: 400; font-size: 18px; color: #000; text-align: center; letter-spacing: 0.5px; line-height: 1.4; padding-bottom: 20px;\">Lofty, cloud-like comfort, in a variety of cozy styles.</td></tr></table></td></tr><tr><td style=\"text-align: center;\" width=\"100%\"><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr><td align=\"center\" bgcolor=\"#000000\" style=\"border-radius: 2px;\"><a href=\"https://click.llbean.com\" style=\"padding: 12px 25px; background: #000000; color: #ffffff; font-family: Montserrat, Verdana, Arial; font-size: 18px; font-weight: 500; text-decoration: none; text-transform: uppercase; display: inline-block;\">Shop Now</a></td></tr></table></td></tr></table>"}
  ],
  "disclaimer": "One-time-use-only, conditions and exclusions apply. Redeem at Your Local L.L.Bean Store. Present a phone display of this coupon at checkout or print using this link. Sorry, offer not valid at L.L.Bean Outlets."
}
""".trimIndent()
