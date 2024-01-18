package com.strayalpaca.hot6.data.product

import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.ProductDetail
import com.strayalpaca.hot6.domain.product.ProductItem

class DemoProductRepository : ProductRepository {
    private fun getCategory(idx : Int) : String {
        return when (idx) {
            1 -> "Top"
            2 -> "Outer"
            3 -> "Pants"
            4 -> "Onepiecs"
            5 -> "Skirt"
            else -> "카테고리 미정"
        }
    }

    private val dummyDataList = (1 until 9).map { num ->
        ProductDetail(
            id = num.toString(),
            name = "${num}번째 상품",
            imageUrl = "",
            price = 139000,
            categories = listOf(Category(id=(num - 1).toString(), name = getCategory(num)), Category(id=(num - 1).toString(), name = "서브카테고리$num")),
            caption = "$num 번쨰 상품에 대한 상세 설명이 들어가는 자리입니다.",
            hashtags = listOf("$num 번째 태그")
        )
    }

    override suspend fun getProductList(): Pair<List<ProductItem>, List<Category>> {
        val productList = dummyDataList.map {
            ProductItem(
                id = it.id,
                name = it.name,
                price = it.price,
                imageUrl = it.imageUrl
            )
        }
        val categoryList = dummyDataList.map { it.categories[0] }
        return Pair(productList, categoryList)
    }

    override suspend fun getProductListByCategory(categoryId: String): List<ProductItem> {
        return dummyDataList.asSequence().filter { product ->
            product.categories.map { it.id }.contains(categoryId)
        }.map {
            ProductItem(
                id = it.id,
                name = it.name,
                price = it.price,
                imageUrl = it.imageUrl
            )
        }.toList()
    }

    override suspend fun getProductDetail(id: String): ProductDetail {
        return dummyDataList.find { it.id == id } ?: throw IllegalAccessException()
    }
}