package com.strayalpaca.hot6.data.review.model

data class ResponseProductReview(
    val count : Int,
    val product : String,
    val detail : List<ReviewDto>
)