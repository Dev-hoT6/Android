package com.strayalpaca.hot6.domain.review

data class Review(
    val id : String,
    val writerName : String,
    val point : Int ?= null,
    val text : String,
    val imageUrl : String ?= null
)