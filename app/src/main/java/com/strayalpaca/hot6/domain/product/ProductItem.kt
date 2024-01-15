package com.strayalpaca.hot6.domain.product

data class ProductItem(
    val id : String,
    val name : String,
    val imageUrl : String,
    val price : Int,
    val category : Category
)