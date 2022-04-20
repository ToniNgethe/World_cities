package com.toni.citiesoftheworld.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.toni.citiesoftheworld.domain.model.City
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityDaoTest {

    lateinit var citiesDb: CitiesDb
    lateinit var cityDao: CityDao

    @Before
    fun setUp() {
        citiesDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), CitiesDb::class.java
        ).allowMainThreadQueries().build()

        cityDao = citiesDb.cityDao()
    }

    @Test
    fun insertAllStoresListOfCities() = runTest {
        val sampleData = listOf(
            City(
                name = "test_city", id = 1, latitude = null, longitude = null, countryName = null
            ), City(
                name = "test_city_2", id = 2, latitude = null, longitude = null, countryName = null
            )
        )

        cityDao.insertAll(sampleData)

        val cities = cityDao.getAll()
        Truth.assertThat(cities).isEqualTo(sampleData)
    }

    @Test
    fun insertedDataIsDeleted() = runTest {
        val sampleData = listOf(
            City(
                name = "test_city", id = 1, latitude = null, longitude = null, countryName = null
            ), City(
                name = "test_city_2", id = 2, latitude = null, longitude = null, countryName = null
            )
        )

        cityDao.insertAll(sampleData)
        cityDao.deleteAll()
        val cities = cityDao.getAll()
        Truth.assertThat(cities).isEmpty()
    }
}