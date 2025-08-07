package com.movableink.app.ui.screens.home

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movableink.app.data.model.Product
import com.movableink.app.data.repository.MovableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val repository: MovableRepository,
) : ViewModel() {
    private val _homeUIState = MutableStateFlow(HomeState())
    val homeUIState: StateFlow<HomeState> = _homeUIState.asStateFlow()

    private fun fetchCategories(gender: String) = repository.getCategoriesByGender(gender)

    fun updateCategories(gender: String) {
        val lowerCase = gender.toLowerCase(Locale.current)
        val categories = fetchCategories(lowerCase)
        _homeUIState.update { currentState ->
            currentState.copy(
                categories = categories,
                gender = lowerCase,
            )
        }
    }

    fun updateCatalogByCategory(
        category: String,
        gender: String,
    ) {
        val catalog =
            repository.getProductsByCategoryAndGender(category, gender)

        _homeUIState.update { currentState ->
            currentState.copy(
                catalog = catalog,
            )
        }
    }

    fun updateSelectedProduct(product: String) {
        _homeUIState.update { currentState ->
            currentState.copy(
                selectedProductId = product,
            )
        }
    }

    init {
        _homeUIState.value = HomeState()
    }

    companion object {
        fun provideFactory(movableRepository: MovableRepository = MovableRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(movableRepository) as T
            }
    }

    data class HomeState(
        val categories: List<String> = arrayListOf(),
        val catalog: List<Product> = arrayListOf(),
        val gender: String = "",
        val selectedProductId: String = "",
        val msp: String = "",
    )
}
