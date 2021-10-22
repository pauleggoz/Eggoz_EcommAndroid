package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class HomeSlider(
    @SerializedName("count")
    @Expose
    val count: Int? = null,

    @SerializedName("next")
    @Expose
    val next: Any? = null,

    @SerializedName("previous")
    @Expose
    val previous: Any? = null,

    @SerializedName("results")
    @Expose
    val results: List<Result>? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {


    class Result(
        @SerializedName("id")
        @Expose
        val id: Int? = null,

        @SerializedName("image_url")
        @Expose
        val imageUrl: String? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null
    )

}