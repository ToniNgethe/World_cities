package com.toni.citiesoftheworld.di

import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.repositories.CitiesRepositoryImpl
import com.toni.citiesoftheworld.data.services.CitiesService
import com.toni.citiesoftheworld.domain.repository.CitiesRepository
import com.toni.citiesoftheworld.utils.AppDispatchers
import com.toni.citiesoftheworld.utils.MainAppDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesDispatcher(): AppDispatchers = MainAppDispatcher()


    @Provides
    @Singleton
    fun provideRepository(
        citiesDb: CitiesDb, citiesService: CitiesService, appDispatchers: AppDispatchers
    ): CitiesRepository = CitiesRepositoryImpl(
        citiesDb, citiesService, appDispatchers
    )
}