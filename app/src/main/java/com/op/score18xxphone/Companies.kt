package com.op.score18xxphone

data class Company(
    val name: String,
    val color: String,
    val textColor: String,
    var stockPrice: Int,
    var runs: MutableList<Int> = mutableListOf(0, 0, 0),
    var runsExplicitlySet: MutableList<Boolean> = mutableListOf(false, false, false),
    var shares: MutableList<Int> = mutableListOf()
)

fun Company.colorInt(): Int = runCatching { android.graphics.Color.parseColor(color) }.getOrDefault(android.graphics.Color.GRAY)
fun Company.textColorInt(): Int = runCatching { android.graphics.Color.parseColor(textColor) }.getOrDefault(android.graphics.Color.WHITE)
