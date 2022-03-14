package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderEventModel(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: Any?,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val results: List<Result>?,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
){
    class Result(
        @SerializedName("date")
        val date: String?,
        @SerializedName("file_photo")
        val filePhoto: Any?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("order")
        val order: Int?,
        @SerializedName("parameters")
        val parameters: Parameters?,
        @SerializedName("remark")
        val remark: Any?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("user")
        val user: Int?
    ){
        class Parameters
    }
}