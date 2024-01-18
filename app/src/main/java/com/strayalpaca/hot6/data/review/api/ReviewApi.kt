package com.strayalpaca.hot6.data.review.api

import com.strayalpaca.hot6.data.review.model.PostReviewRequestBody
import com.strayalpaca.hot6.data.review.model.ResponseProductReview
import com.strayalpaca.hot6.data.review.model.ResponseGetReviewVector
import com.strayalpaca.hot6.data.review.model.ResponsePostReview
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ReviewApi {
    @GET("detail/reviews/{ProductID}")
    suspend fun getProductReviewList(@Path("ProductID") ProductID : String) : Response<ResponseProductReview>

    @POST("review/create/{Product_ID}")
    suspend fun postReviewAndGetVector(@Path("Product_ID") Product_ID : String, @Body params : PostReviewRequestBody) : Response<ResponseGetReviewVector>

    @Multipart
    @POST("review/create/submit/{Review_ID}")
    suspend fun postAvailableReview(
        @Path("Review_ID") Review_ID : String,
        @Part file : MultipartBody.Part,
    ) : Response<ResponsePostReview>

}