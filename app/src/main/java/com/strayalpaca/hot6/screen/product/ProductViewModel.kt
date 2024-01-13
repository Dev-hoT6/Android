package com.strayalpaca.hot6.screen.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.strayalpaca.hot6.data.product.DemoProductRepository
import com.strayalpaca.hot6.data.product.ProductRepository
import com.strayalpaca.hot6.data.review.DemoReviewRepository
import com.strayalpaca.hot6.data.review.ReviewRepository
import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.review.Review
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _productDetailState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val productDetailState = _productDetailState.asStateFlow()

    private val _reviewListState = MutableStateFlow<ReviewListState>(ReviewListState.Loading)
    val reviewListState = _reviewListState.asStateFlow()

    private var productId : String = "-"

    private val loadProductDetailExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _productDetailState.value = ProductDetailState.Error
    }

    private val loadReviewListExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _reviewListState.value = ReviewListState.Error
    }

    fun getProductId() = productId

    fun getCategoryIdList() : List<String> {
        val currentState = productDetailState.value
        if (currentState !is ProductDetailState.Success) {
            return emptyList()
        }

        return currentState.data.categories.map { it.id }
    }

    fun setProductId(productId: String) {
        this.productId = productId
    }

    fun loadProductDetail() {
        viewModelScope.launch(Dispatchers.IO + loadProductDetailExceptionHandler) {
            productRepository.getProductDetail(productId)
                .run {
                    ProductDetailData(
                        id = this.id,
                        imageUrl = this.imageUrl,
                        name = this.name,
                        price = this.price,
                        caption = this.caption,
                        hashtags = this.hashtags.map { "#$it" },
                        categories = this.categories
                    )
                }.also {
                    _productDetailState.value = ProductDetailState.Success(it)
                }
        }
    }

    fun loadReviewList() {
        viewModelScope.launch(Dispatchers.IO + loadReviewListExceptionHandler) {
            reviewRepository.getReviewList(productId)
                .run {
                    ReviewListData(
                        reviews = this,
                        totalCount = this.size
                    )
                }.also {
                    _reviewListState.value =  ReviewListState.Success(it)
                }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductViewModel(DemoProductRepository(), DemoReviewRepository()) as T
            }
        }
    }

}

sealed class ProductDetailState{
    object Loading : ProductDetailState()
    class Success(val data : ProductDetailData) : ProductDetailState()
    object Error : ProductDetailState()
}

data class ProductDetailData(
    val id : String,
    val imageUrl : String,
    val name : String,
    val price : Int,
    val caption : String,
    val hashtags : List<String>,
    val categories : List<Category>
)

sealed class ReviewListState {
    object Loading : ReviewListState()
    class Success(val data : ReviewListData) : ReviewListState()
    object Error : ReviewListState()
}

data class ReviewListData(
    val reviews : List<Review>,
    val totalCount : Int
)