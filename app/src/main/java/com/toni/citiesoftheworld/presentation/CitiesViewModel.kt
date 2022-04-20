package com.toni.citiesoftheworld.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.toni.citiesoftheworld.domain.model.City
import com.toni.citiesoftheworld.domain.repository.CitiesRepository
import com.toni.citiesoftheworld.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CitiesViewModel @Inject constructor(private val repository: CitiesRepository, private val dispatchers: AppDispatchers) : ViewModel() {

    private val querySharedFlow = MutableStateFlow("")

    private val _storedCities = MutableSharedFlow<List<City>>()
    val storedCities = _storedCities.asSharedFlow()

    fun fetchCities() = querySharedFlow.debounce(300).distinctUntilChanged().flatMapLatest {
        repository.fetchCities(it)
    }.cachedIn(viewModelScope)

    fun submitQuery(query: String) {
        querySharedFlow.value = query
    }

    fun fetchCachedCities() {
        viewModelScope.launch(dispatchers.default()) {
            repository.fetchCachedCities().collect { cities ->
                _storedCities.emit(cities)
            }
        }
    }

}