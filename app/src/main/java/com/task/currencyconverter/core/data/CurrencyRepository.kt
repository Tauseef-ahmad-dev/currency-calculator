package com.task.currencyconverter.core.data

import android.util.Log
import com.task.currencyconverter.core.data.source.local.LocalDataSource
import com.task.currencyconverter.core.data.source.local.entity.ExchangeEntity
import com.task.currencyconverter.core.data.source.remote.RemoteDataSource
import com.task.currencyconverter.core.data.source.remote.network.ApiResponse
import com.task.currencyconverter.core.domain.model.CountryCode
import com.task.currencyconverter.core.domain.model.Exchange
import com.task.currencyconverter.core.domain.model.History
import com.task.currencyconverter.core.domain.repository.ICurrencyRepository
import com.task.currencyconverter.core.utils.AppExecutors
import com.task.currencyconverter.core.utils.DataMapper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): ICurrencyRepository {
    override fun getListCode(): Flowable<Resource<List<CountryCode>>> =
        object : NetworkBoundResource<List<CountryCode>, List<String>>(appExecutors){
            override fun loadFromDB(): Flowable<List<CountryCode>> {
                return localDataSource.getListCode().map {
                    DataMapper.mapCodeEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<CountryCode>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): Flowable<ApiResponse<List<String>>> {
                return remoteDataSource.getListCode()
            }

            override fun saveCallResult(data: List<String>) {
                val codeList = DataMapper.mapCodeResponsesToEntities(data)
                localDataSource.insertListCode(codeList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }

        }.asFlowable()

    override fun getExchange(from: String, to: String): Flowable<Resource<Exchange>> =
        object : NetworkBoundResource<Exchange, String>(appExecutors){
            override fun loadFromDB(): Flowable<Exchange> {
                val id = "${from}to${to}"
                return localDataSource.getExchange(id).map {
                    DataMapper.mapExchangeEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: Exchange?): Boolean =
                data == null

            override fun createCall(): Flowable<ApiResponse<String>> {
                Log.e("CurrencyRepository", "dddd")
                return remoteDataSource.getExchange(from, to)
            }

            override fun saveCallResult(data: String) {
                val id = "${from}to${to}"
                val exchange = ExchangeEntity(
                    id,
                    from,
                    to,
                    data.toDouble()
                )
                localDataSource.insertExchange(exchange)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
        }.asFlowable()

    override fun getExchangeCall(from: String, to: String): Call<String> {
        return remoteDataSource.getExchangeCall(from,to)
    }

    override fun getHistories(): Flowable<List<History>> {
        return localDataSource.getHistories().map {
            Log.e("Currency Repository", "getHistories(): List: $it")
            DataMapper.mapHistoryEntitiesToDomain(it)
        }
    }

    override fun insertHistory(history: History) {
        Log.e("CurrencyRepository", "insertHistory()")
        localDataSource.insertHistory(DataMapper.mapHistoryDomainToEntity(history))
    }

    override fun deleteHistory(id: Int) {
        localDataSource.deleteHistory(id)
    }

    override fun deleteAllHistory() {
        localDataSource.deleteAllHistory()
    }

}