package com.task.currencyconverter.core.data.source.local

import com.task.currencyconverter.core.data.source.local.entity.CountryCodeEntity
import com.task.currencyconverter.core.data.source.local.entity.ExchangeEntity
import com.task.currencyconverter.core.data.source.local.entity.HistoryEntity
import com.task.currencyconverter.core.data.source.local.room.CurrencyDao
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val currencyDao: CurrencyDao) {
    fun getListCode(): Flowable<List<CountryCodeEntity>> = currencyDao.getListCode()
    fun insertListCode(code: List<CountryCodeEntity>) = currencyDao.insertListCode(code)

    fun getExchange(id: String): Flowable<ExchangeEntity> = currencyDao.getExchange(id)
    fun insertExchange(exchange: ExchangeEntity) = currencyDao.insertExchange(exchange)

    fun getHistories(): Flowable<List<HistoryEntity>> = currencyDao.getHistories()
    fun insertHistory(historyEntity: HistoryEntity) {
        Observable.fromCallable(object : Callable<Boolean>{
            override fun call(): Boolean {
                currencyDao.insertHistory(historyEntity)
                return true
            }

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun deleteHistory(id: Int){
        Observable.fromCallable(object : Callable<Boolean>{
            override fun call(): Boolean {
                currencyDao.deleteHistory(id)
                return true
            }

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun deleteAllHistory(){
        Observable.fromCallable(object : Callable<Boolean>{
            override fun call(): Boolean {
                currencyDao.deleteAllHistory()
                return true
            }

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}