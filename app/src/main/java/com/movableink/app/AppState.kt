package com.movableink.app

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.movableink.app.data.model.SnackbarManager
import com.movableink.app.ui.navigation.HomeSections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val CATEGORIES_ROUTE = "categories"
    const val CATALOG_ROUTE = "catalog"
    const val PRODUCT_DETAIL_ROUTE = "productDetail"
    const val PRODUCT_ID = "productId"
    const val SELECTED_GENDER = "gender"
    const val SELECTED_CATEGORY = "category"
    const val SEARCH_UI_ROUTE = "search_ui"
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) =
    remember(scaffoldState, navController, snackBarManager, resources, coroutineScope) {
        AppState(scaffoldState, navController, snackBarManager, resources, coroutineScope)
    }

@Stable
class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.getText(message.messageId)
                    scaffoldState.snackbarHostState.showSnackbar(text.toString())
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    val bottomBarTabs = HomeSections.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToCategories(
        gender: String,
        from: NavBackStackEntry,
    ) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.CATEGORIES_ROUTE}/$gender")
        }
    } fun navigateToCatalog(category: String, gender: String) {
        navController.navigate("${MainDestinations.CATALOG_ROUTE}/$category/$gender")
    }
    fun navigateToProductDetail(product: String) {
        navController.navigate("${MainDestinations.PRODUCT_DETAIL_ROUTE}/$product")
    }
    fun navigateToSearchUI(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.SEARCH_UI_ROUTE)
        }
    }
    fun navigateToCart() {
        navController.navigate(MainDestinations.HOME_ROUTE)
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
