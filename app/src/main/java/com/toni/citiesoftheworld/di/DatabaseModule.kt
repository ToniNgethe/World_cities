package com.toni.citiesoftheworld.di

import android.content.Context
import androidx.room.Room
import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.local.CityDao
import com.toni.citiesoftheworld.data.local.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CitiesDb =
        Room.databaseBuilder(context, CitiesDb::class.java, "cities_db").build()


    @Provides
    @Singleton
    fun providesCityDao(citiesDb: CitiesDb): CityDao = citiesDb.cityDao()

    @Provides
    @Singleton
    fun providesRemoteKeyDao(citiesDb: CitiesDb): RemoteKeyDao = citiesDb.remoteKeyDao()
}