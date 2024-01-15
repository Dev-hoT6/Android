package com.strayalpaca.hot6.data.review

import com.strayalpaca.hot6.domain.review.Review
import kotlinx.coroutines.delay

class DemoReviewRepository : ReviewRepository {
    private val reviews = (1 until 10).map {
        Review(
            id = it.toString(),
            writerName = "익명$it",
            point = if (it % 3 == 0) 3000 else null,
            text = "익명${it}의 리뷰입니다.\n상품에 대한 상세한 리뷰가 명시되어 있습니다.",
            imageUrl = if (it % 2 == 0) null else ""
        )
    }
    override suspend fun getReviewList(productId: String): List<Review> {
        delay(1000L)
        return reviews
    }
    override suspend fun uploadReviewText(text: String, productId: String): Boolean {
        return productId.isEmpty() || productId[0] == '2'
    }
}