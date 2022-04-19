package com.toni.citiesoftheworld.data.services

import com.toni.citiesoftheworld.data.dto.CitiesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesService {
    @GET("city")
    suspend fun fetch(
        @Query("page") page: Int?,
        @Query("include") include: String = "country",
        @Query("filter[0][name][contains]") query: String? = null
    ) : CitiesDto
}