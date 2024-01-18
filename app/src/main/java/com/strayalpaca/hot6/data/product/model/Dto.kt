package com.strayalpaca.hot6.data.product.model

import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.ProductItem

data class ProductItemDto(
    val id : String,
    val name : String,
    val price : Int,
    val img_url : String
) {
    fun toProductItem() : ProductItem {
        return ProductItem(
            id = id,
            name = name,
            price = price,
            imageUrl = img_url
        )
    }
}

data class CategoryDto(
    val id : String,
    val name : String
) {
    fun toCategory() : Category {
        return Category(
            id = id,
            name = name
        )
    }
}