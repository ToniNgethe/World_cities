package com.toni.citiesoftheworld.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.services.CitiesService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CitiesRepository @Inject constructor(
    private val citiesDb: CitiesDb, private val apiService: CitiesService
) {

    fun fetchCities(query: String? = null) = Pager(
        config = PagingConfig(15, maxSize = 15 + (15 * 2), enablePlaceholders = true),
        remoteMediator = CitiesRemoteMediator(citiesDb, query, apiService)
    ) {
        if (query == null) citiesDb.cityDao().fetchAllCity() else citiesDb.cityDao()
            .fetchAllCityByQuery(query)
    }.flow
}