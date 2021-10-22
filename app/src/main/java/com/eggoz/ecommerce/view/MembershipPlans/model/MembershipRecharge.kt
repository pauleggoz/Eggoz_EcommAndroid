package com.eggoz.ecommerce.view.MembershipPlans.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class MembershipRecharge(
    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("paymentLink")
    @Expose
    val paymentLink: String? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {

}
