package com.op.score18xxphone

import androidx.core.graphics.toColorInt

data class Company(
    val name: String,
    val color: String,
    val textColor: String,
    var stockPrice: Int,
    val maxShares: Int? = null,
    var runs: MutableList<Int> = mutableListOf(0, 0, 0, 0),
    var runsExplicitlySet: MutableList<Boolean> = mutableListOf(false, false, false, false),
    var shares: MutableList<Int> = mutableListOf()
)

fun Company.colorInt(): Int = runCatching { color.toColorInt() }.getOrDefault(android.graphics.Color.GRAY)
fun Company.textColorInt(): Int = runCatching { textColor.toColorInt() }.getOrDefault(android.graphics.Color.WHITE)
