package com.toni.citiesoftheworld.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.toni.citiesoftheworld.data.repositories.CitiesRepository
import com.toni.citiesoftheworld.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CitiesViewModel @Inject constructor(private val repository: CitiesRepository) : ViewModel() {

    private val querySharedFlow = MutableStateFlow("")

    fun fetchCities() = querySharedFlow.debounce(300).distinctUntilChanged().flatMapLatest {
        repository.fetchCities(it)
    }.cachedIn(viewModelScope)

    fun submitQuery(query: String) {
        querySharedFlow.value = query
    }

}