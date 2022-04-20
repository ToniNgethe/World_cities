package com.toni.citiesoftheworld.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.toni.citiesoftheworld.TestDispatchers
import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.local.CityDao
import com.toni.citiesoftheworld.data.services.CitiesService
import com.toni.citiesoftheworld.domain.model.City
import com.toni.citiesoftheworld.domain.repository.CitiesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CitiesRepositoryImplTest {

    lateinit var repository: CitiesRepository
    lateinit var cityDb: CitiesDb
    lateinit var apiService: CitiesService
    lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        cityDb = mockk()
        apiService = mockk()

        testDispatchers = TestDispatchers()
        repository = CitiesRepositoryImpl(cityDb, apiService, testDispatchers)
    }

    @Test
    fun fetchCachedCitiesReturnsCities() = runTest {
        val sampleData = listOf(
            City(
                name = "test_city", id = 1, latitude = null, longitude = null, countryName = null
            ), City(
                name = "test_city_2", id = 2, latitude = null, longitude = null, countryName = null
            )
        )
        coEvery { cityDb.cityDao().getAll() } returns sampleData

        repository.fetchCachedCities().test {
            Truth.assertThat(awaitItem()).containsAnyIn(sampleData)
            cancelAndConsumeRemainingEvents()
        }
    }
}