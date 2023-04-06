package com.task.currencyconverter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.task.currencyconverter.core.domain.model.History
import com.task.currencyconverter.core.domain.usecase.CurrencyUseCase

class CurrencyViewModel @ViewModelInject constructor(private val currencyUseCase: CurrencyUseCase): ViewModel(),
    LifecycleObserver {
    val currency = LiveDataReactiveStreams.fromPublisher(currencyUseCase.getListCode())
    fun getExchange(fromSet: String, toSet: String) = LiveDataReactiveStreams.fromPublisher(currencyUseCase.getExchange(fromSet, toSet))
    fun getExchange() = LiveDataReactiveStreams.fromPublisher(currencyUseCase.getListCode())

    fun getExchangeCall(fromSet: String, toSet: String) = currencyUseCase.getExchangeCall(fromSet, toSet)

    val history = currencyUseCase.getHistories().toLiveData()
    fun insertHistory(history: History){
        Log.e("CurrencyViewModel", "insertHistory()")
        currencyUseCase.insertHistory(history)
    }
    fun deleteHistory(id: Int){
        currencyUseCase.deleteHistory(id)
    }
    fun deleteAllHistory(){
        currencyUseCase.deleteAllHistory()
    }
}