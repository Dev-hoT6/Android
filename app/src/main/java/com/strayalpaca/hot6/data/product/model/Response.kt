package com.strayalpaca.hot6.data.product.model

import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.ProductDetail

data class ResponseProductList(
    val n_goods : Int,
    val category : List<CategoryDto>,
    val goods : List<ProductItemDto>
)

data class ResponseProductDetail(
    val product_name : String,
    val product_hashtags : String?,
    val product_price : Int,
    val product_image_url : String,
    val category1_name : String
) {
    fun mapToProductDetail() : ProductDetail {
        return ProductDetail(
            id = product_name,
            name = product_name,
            imageUrl = product_image_url,
            price = product_price,
            caption = "${product_name}에 대한 상세설명입니다.",
            categories = listOf(Category("-", category1_name)),
            hashtags = product_hashtags?.split(" ") ?: emptyList()
        )
    }
}