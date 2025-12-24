@file:Suppress("ktlint:standard:filename")

package com.movableink.app.utils

import java.math.BigDecimal
import java.text.NumberFormat

fun formatPrice(price: Long): String = NumberFormat.getCurrencyInstance().format(
    BigDecimal(price).movePointLeft(2)
)
