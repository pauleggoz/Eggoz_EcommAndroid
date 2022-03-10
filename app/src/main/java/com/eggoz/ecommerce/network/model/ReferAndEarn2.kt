package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReferAndEarn2(
    @SerializedName("customer_referral_code")
    val customerReferralCode: String?,
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
)
