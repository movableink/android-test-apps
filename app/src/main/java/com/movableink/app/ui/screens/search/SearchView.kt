package com.movableink.app.ui.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.movableink.app.R
import com.movableink.app.ui.component.MovableSearchBar
import com.movableink.app.ui.screens.home.HomeViewModel
import com.movableink.app.utils.rememberFlowWithLifecycle

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    onBackClicked: () -> Unit,
    viewModel: SearchViewModel,
    navigateToProductDetail: (String) -> Unit,
    homeViewModel: HomeViewModel,
) {
    val productSearchModelState by rememberFlowWithLifecycle(viewModel.productSearchModelState)
        .collectAsState(initial = ProductSearchModelState.Empty)
    MovableSearchBar(
        searchText = productSearchModelState.searchText,
        placeholderText = stringResource(id = R.string.search_label),
        onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
        onClearClick = { viewModel.onClearClick() },
        onNavigateBack = onBackClicked,
        matchesFound = productSearchModelState.products.isNotEmpty(),
        matchList = productSearchModelState.products,
        onResultClick = {
            navigateToProductDetail(it)
        },
        homeViewModel,
    )
}
