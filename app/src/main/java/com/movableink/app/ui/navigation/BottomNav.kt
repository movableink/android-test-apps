@file:Suppress("ktlint:standard:import-ordering", "ktlint:standard:function-naming")

package com.movableink.app.ui.navigation

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.os.ConfigurationCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.movableink.app.R
import com.movableink.app.ui.screens.cart.Cart
import com.movableink.app.ui.screens.cart.CartViewModel
import com.movableink.app.ui.screens.home.HomeScreen
import com.movableink.app.ui.screens.home.HomeViewModel
import com.movableink.app.ui.screens.search.Search
import com.movableink.app.ui.screens.search.SearchViewModel
import java.util.Locale

fun NavGraphBuilder.addHomeGraph(
    onGenderSelected: (String, NavBackStackEntry) -> Unit,
    onSearchBarClick: (NavBackStackEntry) -> Unit,
    cartViewModel: CartViewModel,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    navigateToProductDetail: (String) -> Unit,
) {
    composable(HomeSections.HOME.route) { from ->
        HomeScreen(
            onGenderSelected = { gender -> onGenderSelected(gender, from) },
            homeViewModel = homeViewModel,
        )
    }
    composable(HomeSections.SEARCH.route) { from ->
        Search(
            onSearchBarClick = { onSearchBarClick(from) },
            searchViewModel,
            homeViewModel,
            navigateToProductDetail = { product -> navigateToProductDetail(product) },
        )
    }
    composable(HomeSections.CART.route) {
        Cart(cartViewModel)
    }
}

enum class HomeSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String,
) {
    HOME(R.string.home_feed, Icons.Outlined.Home, "home/init"),
    SEARCH(R.string.home_search, Icons.Outlined.Search, "home/search"),
    CART(R.string.home_cart, Icons.Outlined.ShoppingCart, "home/cart"),
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MovableBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    color: Color = MaterialTheme.colors.primaryVariant,
    contentColor: Color = Color.Cyan,
) {
    val routes = remember { tabs.map { it.route } }
    val currentSection = tabs.first { it.route == currentRoute }

    Surface(
        color = color,
        contentColor = contentColor,
    ) {
        val springSpec =
            SpringSpec<Float>(
                // Determined experimentally
                stiffness = 800f,
                dampingRatio = 0.8f,
            )
        BottomNavLayout(
            selectedIndex = currentSection.ordinal,
            itemCount = routes.size,
            indicator = { BottomNavIndicator() },
            animSpec = springSpec,
            modifier = Modifier.navigationBarsPadding(),
        ) {
            val configuration = LocalConfiguration.current
            val currentLocale: Locale =
                ConfigurationCompat.getLocales(configuration).get(0) ?: Locale.getDefault()

            tabs.forEach { section ->
                val selected = section == currentSection
                val tint by animateColorAsState(
                    if (selected) {
                        Color.White
                    } else {
                        Color.LightGray
                    },
                    label = "",
                )

                val text = stringResource(section.title).uppercase(currentLocale)

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = section.icon,
                            tint = tint,
                            contentDescription = text,
                        )
                    },
                    text = {
                        Text(
                            text = text,
                            color = tint,
                            style = MaterialTheme.typography.button,
                            maxLines = 1,
                        )
                    },
                    selected = selected,
                    onSelected = { navigateToRoute(section.route) },
                    animSpec = springSpec,
                    modifier =
                        BottomNavigationItemPadding
                            .clip(BottomNavIndicatorShape),
                )
            }
        }
    }
}

@Composable
private fun BottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val selectionFractions =
        remember(itemCount) {
            List(itemCount) { i ->
                Animatable(if (i == selectedIndex) 1f else 0f)
            }
        }
    selectionFractions.forEachIndexed { index, selectionFraction ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(target, animSpec) {
            selectionFraction.animateTo(target, animSpec)
        }
    }

    // Animate the position of the indicator
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    Layout(
        modifier = modifier.height(BottomNavHeight),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        },
    ) { measurables, constraints ->
        check(itemCount == (measurables.size - 1)) // account for indicator

        // Divide the width into n+1 slots and give the selected item 2 slots
        val unselectedWidth = constraints.maxWidth / (itemCount + 1)
        val selectedWidth = 2 * unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val itemPlaceables =
            measurables
                .filterNot { it == indicatorMeasurable }
                .mapIndexed { index, measurable ->
                    // Animate item's width based upon the selection amount
                    val width = lerp(unselectedWidth, selectedWidth, selectionFractions[index].value)
                    measurable.measure(
                        constraints.copy(
                            minWidth = width,
                            maxWidth = width,
                        ),
                    )
                }
        val indicatorPlaceable =
            indicatorMeasurable.measure(
                constraints.copy(
                    minWidth = selectedWidth,
                    maxWidth = selectedWidth,
                ),
            )

        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0,
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
fun BottomNavigationItem(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier,
) {
    // Animate the icon/text positions within the item based on selection
    val animationProgress by animateFloatAsState(if (selected) 1f else 0f, animSpec)
    BottomNavItemLayout(
        icon = icon,
        text = text,
        animationProgress = animationProgress,
        modifier =
            modifier
                .selectable(selected = selected, onClick = onSelected)
                .wrapContentSize(),
    )
}

@Composable
private fun BottomNavItemLayout(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier =
                    Modifier
                        .layoutId("icon")
                        .padding(horizontal = TextIconSpacing),
                content = icon,
            )
            val scale = lerp(0.6f, 1f, animationProgress)
            Box(
                modifier =
                    Modifier
                        .layoutId("text")
                        .padding(horizontal = TextIconSpacing)
                        .graphicsLayer {
                            alpha = animationProgress
                            scaleX = scale
                            scaleY = scale
                            transformOrigin = BottomNavLabelTransformOrigin
                        },
                content = text,
            )
        },
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measurables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            textPlaceable,
            iconPlaceable,
            constraints.maxWidth,
            constraints.maxHeight,
            animationProgress,
        )
    }
}

private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2

    val textWidth = textPlaceable.width * animationProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animationProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

@Composable
private fun BottomNavIndicator(
    strokeWidth: Dp = 2.dp,
    color: Color = Color.White,
    shape: Shape = BottomNavIndicatorShape,
) {
    Spacer(
        modifier =
            Modifier
                .fillMaxSize()
                .then(BottomNavigationItemPadding)
                .border(strokeWidth, color, shape),
    )
}

private val TextIconSpacing = 2.dp
private val BottomNavHeight = 56.dp
private val BottomNavLabelTransformOrigin = TransformOrigin(0f, 0.5f)
private val BottomNavIndicatorShape = RoundedCornerShape(percent = 50)
private val BottomNavigationItemPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

@Preview
@Composable
private fun BottomNavPreview() {
    MaterialTheme {
        MovableBottomBar(
            tabs = HomeSections.values(),
            currentRoute = "home/init",
            navigateToRoute = { },
        )
    }
}
