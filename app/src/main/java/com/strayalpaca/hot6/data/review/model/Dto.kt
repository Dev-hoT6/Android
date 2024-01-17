package com.strayalpaca.hot6.data.review.model

import com.strayalpaca.hot6.domain.review.Review

data class ReviewDto(
    val review_id : String,
    val review_writer : String,
    val review_content : String,
    val review_image_path : String?,
    val review_status : Int
) {
    fun toReview() : Review {
        return Review(
            id = review_id,
            writerName = review_writer,
            text = review_content,
            imageUrl = review_image_path
        )
    }
}