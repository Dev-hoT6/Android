package com.strayalpaca.hot6.data.review

import com.strayalpaca.hot6.domain.review.Review
import java.io.File

interface ReviewRepository {
    suspend fun getReviewList(productId : String) : List<Review>
    suspend fun uploadReview(reviewId : String, image : File?) : Boolean
    suspend fun getReviewVector(text : String, productId: String) : Pair<String, List<List<Double>>>
}