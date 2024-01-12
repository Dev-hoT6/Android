package com.strayalpaca.hot6.utils

fun List<*>.joinToStringExceptSingle(separator: CharSequence) : String {
    return when(size) {
        0 -> {
            "-"
        }
        1 -> {
            this[0].toString()
        }
        else -> {
            this.joinToString(separator)
        }
    }

}