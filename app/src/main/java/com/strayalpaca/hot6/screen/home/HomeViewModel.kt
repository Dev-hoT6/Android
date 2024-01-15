package com.strayalpaca.hot6.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.strayalpaca.hot6.data.product.DemoProductRepository
import com.strayalpaca.hot6.data.product.ProductRepository
import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.ProductItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val homeScreenState = _homeScreenState.asStateFlow()

    private val allProductList = mutableListOf<ProductItem>()

    private val loadProductListExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _homeScreenState.value = HomeScreenState.Error
    }

    init {
        loadProductList()
    }

    fun setCategory(categoryId : String?) {
        val currentState = homeScreenState.value
        if (currentState !is HomeScreenState.Success) return

        if (categoryId == currentState.data.selectedCategoryId || categoryId == null) {
            _homeScreenState.value = HomeScreenState.Success(
                currentState.data.copy(
                    productList = allProductList,
                    selectedCategoryId = null
                )
            )
        } else {
            _homeScreenState.value = HomeScreenState.Success(
                currentState.data.copy(
                    productList = allProductList.filter { it.category.id == categoryId },
                    selectedCategoryId = categoryId
                )
            )
        }

    }

    private fun loadProductList() {
        viewModelScope.launch(Dispatchers.IO + loadProductListExceptionHandler) {
            productRepository.getProductList()
                .run {
                    HomeScreenData(
                        categories = this.map { it.category },
                        productList = this,
                        totalCount = this.size
                    )
                }.also {
                    allProductList.clear()
                    allProductList.addAll(it.productList)
                    _homeScreenState.value = HomeScreenState.Success(it)
                }

        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(DemoProductRepository()) as T
            }
        }
    }
}


sealed class HomeScreenState{
    object Loading : HomeScreenState()
    class Success(val data : HomeScreenData) : HomeScreenState()
    object Error : HomeScreenState()
}

data class HomeScreenData(
    val categories : List<Category>,
    val productList : List<ProductItem>,
    val totalCount : Int,
    val selectedCategoryId : String ?= null
)