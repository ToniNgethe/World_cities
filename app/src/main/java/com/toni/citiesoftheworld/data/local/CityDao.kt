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

    @Query("SELECT * FROM City ")
    fun fetchAllCity(): PagingSource<Int, City>

    @Query("SELECT * FROM City ")
    suspend fun getAll(): List<City>

    @Query("DELETE FROM City")
    suspend fun deleteAll()
}