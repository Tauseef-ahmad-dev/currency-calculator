package com.task.currencyconverter.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.currencyconverter.core.data.source.local.entity.CountryCodeEntity
import com.task.currencyconverter.core.data.source.local.entity.ExchangeEntity
import com.task.currencyconverter.core.data.source.local.entity.HistoryEntity

@Database(entities = [CountryCodeEntity::class, ExchangeEntity::class, HistoryEntity::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun currencyDao() : CurrencyDao
}