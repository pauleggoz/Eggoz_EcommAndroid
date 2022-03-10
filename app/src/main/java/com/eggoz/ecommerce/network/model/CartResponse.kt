package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartResponse(
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
        @SerializedName("description")
        val description: String?,
        @SerializedName("ecommerce_name")
        val ecommerceName: String?,
        @SerializedName("ecommerce_price")
        val ecommercePrice: String?,
        @SerializedName("ecommerce_selling_price")
        val ecommerceSellingPrice: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("product_image")
        val productImage: String?,
        @SerializedName("SKU_Count")
        val sKUCount: Int?
    )
}