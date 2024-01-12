package com.strayalpaca.hot6.data.product

import com.strayalpaca.hot6.domain.product.ProductDetail
import com.strayalpaca.hot6.domain.product.ProductItem

interface ProductRepository {
    suspend fun getProductList() : List<ProductItem>
    suspend fun getProductDetail(id : String) : ProductDetail
}