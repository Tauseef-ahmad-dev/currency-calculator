package com.task.currencyconverter.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_code")
data class CountryCodeEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "code")
    val code: String
)
