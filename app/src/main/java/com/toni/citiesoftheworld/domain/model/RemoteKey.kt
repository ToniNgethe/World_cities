package com.toni.citiesoftheworld.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val cityId: Int, val nextKey: Int?, val previousKey: Int?
)