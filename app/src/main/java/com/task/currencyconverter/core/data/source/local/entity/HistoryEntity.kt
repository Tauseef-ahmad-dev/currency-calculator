package com.task.currencyconverter.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "from_code")
    val fromCode: String,
    @ColumnInfo(name = "to_code")
    val toCode: String,
    @ColumnInfo(name = "from_value")
    val fromValue: Double,
    @ColumnInfo(name = "to_value")
    val toValue: Double
)