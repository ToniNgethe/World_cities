package com.toni.citiesoftheworld.data.dto


import com.google.gson.annotations.SerializedName
import com.toni.citiesoftheworld.domain.model.City

data class CitiesDto(
    @SerializedName("data") var `data`: CitiesData?, @SerializedName("time") var time: Int?
) {
    data class CitiesData(
        @SerializedName("items") var items: List<Item?>?,
        @SerializedName("pagination") var pagination: Pagination?
    ) {
        data class Item(
            @SerializedName("country") var country: Country?,
            @SerializedName("country_id") var countryId: Int?,
            @SerializedName("created_at") var createdAt: String?,
            @SerializedName("id") var id: Int?,
            @SerializedName("lat") var lat: Double?,
            @SerializedName("lng") var lng: Double?,
            @SerializedName("local_name") var localName: String?,
            @SerializedName("name") var name: String?,
            @SerializedName("updated_at") var updatedAt: String?
        ) {
            data class Country(
                @SerializedName("code") var code: String?,
                @SerializedName("continent_id") var continentId: Int?,
                @SerializedName("created_at") var createdAt: String?,
                @SerializedName("id") var id: Int?,
                @SerializedName("name") var name: String?,
                @SerializedName("updated_at") var updatedAt: String?
            )

            fun toCity(): City = City(
                id = id!!, name = name, latitude = lat, longitude = lng, countryName = country?.name
            )
        }

        data class Pagination(
            @SerializedName("current_page") var currentPage: Int?,
            @SerializedName("last_page") var lastPage: Int?,
            @SerializedName("per_page") var perPage: Int?,
            @SerializedName("total") var total: Int?
        )
    }
}