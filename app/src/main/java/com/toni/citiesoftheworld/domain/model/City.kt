package com.toni.citiesoftheworld.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    var name: String?,
    @PrimaryKey var id: Int,
    var latitude: Double?,
    var longitude: Double?,
    var countryName: String?
)