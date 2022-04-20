package com.toni.citiesoftheworld.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.services.CitiesService
import com.toni.citiesoftheworld.domain.model.City
import com.toni.citiesoftheworld.domain.repository.CitiesRepository
import com.toni.citiesoftheworld.utils.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CitiesRepositoryImpl @Inject constructor(
    private val citiesDb: CitiesDb,
    private val apiService: CitiesService,
    private val dispatchers: AppDispatchers
) : CitiesRepository {

    override fun fetchCities(query: String?) = Pager(
        config = PagingConfig(15, maxSize = 15 + (15 * 2), enablePlaceholders = true),
        remoteMediator = CitiesRemoteMediator(citiesDb, query, apiService)
    ) {
        citiesDb.cityDao().fetchAllCity()
    }.flow

    override fun fetchCachedCities() = flow {
        val data = citiesDb.cityDao().getAll()
        emit(data)
    }.flowOn(dispatchers.io())
}