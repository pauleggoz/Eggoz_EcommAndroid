package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Checkout(
    @SerializedName("sign")
    @Expose
     val sign: String? = null,

    @SerializedName("signData")
    @Expose
     val signData: SignData? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {

    class SignData(
        @SerializedName("orderId")
        @Expose
         val orderId: String? = null,

        @SerializedName("orderAmount")
        @Expose
         val orderAmount: String? = null,

        @SerializedName("paymentMode")
        @Expose
         val paymentMode: String? = null,

        @SerializedName("referenceId")
        @Expose
         val referenceId: String? = null,

        @SerializedName("txStatus")
        @Expose
         val txStatus: String? = null,

        @SerializedName("txMsg")
        @Expose
         val txMsg: String? = null,

        @SerializedName("txTime")
        @Expose
         val txTime: String? = null,

        @SerializedName("type")
        @Expose
         val type: String? = null,

        @SerializedName("signature")
        @Expose
         val signature: String? = null,

        @SerializedName("orderStatus")
        @Expose
         val orderStatus: String? = null
    )

}
