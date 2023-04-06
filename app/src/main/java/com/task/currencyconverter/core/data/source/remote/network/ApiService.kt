package com.task.currencyconverter.core.data.source.remote.network

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("listquotes")
    fun getListCode(): Flowable<List<String>>

    @GET("exchange")
    fun getExchange(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("q") q: Int
    ): Flowable<String>

    @GET("exchange")
    fun getExchangeCall(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("q") q: Int
    ): Call<String>
}