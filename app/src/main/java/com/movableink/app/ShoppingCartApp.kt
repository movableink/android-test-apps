package com.movableink.app

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.movableink.app.ui.component.MovableSnackSnackBar
import com.movableink.app.ui.navigation.DeepLinkPattern.baseDestination
import com.movableink.app.ui.navigation.HomeSections
import com.movableink.app.ui.navigation.MovableBottomBar
import com.movableink.app.ui.navigation.addHomeGraph
import com.movableink.app.ui.screens.cart.CartViewModel
import com.movableink.app.ui.screens.home.CatalogScreen
import com.movableink.app.ui.screens.home.CategoryScreen
import com.movableink.app.ui.screens.home.HomeViewModel
import com.movableink.app.ui.screens.home.ProductDetailScreen
import com.movableink.app.ui.screens.search.SearchView
import com.movableink.app.ui.screens.search.SearchViewModel
import com.movableink.app.ui.theme.ShoppingCartTheme

@Composable
fun ShoppingCartApp() {
    ShoppingCartTheme {
        val appState = rememberAppState()
        val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.provideFactory())
        val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.provideFactory())
        val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.provideFactory())
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    MovableBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute!!,
                        navigateToRoute = appState::navigateToBottomBarRoute,
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> MovableSnackSnackBar(snackbarData) },
                )
            },
            scaffoldState = appState.scaffoldState,
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier),

            ) {
                appNavGraph(
                    onGenderSelected = appState::navigateToCategories,
                    upPress = appState::upPress,
                    onCategoryClick = appState::navigateToCatalog,
                    navigateToProductDetail = appState::navigateToProductDetail,
                    onSearchBarClick = appState::navigateToSearchUI,
                    cartViewModel = cartViewModel,
                    navigateToCart = appState::navigateToCart,
                    homeViewModel = homeViewModel,
                    searchViewModel = searchViewModel,
                )
            }
        }
    }
}
private fun NavGraphBuilder.appNavGraph(
    onGenderSelected: (String, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onCategoryClick: (String, String) -> Unit,
    navigateToProductDetail: (String) -> Unit,
    onSearchBarClick: (NavBackStackEntry) -> Unit,
    cartViewModel: CartViewModel,
    navigateToCart: () -> Unit,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.HOME.route,
    ) {
        addHomeGraph(
            onGenderSelected,
            onSearchBarClick,
            cartViewModel = cartViewModel,
            homeViewModel = homeViewModel,
            searchViewModel = searchViewModel,
            navigateToProductDetail = navigateToProductDetail,
        )
    }
    composable(
        route = "${MainDestinations.CATEGORIES_ROUTE}/{${MainDestinations.SELECTED_GENDER}}",
        arguments = listOf(navArgument(MainDestinations.SELECTED_GENDER) { type = NavType.StringType }),
    ) {
        CategoryScreen(
            onCategoryClick = onCategoryClick,
            upPress,
            homeViewModel,
        )
    }
    composable(
        route = "${MainDestinations.CATALOG_ROUTE}/{${MainDestinations.SELECTED_CATEGORY}}/{${MainDestinations.SELECTED_GENDER}}",
        arguments = listOf(
            navArgument(MainDestinations.SELECTED_GENDER) { type = NavType.StringType },
            navArgument(MainDestinations.SELECTED_CATEGORY) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val selectedCategory = arguments.getString(MainDestinations.SELECTED_CATEGORY)
        CatalogScreen(
            category = selectedCategory,
            navigateToProductDetail = navigateToProductDetail,
            upPress,
            cartViewModel,
            onViewCart = navigateToCart,
            homeViewModel,
        )
    }

    composable(
        route = "${MainDestinations.PRODUCT_DETAIL_ROUTE}/{${MainDestinations.PRODUCT_ID}}",
        arguments = listOf(
            navArgument(MainDestinations.PRODUCT_ID) { type = NavType.StringType },
        ),
    ) {
        ProductDetailScreen(
            popBackStack = upPress,
            cartViewModel,
            homeViewModel,
        )
    }
    composable(
        route = MainDestinations.PRODUCT_DETAIL_ROUTE,
        deepLinks = listOf(navDeepLink { uriPattern = "$baseDestination/{productId}" }),
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val selectedCategory = arguments.getString(MainDestinations.SELECTED_CATEGORY)
        val productID = arguments.getString("productId")
        homeViewModel.updateSelectedProduct(productID ?: "")
        ProductDetailScreen(
            popBackStack = upPress,
            cartViewModel,
            homeViewModel,
        )
    }
    composable(
        route = MainDestinations.SEARCH_UI_ROUTE,
    ) {
        SearchView(
            onBackClicked = upPress,
            searchViewModel,
            navigateToProductDetail,
            homeViewModel,
        )
    }
}
