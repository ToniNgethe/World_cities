package com.toni.citiesoftheworld.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.toni.citiesoftheworld.data.repositories.CitiesRepository
import com.toni.citiesoftheworld.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val repository: CitiesRepository) : ViewModel() {

    fun fetchCities(query: String? = null): Flow<PagingData<City>> {
        return repository.fetchCities(query).cachedIn(viewModelScope)
    }

}