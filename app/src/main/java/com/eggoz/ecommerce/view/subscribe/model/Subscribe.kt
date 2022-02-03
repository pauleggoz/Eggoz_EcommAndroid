package com.eggoz.ecommerce.view.subscribe.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class Subscribe(
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
        @SerializedName("id")
        @Expose
        val id: Int? = null,

        @SerializedName("benefit_subscription")
        @Expose
        val benefitSubscription: List<BenefitSubscription>? = null,

        @SerializedName("extra_subscription")
        @Expose
        val extraSubscription: List<ExtraSubscription>? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("margin")
        @Expose
        val margin: Int? = null,

        @SerializedName("is_visible")
        @Expose
        val isVisible: Boolean? = null
    ) {


        class BenefitSubscription(
            @SerializedName("benefit")
            @Expose
            val benefit: String? = null,

            @SerializedName("is_visible")
            @Expose
            val isVisible: Boolean? = null
        )

        class ExtraSubscription(
            @SerializedName("extra")
            @Expose
            val extra: String? = null,

            @SerializedName("is_visible")
            @Expose
            val isVisible: Boolean? = null
        )
    }

}
