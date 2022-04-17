package com.toni.citiesoftheworld.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [])
abstract class CitiesDb : RoomDatabase() {}