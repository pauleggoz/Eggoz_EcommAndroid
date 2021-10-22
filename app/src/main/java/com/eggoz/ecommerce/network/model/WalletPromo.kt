package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletPromo(
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
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {
    class Result(
        @SerializedName("id")
        @Expose
        val id: Int? = null,
        @SerializedName("amount")
        @Expose
        val amount: String? = null,
        @SerializedName("promo")
        @Expose
        val promo: String? = null,
        @SerializedName("voucher_colour")
        @Expose
        val voucherColour: String? = null,
        @SerializedName("is_available")
        @Expose
        val isAvailable: Boolean? = null,
        @SerializedName("created_at")
        @Expose
        val createdAt: String? = null,
        @SerializedName("expired_at")
        @Expose
        val expiredAt: String? = null,
        @SerializedName("note")
        @Expose
        val note: String? = null
    )
}
