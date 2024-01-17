package com.strayalpaca.hot6.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.strayalpaca.hot6.ai.classifier.ImageCategoryClassifier
import com.strayalpaca.hot6.data.review.ReviewRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val imageCategoryClassifier: ImageCategoryClassifier
) : ViewModel() {
    private val _reviewText = MutableStateFlow("")
    val reviewText = _reviewText.asStateFlow()

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    private val _reviewState = MutableStateFlow<ReviewState>(ReviewState.IDLE)
    val reviewState = _reviewState.asStateFlow()

    private val _showEmptyTextMessage = MutableSharedFlow<Boolean>()
    val showEmptyTextMessage = _showEmptyTextMessage.asSharedFlow()

    private var productId : String = "-"
    var categoryIdList : List<String> = emptyList()
        private set

    private val reviewUploadErrorHandler = CoroutineExceptionHandler{ _, _ ->
        _reviewState.value = ReviewState.Error
    }

    override fun onCleared() {
        super.onCleared()
        imageCategoryClassifier.close()
    }

    fun setProductIdAndCategoryId(productId : String, categoryIdList : List<String>) {
        this.productId = productId
        this.categoryIdList = categoryIdList
    }

    fun setReviewText(text : String) {
        _reviewText.value = text
    }

    fun setImage(imageUrl : String?) {
        viewModelScope.launch(Dispatchers.Default) {
            if (imageUrl == null) {
                _imageUrl.update { null }
                return@launch
            }

            if (!imageCategoryClassifier.isLoaded()) {
                _reviewState.update { ReviewState.ImageModelLoading }
                imageCategoryClassifier.load()
                _reviewState.update { ReviewState.IDLE }
            }

            imageCategoryClassifier.preferenceByUrl(imageUrl, categoryIdList).run {
                if (this) {
                    _imageUrl.update { imageUrl }
                } else {
                    _reviewState.update { ReviewState.ImageReject }
                }
            }

        }
    }

    fun uploadReview() {
        viewModelScope.launch(Dispatchers.IO + reviewUploadErrorHandler) {
            if (reviewText.value.isEmpty()) {
                _showEmptyTextMessage.emit(true)
                return@launch
            }

            reviewRepository.uploadReviewText(reviewText.value, productId).also { success ->
                if (success) _reviewState.value = ReviewState.UploadSuccess
                else _reviewState.value = ReviewState.Reject
            }
        }
    }

    fun closeRejectDialog() {
        _reviewState.value = ReviewState.IDLE
    }

    companion object {
        class Factory(
            private val reviewRepository: ReviewRepository,
            private val imageCategoryClassifier: ImageCategoryClassifier
        ) : ViewModelProvider.Factory  {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ReviewViewModel(reviewRepository, imageCategoryClassifier) as T
            }
        }
    }
}

sealed class ReviewState {
    object IDLE : ReviewState()
    object Loading : ReviewState()
    object Reject : ReviewState()
    object UploadSuccess : ReviewState()
    object Error : ReviewState()
    object ImageModelLoading : ReviewState()
    object ImageReject : ReviewState()
}
