package com.movableink.app.ui.screens.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.movableink.app.R
import com.movableink.app.ui.component.CheckoutDialog
import com.movableink.app.ui.component.MovableTopBar
import com.movableink.app.ui.screens.home.BottomBarHeight
import com.movableink.app.ui.screens.home.ScreenDivider

@Composable
fun Cart(
    viewModel: CartViewModel,
) {
    val cartUiState by viewModel.uiState.collectAsState()
    val cartList = cartUiState.cart
    var showDialog by remember { mutableStateOf(false) }
    LazyColumn {
        item {
            MovableTopBar(title = stringResource(id = R.string.home_cart))
        }
        item {
            if (showDialog) {
                CheckoutDialog(setShowDialog = {
                    showDialog = it
                }, viewModel)
            }
        }
        if (cartList.isNotEmpty()) {
            items(cartList) { product ->
                CartItem(product)
            }
            item {
                Surface(Modifier) {
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
                                    showDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(all = 16.dp),
                            ) {
                                Text(
                                    text = "Checkout ",
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
        } else {
            // show empty Cart State
            item {
                Text(
                    text = stringResource(R.string.empty_cart),
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
