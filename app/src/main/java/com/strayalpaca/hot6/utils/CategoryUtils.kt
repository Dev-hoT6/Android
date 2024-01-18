package com.strayalpaca.hot6.utils

// {"id":"01","name":"상의"},{"id":"02","name":"하의"},{"id":"03","name":"아우터"},{"id":"04","name":"원피스"},{"id":"05","name":"스커트"},{"id":"06","name":"스포츠"},{"id":"07","name":"기타"}

fun modelPreferenceIndexToCategoryId(index : Int) : String {
    return when(index) {
        0 -> "01"
        1 -> "03"
        2 -> "02"
        3 -> "04"
        4 -> "05"
        else -> "-"
    }
}

fun getCategoryIdByName(name : String) : String {
    return when (name) {
        "상의" -> "01"
        "하의" -> "02"
        "아우터" -> "03"
        "원피스" -> "04"
        "스커트" -> "05"
        else -> "-"
    }
}