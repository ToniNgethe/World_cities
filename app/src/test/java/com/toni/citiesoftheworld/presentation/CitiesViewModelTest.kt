package com.toni.citiesoftheworld.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.toni.citiesoftheworld.TestCoroutineRule
import com.toni.citiesoftheworld.TestDispatchers
import com.toni.citiesoftheworld.domain.model.City
import com.toni.citiesoftheworld.domain.repository.CitiesRepository
import com.toni.citiesoftheworld.noopListUpdateCallback
import com.toni.citiesoftheworld.presentation.adapter.CitiesAdapter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CitiesViewModelTest {

    private lateinit var testDispatchers: TestDispatchers

    lateinit var repository: CitiesRepository
    lateinit var citiesViewModel: CitiesViewModel

    @get:Rule
    val testDispatcherRule = TestCoroutineRule()

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()

        repository = mockk()
        citiesViewModel = CitiesViewModel(repository, TestDispatchers())
    }

    @Test
    fun fetchCitiesReturnsPagedListOfCities() = runTest {
        val sampleData = listOf(
            City(
                name = "test_city", id = 1, latitude = null, longitude = null, countryName = null
            ), City(
                name = "test_city_2", id = 2, latitude = null, longitude = null, countryName = null
            )
        )

        coEvery { repository.fetchCities("") } returns flowOf(
            PagingData.from(sampleData)
        )

        val differ = AsyncPagingDataDiffer(
            diffCallback = CitiesAdapter.CitiesDiffUtil(),
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testDispatchers.default(),
            workerDispatcher = testDispatchers.default(),
        )
        val job = launch {
            citiesViewModel.fetchCities().collectLatest {
                differ.submitData(it)
            }
        }
        advanceUntilIdle()

        Truth.assertThat(differ.snapshot().containsAll(sampleData))
        job.cancel()
    }

    @Test
    fun fetchCitiesReturnsListOfCachedCities() = runTest {
        val sampleData = listOf(
            City(
                name = "test_city", id = 1, latitude = null, longitude = null, countryName = null
            ), City(
                name = "test_city_2", id = 2, latitude = null, longitude = null, countryName = null
            )
        )
        coEvery { repository.fetchCachedCities() } returns flowOf(sampleData)

        val job = launch {
            citiesViewModel.storedCities.test {
                Truth.assertThat(awaitItem()).containsExactly(sampleData)
                cancelAndConsumeRemainingEvents()
            }
        }

        citiesViewModel.fetchCities()
        job.join()
        job.cancel()
    }
}

