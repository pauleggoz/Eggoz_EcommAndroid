package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Otpgenerate(
    @SerializedName("Result")
    @Expose
    var result: String? = null,
    @SerializedName("existing_user")
    @Expose
    var existingUser: Boolean? = null,
    @SerializedName("is_customer")
    @Expose
    var isCustomerval: Boolean? = null,
    @SerializedName("otp")
    @Expose
    var otpval: String? = null,
    @SerializedName("message")
    @Expose
    var message: String? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null,
    @SerializedName("success")
    @Expose
    var success: String? = null
)
