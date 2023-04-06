package com.task.currencyconverter.core.domain.usecase

import android.util.Log
import com.task.currencyconverter.core.data.Resource
import com.task.currencyconverter.core.domain.model.CountryCode
import com.task.currencyconverter.core.domain.model.Exchange
import com.task.currencyconverter.core.domain.model.History
import com.task.currencyconverter.core.domain.repository.ICurrencyRepository
import io.reactivex.Flowable
import retrofit2.Call
import javax.inject.Inject

class CurrencyInteractor @Inject constructor(private val currencyRepository: ICurrencyRepository): CurrencyUseCase {
    override fun getListCode(): Flowable<Resource<List<CountryCode>>> = currencyRepository.getListCode()
    override fun getExchange(from: String, to: String): Flowable<Resource<Exchange>> = currencyRepository.getExchange(from, to)
    override fun getExchangeCall(from: String, to: String): Call<String> {
        return currencyRepository.getExchangeCall(from,to)
    }

    override fun getHistories(): Flowable<List<History>> = currencyRepository.getHistories()

    override fun insertHistory(history: History){
        Log.e("CurrencyInteractor", "insertHistory()")
        currencyRepository.insertHistory(history)
    }

    override fun deleteHistory(id: Int) {
        currencyRepository.deleteHistory(id)
    }

    override fun deleteAllHistory() {
        currencyRepository.deleteAllHistory()
    }
}