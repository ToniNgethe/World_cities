package com.toni.citiesoftheworld.repositories.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.toni.citiesoftheworld.data.dto.CitiesDto
import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.local.CityDao
import com.toni.citiesoftheworld.data.local.RemoteKeyDao
import com.toni.citiesoftheworld.data.repositories.CitiesRemoteMediator
import com.toni.citiesoftheworld.data.services.CitiesService
import com.toni.citiesoftheworld.domain.model.City
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
class CitiesRemoteMediatorTest {

    lateinit var apiService: CitiesService
    lateinit var database: CitiesDb
    lateinit var cityDao: CityDao
    lateinit var remoteKeysDao: RemoteKeyDao
    lateinit var citiesRemoteMediator: CitiesRemoteMediator

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), CitiesDb::class.java
        ).allowMainThreadQueries().build()

        apiService = mockk()

        cityDao = database.cityDao()
        remoteKeysDao = database.remoteKeyDao()

        citiesRemoteMediator = CitiesRemoteMediator(database, " ", apiService)
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun givenNodDataPresentRefreshReturnsSuccess() = runTest {
        coEvery { apiService.fetch(any(), any(), any()) } returns CitiesDto(
            data = CitiesDto.CitiesData(
                items = listOf(), pagination = null
            ), time = null
        )

        val pagingState = PagingState<Int, City>(listOf(), null, PagingConfig(15), 15)

        val result = citiesRemoteMediator.load(LoadType.REFRESH, pagingState)

        Truth.assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
            .isTrue()
    }

    @Test
    fun wheAnExceptionIsEncounteredErrorIsReturned() = runTest {
        coEvery {
            apiService.fetch(
                any(), any(), any()
            )
        } throws Exception()
        val pagingState = PagingState<Int, City>(listOf(), null, PagingConfig(15), 15)

        val result = citiesRemoteMediator.load(LoadType.REFRESH, pagingState)

        Truth.assertThat(result is RemoteMediator.MediatorResult.Error).isTrue()
    }
}