package com.strayalpaca.hot6.ai.classifier

interface ImageCategoryClassifier {
    fun isLoaded() : Boolean
    fun load()
    fun close()
    fun preferenceByUrl(url : String, categoryIds : List<String>) : Boolean
}