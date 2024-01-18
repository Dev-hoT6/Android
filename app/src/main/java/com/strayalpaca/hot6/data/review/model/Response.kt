package com.strayalpaca.hot6.data.review.model

data class ResponseProductReview(
    val count : Int,
    val product : String,
    val detail : List<ReviewDto>
)

data class ResponseGetReviewVector(
    val id : String,
    val vector : String
)

data class ResponsePostReview(
    val score : Int,
    val point : Int
)