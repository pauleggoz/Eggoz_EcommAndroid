package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityData(
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null,
    @SerializedName("count")
    @Expose
    var count: Int? = null,
    @SerializedName("next")
    @Expose
    var next: Any? = null,
    @SerializedName("previous")
    @Expose
    var previousval: Any? = null,
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
) {
    class Result(
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        @SerializedName("zone_name")
        @Expose
        var zoneName: String? = null,
        @SerializedName("cities")
        @Expose
        var cities: List<City>? = null,
        @SerializedName("errors")
        @Expose
        var errors: List<Error>? = null,
        @SerializedName("error_type")
        @Expose
        var errorType: String? = null
    ) {
        class City(

            @SerializedName("id")
            @Expose
            var id: Int? = null,
            @SerializedName("city_name")
            @Expose
            var cityName: String? = null,
            @SerializedName("state")
            @Expose
            var state: String? = null,
            @SerializedName("country")
            @Expose
            var country: String? = null,
            @SerializedName("ecommerceSectors")
            @Expose
            var ecommerceSectors: List<EcommerceSector>? = null
        ) {
            class EcommerceSector(
                @SerializedName("id")
                @Expose
                var id: Int? = null,
                @SerializedName("sector_name")
                @Expose
                var sectorName: String? = null,
                @SerializedName("city")
                @Expose
                var city: Int? = null,
                @SerializedName("cluster")
                @Expose
                var cluster: Any? = null
            )
        }
    }
}
