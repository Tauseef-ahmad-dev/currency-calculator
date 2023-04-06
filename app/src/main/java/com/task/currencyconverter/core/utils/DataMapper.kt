package com.task.currencyconverter.core.utils

import com.task.currencyconverter.core.data.source.local.entity.CountryCodeEntity
import com.task.currencyconverter.core.data.source.local.entity.ExchangeEntity
import com.task.currencyconverter.core.data.source.local.entity.HistoryEntity
import com.task.currencyconverter.core.domain.model.CountryCode
import com.task.currencyconverter.core.domain.model.Exchange
import com.task.currencyconverter.core.domain.model.History

object DataMapper {
    fun mapCodeResponsesToEntities(input: List<String>): List<CountryCodeEntity>{
        val list = ArrayList<CountryCodeEntity>()
        input.map{
            val code = CountryCodeEntity(
                code = it
            )
            list.add(code)
        }
        return list
    }

    fun mapCodeEntitiesToDomain(input: List<CountryCodeEntity>): List<CountryCode>{
        val list = ArrayList<CountryCode>()
        input.map {
            val code = CountryCode(
                code = it.code
            )
            list.add(code)
        }
        return list
    }

    fun mapExchangeResponsesToEntities(input: Double, from: String, to: String): ExchangeEntity{
        return ExchangeEntity(
            "${from}to${to}",
            from,
            to,
            input
        )
    }

    fun mapExchangeEntitiesToDomain(input: ExchangeEntity): Exchange {
        return Exchange(
            input.id,
            input.from,
            input.to,
            input.amount
        )
    }

    fun mapHistoryEntitiesToDomain(input: List<HistoryEntity>): List<History>{
        val list = ArrayList<History>()
        input.map {
            val history = History(
                it.id,
                it.fromCode,
                it.toCode,
                it.fromValue,
                it.toValue
            )
            list.add(history)
        }
        return list
    }

    fun mapHistoryDomainToEntity(input: History): HistoryEntity{
        return HistoryEntity(
            fromCode = input.fromCode,
            toCode = input.toCode,
            fromValue = input.fromValue,
            toValue = input.toValue
        )
    }
}