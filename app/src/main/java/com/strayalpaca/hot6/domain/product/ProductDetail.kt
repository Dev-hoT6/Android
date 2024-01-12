package com.strayalpaca.hot6.domain.product

data class ProductDetail(
    val id : String,
    val name : String,
    val imageUrl : String,
    val price : Int,
    val categories : List<Category>,
    val caption : String,
    val hashtags : List<String>
)