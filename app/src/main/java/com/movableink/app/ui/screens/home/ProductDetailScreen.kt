package com.movableink.app.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movableink.app.R
import com.movableink.app.data.model.Product
import com.movableink.app.data.repository.MovableRepository
import com.movableink.app.ui.screens.cart.CartViewModel
import com.movableink.app.utils.getDrawableId

val BottomBarHeight = 56.dp

@Composable
fun ProductDetailScreen(
    popBackStack: () -> Unit,
    viewModel: CartViewModel,
    homeViewModel: HomeViewModel,
) {
    val homeUIState by homeViewModel.homeUIState.collectAsStateWithLifecycle()
    val productId = homeUIState.selectedProductId
    val product = remember { MovableRepository.getProductById(productId) }
    Box(Modifier.fillMaxSize()) {
        DetailsView(product = product)
        CartBottomBar(modifier = Modifier.align(Alignment.BottomCenter), viewModel, product, popBackStack)
        DetailTopBar(
            product = product,
            onBackClicked = popBackStack,
        )
    }
}

@Composable
private fun CartBottomBar(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel,
    product: Product?,
    onCartUpdate: () -> Unit,
) {
    Surface(modifier) {
        Column {
            ScreenDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding()
                    .then(Modifier.padding(horizontal = 24.dp))
                    .heightIn(min = BottomBarHeight),
            ) {
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = {
                        if (product != null) {
                            onCartUpdateAction(viewModel, product, onCartUpdate)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 18.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.addToCart),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun ScreenDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
    )
}

@Composable
fun DetailsView(product: Product?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp),
    ) {
        item {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 60.dp)),
                ),
            )
            product.apply {
                val drawable = getDrawableId(product?.imageUrl.toString()) ?: R.drawable.placeholder
                val image: Painter = painterResource(id = drawable)
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    painter = image,
                    alignment = Alignment.CenterStart,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = this?.name ?: "",
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                )
            }
        }
    }
}

@Composable
fun DetailTopBar(modifier: Modifier = Modifier, product: Product?, onBackClicked: () -> Unit) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = Color.Black,
            elevation = 0.dp,

        ) {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier
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
                text = "${product?.name}",
                style = MaterialTheme.typography.h4,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterVertically),
            )
        }
    }
}

fun onCartUpdateAction(viewModel: CartViewModel, product: Product, onCartUpdate: () -> Unit) {
    viewModel.addItemToCart(product)
    onCartUpdate.invoke()
}
