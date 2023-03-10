package com.movableink.app.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movableink.app.R
import com.movableink.app.data.model.Product
import com.movableink.app.ui.screens.cart.CartViewModel
import com.movableink.app.utils.fetchDrawableByName
import com.movableink.app.utils.formatPrice
import com.movableink.inked.MIClient

@Composable
fun CatalogScreen(
    category: String?,
    navigateToProductDetail: (String) -> Unit,
    onBackClicked: () -> Unit,
    viewModel: CartViewModel,
    onViewCart: () -> Unit,
    homeViewModel: HomeViewModel
) {
    val homeUIState by homeViewModel.homeUIState.collectAsStateWithLifecycle()

    val productList = homeUIState.catalog
    val cartUiState by viewModel.uiState.collectAsState()
    val gender = remember { homeUIState.gender }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.statusBars.add(WindowInsets(top = 60.dp))
            )
        )
        if (cartUiState.cart.isNotEmpty()) {
            CartItems(cartUiState.cart, onViewCart)
        }
        Products(products = productList, onProductClick = navigateToProductDetail, homeViewModel)
    }
    CatalogAppBar(gender = gender, category = "$category".capitalize(Locale.current), onBackClicked = onBackClicked)
}

@Composable
private fun Products(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    homeViewModel: HomeViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductItem(product, onProductClick, homeViewModel)
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (String) -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(
                width = 250.dp,
                height = 250.dp
            )
            .padding(all = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = {
                    homeViewModel.updateSelectedProduct(product.id)
                    MIClient.productViewed(mapOf("id" to product.id))
                    onProductClick(product.id)
                })
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .background(
                        colorResource(id = android.R.color.transparent)
                    )
            ) {
                ProductImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                        .align(Alignment.TopCenter)
                )
            }
            Text(
                text = product.name.uppercase(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            // Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatPrice(product.price),
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp)

            )
        }
    }
}

@Composable
fun ProductImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        fetchDrawableByName(imageUrl, context = LocalContext.current).apply {
            val painter: Painter = painterResource(id = this)
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = painter,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun CatalogAppBar(modifier: Modifier = Modifier, gender: String?, category: String, onBackClicked: () -> Unit) {
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
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.app_name)
                )
            }
            Text(
                text = "$gender >>> $category",
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
fun CartItems(cart: List<Product>, onViewCart: () -> Unit) {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.ShoppingCart, "") },
        text = { Text("View Cart ( ${cart.size} )") },
        onClick = onViewCart,
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}
