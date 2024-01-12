package com.strayalpaca.hot6.data.product

import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.Hashtag
import com.strayalpaca.hot6.domain.product.ProductDetail
import com.strayalpaca.hot6.domain.product.ProductItem

class DemoProductRepository : ProductRepository {
    private val dummyDataList = (1 until 9).map { num ->
        ProductDetail(
            id = num.toString(),
            name = "$num 번쨰 상품",
            imageUrl = "",
            price = 139000,
            categories = listOf(Category(id=num.toString(), name = "카테고리$num")),
            caption = "$num 번쨰 상품에 대한 상세 설명이 들어가는 자리입니다.",
            hashtags = listOf(Hashtag(id = num.toString(), name = "$num 번째 태그"))
        )
    }

    override suspend fun getProductList(): List<ProductItem> {
        return dummyDataList.map {
            ProductItem(
                id = it.id,
                name = it.name,
                price = it.price,
                imageUrl = it.imageUrl,
                category = it.categories[0]
            )
        }
    }

    override suspend fun getProductDetail(id: String): ProductDetail {
        return dummyDataList.find { it.id == id } ?: throw IllegalAccessException()
    }
}