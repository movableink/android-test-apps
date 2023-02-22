package com.movableink.app.ui.navigation

import androidx.core.net.toUri
import com.movableink.app.MainDestinations

object DeepLinkPattern {
    val PRODUCT = "product"
    val baseDestination = "https://movableink-inkredible-retail.herokuapp.com/product".toUri()
    val productDetail = "$baseDestination/${MainDestinations.PRODUCT_DETAIL_ROUTE}/{${MainDestinations.PRODUCT_ID}}"
    fun getProductDetailUri() = productDetail.toUri()
}
