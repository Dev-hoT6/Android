package com.strayalpaca.hot6.data.review

import com.strayalpaca.hot6.data.review.api.ReviewApi
import com.strayalpaca.hot6.domain.review.Review
import retrofit2.Retrofit

class RemoteReviewRepository(
    retrofit: Retrofit
) : ReviewRepository {
    private val reviewRetrofit = retrofit.create(ReviewApi::class.java)

    override suspend fun getReviewList(productId: String): List<Review> {
        val response = reviewRetrofit.getProductReviewList(productId)
        return response.body()?.let { productReviewResponse ->
            productReviewResponse.detail.map { it.toReview() }
        } ?: throw Exception("server error occur, ${response.message()}, ${response.code()}")
    }

    override suspend fun uploadReviewText(text: String, productId: String): Boolean {
        return true
    }
}