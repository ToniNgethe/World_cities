package com.toni.citiesoftheworld.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.toni.citiesoftheworld.BuildConfig
import com.toni.citiesoftheworld.data.services.CitiesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkhttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }.build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).baseUrl(BuildConfig.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideCitiesService(retrofit: Retrofit): CitiesService =
        retrofit.create(CitiesService::class.java)
}