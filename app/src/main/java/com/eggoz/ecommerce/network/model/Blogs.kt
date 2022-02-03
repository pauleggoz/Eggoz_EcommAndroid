package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.SerializedName

data class Blogs(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<Result>? = null
) {
    class Result(
        @SerializedName("alt_text")
        val altText: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("title")
        val title: String
    )
}