package com.toni.citiesoftheworld.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.toni.citiesoftheworld.domain.model.City

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<City>?)

    @Query("SELECT * FROM City WHERE name LIKE :query")
    fun fetchAllCityByQuery(query: String?): PagingSource<Int, City>

    @Query("SELECT * FROM City ")
    fun fetchAllCity(): PagingSource<Int, City>

    @Query("DELETE  FROM City WHERE name LIKE :query")
    suspend fun deleteByQuery(query: String?)

    @Query("DELETE FROM City")
    suspend fun deleteAll()
}