package com.task.currencyconverter.core.domain.model

data class History(
    val id: Int = 0,
    val fromCode: String,
    val toCode: String,
    val fromValue: Double,
    val toValue: Double
)
