package com.strayalpaca.hot6.data.product

import com.strayalpaca.hot6.data.product.api.ProductApi
import com.strayalpaca.hot6.domain.product.Category
import com.strayalpaca.hot6.domain.product.ProductDetail
import com.strayalpaca.hot6.domain.product.ProductItem
import retrofit2.Retrofit

class RemoteProductRepository(
    retrofit: Retrofit
) : ProductRepository {
    private val productRetrofit = retrofit.create(ProductApi::class.java)

    override suspend fun getProductList(): Pair<List<ProductItem>, List<Category>> {
        val response = productRetrofit.getProductList()
        return response.body()?.let { productListResponse ->
            Pair(productListResponse.goods.map { it.toProductItem() }, productListResponse.category.map { it.toCategory() })
        } ?: throw Exception("server error occur, ${response.message()}, ${response.code()}")
    }

    override suspend fun getProductListByCategory(categoryId: String): List<ProductItem> {
        val response = productRetrofit.getProductListByCategory(categoryId)
        return response.body()?.let { productListResponse ->
            productListResponse.goods.map { it.toProductItem() }
        } ?: throw Exception("server error occur, ${response.message()}, ${response.code()}")
    }

    override suspend fun getProductDetail(id: String): ProductDetail {
        val response = productRetrofit.getProductDetail(id)
        return response.body()?.mapToProductDetail() ?: throw Exception("server error occur, ${response.message()}, ${response.code()}")
    }
}