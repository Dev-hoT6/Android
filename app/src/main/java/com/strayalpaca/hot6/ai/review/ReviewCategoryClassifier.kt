package com.strayalpaca.hot6.ai.review

interface ReviewCategoryClassifier {
    fun isLoaded() : Boolean
    fun load()
    fun close()
    fun preferenceRelated(vector : List<List<Double>>) : Boolean
}