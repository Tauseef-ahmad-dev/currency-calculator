package com.task.currencyconverter.core.data.source.local.room

import androidx.room.*
import com.task.currencyconverter.core.data.source.local.entity.CountryCodeEntity
import com.task.currencyconverter.core.data.source.local.entity.ExchangeEntity
import com.task.currencyconverter.core.data.source.local.entity.HistoryEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM country_code")
    fun getListCode(): Flowable<List<CountryCodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListCode(code: List<CountryCodeEntity>): Completable

    @Query("SELECT * FROM exchange WHERE id = :id")
    fun getExchange(id: String): Flowable<ExchangeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchange(exchange: ExchangeEntity): Completable

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getHistories(): Flowable<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: HistoryEntity)

    @Query("DELETE FROM history WHERE id=:id")
    fun deleteHistory(id: Int)

    @Query("DELETE FROM history")
    fun deleteAllHistory()

}