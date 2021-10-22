package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Wallet(
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
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
){






    class Result(
        @SerializedName("id")
        @Expose
         val id: Int? = null,

        @SerializedName("customer")
        @Expose
         val customer: Customer? = null,

        @SerializedName("total_balance")
        @Expose
         val totalBalance: String? = null,

        @SerializedName("is_recharged_once")
        @Expose
         val isRechargedOnce: Boolean? = null,

        @SerializedName("recharge_balance")
        @Expose
         val rechargeBalance: String? = null,

        @SerializedName("note")
        @Expose
         val note: String? = null
    ){

        class Customer(
        @SerializedName("id")
        @Expose
         val id: Int? = null,

        @SerializedName("name")
        @Expose
         val name: String? = null,

        @SerializedName("code_string")
        @Expose
         val codeString: Any? = null,

        @SerializedName("code_int")
        @Expose
         val codeInt: Any? = null,

        @SerializedName("code")
        @Expose
         val code: String? = null,

        @SerializedName("billing_shipping_address_same")
        @Expose
         val billingShippingAddressSame: Boolean? = null,

        @SerializedName("onboarding_date")
        @Expose
         val onboardingDate: String? = null,

        @SerializedName("last_order_date")
        @Expose
         val lastOrderDate: Any? = null,

        @SerializedName("phone_no")
        @Expose
         val phoneNo: String? = null,

        @SerializedName("is_test_profile")
        @Expose
         val isTestProfile: Boolean? = null,

        @SerializedName("current_order_amount")
        @Expose
         val currentOrderAmount: String? = null,

        @SerializedName("user")
        @Expose
         val user: Int? = null,

        @SerializedName("shipping_address")
        @Expose
         val shippingAddress: Int? = null,

        @SerializedName("billing_address")
        @Expose
         val billingAddress: Int? = null)
    }

}
