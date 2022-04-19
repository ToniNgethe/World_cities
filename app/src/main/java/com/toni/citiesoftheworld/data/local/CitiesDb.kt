package com.toni.citiesoftheworld.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.toni.citiesoftheworld.domain.model.City
import com.toni.citiesoftheworld.domain.model.RemoteKey

@Database(version = 1, entities = [City::class, RemoteKey::class])
abstract class CitiesDb : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}