package com.strayalpaca.hot6.data.product

import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.ProductDetail
import com.strayalpaca.hot6.domain.product.ProductItem

interface ProductRepository {
    suspend fun getProductList() : Pair<List<ProductItem>, List<Category>>
    suspend fun getProductListByCategory(categoryId : String) : List<ProductItem>
    suspend fun getProductDetail(id : String) : ProductDetail
}