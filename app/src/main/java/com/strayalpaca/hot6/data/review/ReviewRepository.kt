package com.strayalpaca.hot6.data.review

import com.strayalpaca.hot6.domain.review.Review

interface ReviewRepository {
    suspend fun getReviewList(productId : String) : List<Review>
}