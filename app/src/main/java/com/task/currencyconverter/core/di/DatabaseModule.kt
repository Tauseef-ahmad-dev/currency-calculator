package com.task.currencyconverter.core.di

import android.content.Context
import androidx.room.Room
import com.task.currencyconverter.core.data.source.local.room.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : CurrencyDatabase{
        return Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "Currency.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCurrencyDao(database: CurrencyDatabase) = database.currencyDao()
}