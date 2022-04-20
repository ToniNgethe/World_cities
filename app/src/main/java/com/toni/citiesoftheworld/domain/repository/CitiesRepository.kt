package com.toni.citiesoftheworld.domain.repository

import androidx.paging.PagingData
import com.toni.citiesoftheworld.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    fun fetchCities(query: String? = null) : Flow<PagingData<City>>
    fun fetchCachedCities( ) : Flow<List<City>>
}