package com.movableink.app.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movableink.app.R
import com.movableink.app.data.model.Product
import com.movableink.app.data.model.SnackbarManager
import com.movableink.app.data.repository.MovableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel(
    private val snackbarManager: SnackbarManager,
    repository: MovableRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartState())
    val uiState: StateFlow<CartState> = _uiState.asStateFlow()
    fun addItemToCart(product: Product) {
        val updatedCart = _uiState.value.cart.plus(product)
        _uiState.update { currentState ->
            currentState.copy(
                cart = updatedCart,
            )
        }
        snackbarManager.showMessage(R.string.cart_updated)
    }

    init {
        _uiState.value = CartState()
    }

    fun checkoutFromCart() {
        val updatedCart = listOf<Product>()
        _uiState.update { currentState ->
            currentState.copy(
                cart = updatedCart,
            )
        }
        snackbarManager.showMessage(R.string.cart_checked_out)
    }
    companion object {
        fun provideFactory(
            snackbarManager: SnackbarManager = SnackbarManager,
            movableRepository: MovableRepository = MovableRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(snackbarManager, movableRepository) as T
            }
        }
    }
}
data class CartState(
    val cart: List<Product> = arrayListOf(),
)
