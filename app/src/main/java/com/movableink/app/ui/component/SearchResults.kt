package com.movableink.app.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movableink.app.data.model.Product
import com.movableink.app.utils.formatPrice

@Composable
fun SearchProductList(products: List<Product>?, onClick: (Product) -> Unit) {
    if (!products.isNullOrEmpty()) {
        LazyColumn {
            items(products) { product ->
                SearchRow(product) {
                    onClick(product)
                }
                Divider()
            }
        }
    }
}

@Composable
fun SearchRow(product: Product, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(formatPrice(product.price))
        Spacer(modifier = Modifier.height(4.dp))
    }
}
