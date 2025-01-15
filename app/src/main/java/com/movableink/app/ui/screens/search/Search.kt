@file:Suppress("ktlint:standard:function-naming")

package com.movableink.app.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movableink.app.R
import com.movableink.app.ui.component.SearchProductList
import com.movableink.app.ui.screens.home.HomeViewModel

@Composable
fun Search(
    onSearchBarClick: () -> Unit,
    searchViewModel: SearchViewModel,
    homeViewModel: HomeViewModel,
    navigateToProductDetail: (String) -> Unit,
) {
    val products by searchViewModel.products.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        SearchViewBar(onSearchBarClick)
        Text(
            stringResource(id = R.string.home_search),
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )

        SearchProductList(products = products) { product ->
            homeViewModel.updateSelectedProduct(product.id)
            navigateToProductDetail(product.id)
        }
    }
}

@Composable
fun SearchViewBar(onSearchBarClick: () -> Unit) {
    TopAppBar(title = { Text("Search Products") }, actions = {
        IconButton(
            modifier = Modifier,
            onClick = onSearchBarClick,
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.app_name),
            )
        }
    })
}
