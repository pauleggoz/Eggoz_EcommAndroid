package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Orderhistory(
    @SerializedName("from_date")
    @Expose
    val fromDate: Any? = null,

    @SerializedName("to_date")
    @Expose
    val toDate: String? = null,

    @SerializedName("results")
    @Expose
    val results: List<OrderDetail>? = null,
    @SerializedName("errors")
    @Expose
    val errors: List<Error>? = null,

    @SerializedName("error_type")
    @Expose
    val errorType: String? = null
) {


    class Error(
        @SerializedName("message")
        @Expose
        val message: String? = null
    )

}

