package com.movableink.app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movableink.app.data.model.Product
import com.movableink.app.data.repository.MovableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import java.util.ArrayList

class SearchViewModel
(private val repository: MovableRepository) :
    ViewModel() {

    private val _products: MutableStateFlow<List<Product>> =
        MutableStateFlow(repository.getProducts())
    val products: StateFlow<List<Product>> get() = _products

    private var allProducts: ArrayList<Product> = ArrayList<Product>()
    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedProducts: MutableStateFlow<List<Product>> =
        MutableStateFlow(arrayListOf())

    val productSearchModelState = combine(
        searchText,
        matchedProducts,
        showProgressBar
    ) { text, matchedProducts, showProgress ->

        ProductSearchModelState(
            text,
            matchedProducts,
            showProgress
        )
    }

    init {
        getProducts()
    }

    private fun getProducts() {
        val products = repository.getProducts()

        if (products.isNotEmpty()) {
            allProducts.addAll(products)
        }
    }

    fun onSearchTextChanged(changedSearchText: String) {
        searchText.value = changedSearchText
        if (changedSearchText.isEmpty()) {
            matchedProducts.value = arrayListOf()
            return
        }
        val productsFromSearch = allProducts.filter { product ->
            product.name.contains(changedSearchText, true) ||
                product.category.contains(changedSearchText, true) || product.gender.contains(
                changedSearchText,
                true
            )
        }

        matchedProducts.value = productsFromSearch
    }

    fun onClearClick() {
        searchText.value = ""
        matchedProducts.value = arrayListOf()
    }
    companion object {
        fun provideFactory(
            movableRepository: MovableRepository = MovableRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(movableRepository) as T
            }
        }
    }
}

data class ProductSearchModelState(
    val searchText: String = "",
    val products: List<Product> = arrayListOf(),
    val showProgressBar: Boolean = false
) {

    companion object {
        val Empty = ProductSearchModelState()
    }
}
