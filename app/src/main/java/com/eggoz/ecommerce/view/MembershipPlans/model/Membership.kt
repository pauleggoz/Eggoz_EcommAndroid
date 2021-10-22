package com.eggoz.ecommerce.view.MembershipPlans.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class Membership(
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
) {


    class Result(
        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("margin")
        @Expose
        val margin: Int? = null,

        @SerializedName("data_membership")
        @Expose
        val dataMembership: List<DataMembership>? = null,

        @SerializedName("benefit_membership")
        @Expose
        val benefitMembership: List<BenefitMembership>? = null
    ) {


        class DataMembership(
            @SerializedName("id")
            @Expose
            val id: Int? = null,

            @SerializedName("months")
            @Expose
            val months: Int? = null,

            @SerializedName("rate")
            @Expose
            val rate: String? = null
        )

        class BenefitMembership(
            @SerializedName("benefit")
            @Expose
            val benefit: String? = null,

            @SerializedName("is_visible")
            @Expose
            val isVisible: Boolean? = null
        )
    }

}
