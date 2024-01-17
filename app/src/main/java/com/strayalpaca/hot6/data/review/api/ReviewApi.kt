package com.strayalpaca.hot6.data.review.api

import com.strayalpaca.hot6.data.review.model.ResponseProductReview
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewApi {
    @GET("detail/reviews/{ProductID}")
    suspend fun getProductReviewList(@Path("ProductID") ProductID : String) : Response<ResponseProductReview>
}