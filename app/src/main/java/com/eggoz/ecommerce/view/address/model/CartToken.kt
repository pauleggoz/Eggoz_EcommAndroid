package com.eggoz.ecommerce.view.address.model

import com.eggoz.ecommerce.network.model.Errors
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class CartToken(

    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null,
    @SerializedName("txnid")
    @Expose
    var txnid: String? = null,
    @SerializedName("token")
    @Expose
    var token: String? = null,
    @SerializedName("error")
    @Expose
    var error: Boolean? = null,
    @SerializedName("success")
    @Expose
    var success: Boolean? = null,
    @SerializedName("surl")
    @Expose
    var surl: String? = null,
    @SerializedName("furl")
    @Expose
    var furl: String? = null,
    @SerializedName("productinfo")
    @Expose
    var productinfo: String? = null,
    @SerializedName("firstname")
    @Expose
    var firstname: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("amount")
    @Expose
    var amount: String? = null,
    @SerializedName("merchant_key")
    @Expose
    var merchant_key: String? = null,
    @SerializedName("phone")
    @Expose
    var phone: String? = null,
    @SerializedName("hash")
    @Expose
    var hash: String? = null
)
