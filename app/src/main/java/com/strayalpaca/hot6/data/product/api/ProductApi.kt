package com.strayalpaca.hot6.data.product.api

import com.strayalpaca.hot6.data.product.model.ResponseProductDetail
import com.strayalpaca.hot6.data.product.model.ResponseProductList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("product")
    suspend fun getProductList() : Response<ResponseProductList>

    @GET("product/category/{Category_ID}")
    suspend fun getProductListByCategory(@Path("Category_ID") Category_ID : String) : Response<ResponseProductList>

    @GET("detail/product/{ProductID}")
    suspend fun getProductDetail(@Path("ProductID") ProductID : String) : Response<ResponseProductDetail>
}