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
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.tabs.TabLayout
import com.movableink.inked.ui.carousel.CarouselItem
import com.movableink.inked.ui.carousel.CarouselListener
import com.movableink.inked.ui.carousel.MovableCarouselView
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "CarouselDemoScreen"

@Composable
fun CarouselDemoScreen(upPress: () -> Unit) {
    val carouselJson = remember { createSampleCarouselJson() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 60.dp))
                    )
                )
                CarouselViewWithIndicator(
                    jsonPayload = carouselJson,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )
            }
            CarouselDemoTopBar(onBackClicked = upPress)
        }
    }
}

@Composable
private fun CarouselDemoTopBar(modifier: Modifier = Modifier, onBackClicked: () -> Unit) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primaryVariant,
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
                text = "Carousel Demo",
                style = MaterialTheme.typography.h4,
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
private fun CarouselViewWithIndicator(jsonPayload: String, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL

                val carouselView = MovableCarouselView(context).apply {
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

                addView(carouselView)
                addView(tabLayout)

                carouselView.setListener(object : CarouselListener {
                    override fun onItemClicked(item: CarouselItem, position: Int) {
                        Log.d(TAG, "Item clicked: ${item.title} at position $position")
                        Log.d(TAG, "Link URL: ${item.linkUrl}")
                    }

                    override fun onNavigationError(item: CarouselItem, url: String, exception: Exception) {
                        Log.e(TAG, "Navigation error for ${item.title}: $url", exception)
                    }

                    override fun onPageChanged(position: Int) {
                        Log.d(TAG, "Page changed to: $position")
                    }

                    override fun onImageLoadError(item: CarouselItem, position: Int, exception: Exception) {
                        Log.e(TAG, "Image load error for ${item.title} at $position", exception)
                    }
                })

                carouselView.loadFromJson(jsonPayload)
                carouselView.attachIndicator(tabLayout)
                carouselView.autoNavigate = false
            }
        },
        modifier = modifier
    )
}

private fun createSampleCarouselJson(): String {
    val inputJson = """
        [
           {
               "mi_title": "Summer Sale is Here!",
               "mi_body": "Get up to 50% off on all summer essentials. Shop now before it's gone!",
               "mi_image_url": "https://images.example.com/summer-sale-banner.jpg",
               "mi_deep_link": "https://app.example.com/promo/summer-sale",
               "mi_cmp": "camp_summer_2024",
               "mi_ecmp": "ext_summer_promo_001"
           },
           {
               "mi_title": "New Arrivals Just Dropped",
               "mi_body": "Check out the latest styles curated just for you.",
               "mi_image_url": "https://images.example.com/new-arrivals.jpg",
               "mi_deep_link": "https://app.example.com/new-arrivals?category=featured",
               "mi_cmp": "camp_new_arrivals",
               "mi_ecmp": "ext_weekly_drop_015"
           }
        ]
    """.trimIndent()

    val inputArray = JSONArray(inputJson)
    val outputArray = JSONArray()

    for (i in 0 until inputArray.length()) {
        val inputItem = inputArray.getJSONObject(i)
        val outputItem = JSONObject().apply {
            put("title", inputItem.optString("mi_title", ""))
            put("body", inputItem.optString("mi_body", ""))
            put("imageUrl", inputItem.optString("mi_image_url", ""))
            put("linkUrl", inputItem.optString("mi_deep_link", ""))
        }
        outputArray.put(outputItem)
    }

    return outputArray.toString()
}
