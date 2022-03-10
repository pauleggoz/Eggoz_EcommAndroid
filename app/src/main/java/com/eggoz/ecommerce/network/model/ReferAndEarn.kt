package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

data class ReferAndEarn(
    @SerializedName("customer_email")
    val customerEmail: String?,
    @SerializedName("customer_name")
    val customerName: String?,
    @SerializedName("referral_data")
    val referralData: ReferralData?,
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {
    class ReferralData(
        @SerializedName("customer")
        val customer: Int?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("referral_code")
        val referralCode: String?,
        @SerializedName("referral_data")
        val referralData: List<ReferralDataX>?
    ) {
        class ReferralDataX(
            @SerializedName("desc")
            val desc: String?,
            @SerializedName("expiry_date")
            val expiryDate: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("start_date")
            val startDate: String?,
            @SerializedName("used_by")
            val usedBy: Int?
        )
    }
}