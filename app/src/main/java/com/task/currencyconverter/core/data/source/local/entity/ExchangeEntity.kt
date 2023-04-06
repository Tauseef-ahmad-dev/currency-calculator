package com.task.currencyconverter.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange")
data class ExchangeEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "from")
    val from: String,

    @ColumnInfo(name = "to")
    val to: String,

    @ColumnInfo(name = "amount")
    val amount: Double
)
