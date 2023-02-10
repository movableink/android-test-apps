package com.movableink.app.data.repository

import com.movableink.app.data.model.Product
import com.movableink.app.data.model.productList

object MovableRepository {
    fun getProducts(): List<Product> = productList

    fun getCategoriesByGender(gender: String) = productList.filter { it.gender == gender }
        .map { it.category }.distinct()

    fun getProductsByCategoryAndGender(selectedCategory: String, gender: String): List<Product> =
        productList.filter { it.category == selectedCategory && it.gender.contains(gender, ignoreCase = true) }

    fun getProductById(id: String) =
        productList.find { it.id == id }
}
