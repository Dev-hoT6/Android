package com.strayalpaca.hot6.data.review

import com.strayalpaca.hot6.data.review.api.ReviewApi
import com.strayalpaca.hot6.data.review.model.PostReviewRequestBody
import com.strayalpaca.hot6.domain.review.Review
import retrofit2.Retrofit

class RemoteReviewRepository(
    retrofit: Retrofit
) : ReviewRepository {
    private val reviewRetrofit = retrofit.create(ReviewApi::class.java)

    override suspend fun getReviewList(productId: String): List<Review> {
        val response = reviewRetrofit.getProductReviewList(productId)
        return response.body()?.let { productReviewResponse ->
            productReviewResponse.detail.sortedByDescending { it.review_id }.map { it.toReview() }
        } ?: throw Exception("server error occur, ${response.message()}, ${response.code()}")
    }

    override suspend fun uploadReviewText(text: String, productId: String): Boolean {
        return true
    }

    override suspend fun getReviewVector(text: String, productId: String): Pair<String, List<List<Double>>> {
        val response = reviewRetrofit.postReviewAndGetVector(productId, PostReviewRequestBody(writer = "hoT6", content = text))
        return response.body()?.let { postReviewResponse ->
            Pair(postReviewResponse.id, stringToListOfListOfFloats(postReviewResponse.vector))
        } ?: throw Exception("server error occur, ${response.message()}, ${response.code()}")
    }

    // [[-1.0155298709869385, -1.4037933349609375], [0.7646861672401428, -0.11003738641738892]] 형식의 String을 List<List<Double>로 변환합니다.
    private fun stringToListOfListOfFloats(input: String): List<List<Double>> {
        val outerList = mutableListOf<List<Double>>()

        val innerStrings = input.substring(2, input.length - 2).split("], [").toTypedArray()

        for (innerString in innerStrings) {
            val innerList = innerString.split(", ").map { it.toDoubleOrNull() ?: 0.0 }
            outerList.add(innerList)
        }

        return outerList
    }
}