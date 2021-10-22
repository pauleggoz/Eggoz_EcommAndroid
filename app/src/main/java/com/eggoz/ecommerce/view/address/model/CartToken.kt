package com.eggoz.ecommerce.view.address.model

import com.eggoz.ecommerce.network.model.Errors
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class CartToken(
    @SerializedName("gateway_response")
    @Expose
     val gatewayResponse: GatewayResponse? = null,

    @SerializedName("payload")
    @Expose
     val payload: Payload? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {

    class GatewayResponse(
        @SerializedName("status")
        @Expose
         val status: String? = null,

        @SerializedName("message")
        @Expose
         val message: String? = null,

        @SerializedName("cftoken")
        @Expose
         val cftoken: String? = null
    )

    class Payload(
        @SerializedName("appId")
        @Expose
         val appId: String? = null,

        @SerializedName("orderId")
        @Expose
         val orderId: String? = null,

        @SerializedName("orderAmount")
        @Expose
         val orderAmount: Double? = null,

        @SerializedName("orderCurrency")
        @Expose
         val orderCurrency: String? = null,

        @SerializedName("orderNote")
        @Expose
         val orderNote: String? = null,

        @SerializedName("customerEmail")
        @Expose
         val customerEmail: String? = null,

        @SerializedName("customerName")
        @Expose
         val customerName: String? = null,

        @SerializedName("customerPhone")
        @Expose
         val customerPhone: String? = null,

        @SerializedName("returnUrl")
        @Expose
         val returnUrl: String? = null,

        @SerializedName("notifyUrl")
        @Expose
         val notifyUrl: String? = null
    )

}
