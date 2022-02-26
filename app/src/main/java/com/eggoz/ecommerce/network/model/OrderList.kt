package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderList(
    @SerializedName("results")
    val results: List<OrderDetail>? = null,
    @SerializedName("errors")
    @Expose
    val errors: List<Orderhistory.Error>? = null,

    @SerializedName("error_type")
    @Expose
    val errorType: String? = null

)