package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Sublist(
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
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {

    class Result(
        @SerializedName("id")
        @Expose
        val id: Int? = null,

        @SerializedName("subscription")
        @Expose
        val subscription: Subscription? = null,

        @SerializedName("subscription_type")
        @Expose
        val subscriptionType: String? = null,

        @SerializedName("single_sku_mrp")
        @Expose
        val singleSkuMrp: String? = null,

        @SerializedName("single_sku_rate")
        @Expose
        val singleSkuRate: String? = null,

        @SerializedName("quantity")
        @Expose
        val quantity: Int? = null,

        @SerializedName("start_date")
        @Expose
        val startDate: String? = null,

        @SerializedName("expiry_date")
        @Expose
        val expiryDate: String? = null,

        @SerializedName("customer")
        @Expose
        val customer: Int? = null,

        @SerializedName("product")
        @Expose
        val product: Product? = null,

        @SerializedName("slot")
        @Expose
        val slot: Int? = null,

        @SerializedName("days")
        @Expose
        val days: List<Int>? = null
    ) {

        class Product(
            @SerializedName("id")
            @Expose
             val id: Int? = null,

            @SerializedName("SKU_Count")
            @Expose
             val sKUCount: Int? = null,

            @SerializedName("name")
            @Expose
             val name: String? = null,

            @SerializedName("description")
            @Expose
             val description: String? = null,

            @SerializedName("product_image")
            @Expose
             val productImage: String? = null,

            @SerializedName("ecommerce_price")
            @Expose
             val ecommercePrice: String? = null
        )

        class Subscription(
            @SerializedName("name")
            @Expose
            val name: String? = null,

            @SerializedName("margin")
            @Expose
            val margin: Int? = null
        )
    }

}