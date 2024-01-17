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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val loadProductListExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _homeScreenState.value = HomeScreenState.Error()
    }

    init {
        loadProductList()
    }

    fun setCategory(categoryId : String?) {
        val currentState = homeScreenState.value
        if (currentState !is HomeScreenState.Success) return

        _homeScreenState.update { HomeScreenState.Loading(it.data) }
        if (categoryId == currentState.data.selectedCategoryId || categoryId == null) {
            loadProductList()
        } else {
            loadProductListByCategory(categoryId)
        }

    }

    private fun loadProductList() {
        viewModelScope.launch(Dispatchers.IO + loadProductListExceptionHandler) {
            productRepository.getProductList()
                .run { // first = List<Product>, second = List<Category>
                    HomeScreenData(
                        categories = second,
                        productList = first,
                        totalCount = first.size,
                        selectedCategoryId = null
                    )
                }.also {
                    _homeScreenState.value = HomeScreenState.Success(it)
                }

        }
    }

    private fun loadProductListByCategory(categoryId : String) {
        viewModelScope.launch(Dispatchers.IO + loadProductListExceptionHandler) {
            productRepository.getProductListByCategory(categoryId)
                .also { productList ->
                    _homeScreenState.update { HomeScreenState.Success(it.data.copy(productList = productList, totalCount = productList.size, selectedCategoryId = categoryId)) }
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


sealed class HomeScreenState(val data : HomeScreenData){
    class Loading(data : HomeScreenData = HomeScreenData()) : HomeScreenState(data)
    class Success(data : HomeScreenData) : HomeScreenState(data)
    class Error(data : HomeScreenData = HomeScreenData()) : HomeScreenState(data)
}

data class HomeScreenData(
    val categories : List<Category> = emptyList(),
    val productList : List<ProductItem> = emptyList(),
    val totalCount : Int = 0,
    val selectedCategoryId : String ?= null
)