package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Products(
    @SerializedName("count")
    @Expose
    var countval: Int? = null,
    @SerializedName("next")
    @Expose
    var next: Any? = null,
    @SerializedName("previous")
    @Expose
    var previous: Any? = null,
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null,
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

        @SerializedName("updated_at")
        @Expose
        val updatedAt: String? = null,

        @SerializedName("productDescriptions")
        @Expose
        val productDescriptions: List<ProductDescription>? = null,

        @SerializedName("productBenefits")
        @Expose
        val productBenefits: List<Any>? = null,

        @SerializedName("productInlines")
        @Expose
        val productInlines: List<ProductInline>? = null,

        @SerializedName("productLongDescriptions")
        @Expose
        val productLongDescriptions: List<ProductLongDescription>? = null,

        @SerializedName("productSpecifications")
        @Expose
        val productSpecifications: List<ProductSpecification>? = null,

        @SerializedName("productInformations")
        @Expose
        val productInformations: List<ProductInformation>? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("description")
        @Expose
        val description: String? = null,

        @SerializedName("slug")
        @Expose
        val slug: String? = null,

        @SerializedName("is_available")
        @Expose
        val isAvailable: Boolean? = null,

        @SerializedName("is_stock_available_online")
        @Expose
        val isStockAvailableOnline: Boolean? = null,

        @SerializedName("is_prime_online")
        @Expose
        val isPrimeOnline: Boolean? = null,

        @SerializedName("is_new_online")
        @Expose
        val isNewOnline: Boolean? = null,

        @SerializedName("is_available_online")
        @Expose
        val isAvailableOnline: Boolean? = null,

        @SerializedName("product_image")
        @Expose
        val productImage: String? = null,

        @SerializedName("SKU_Count")
        @Expose
        val sKUCount: Int? = null,

        @SerializedName("currency")
        @Expose
        val currency: String? = null,

        @SerializedName("oms_order")
        @Expose
        val omsOrder: Int? = null,

        @SerializedName("ecomm_order")
        @Expose
        val ecommOrder: Int? = null,

        @SerializedName("current_price")
        @Expose
        val currentPrice: String? = null,

        @SerializedName("ecommerce_price")
        @Expose
        val ecommercePrice: String? = null,

        @SerializedName("shelf_life")
        @Expose
        val shelfLife: String? = null,

        @SerializedName("sku_weight")
        @Expose
        val skuWeight: String? = null,

        @SerializedName("weight_type")
        @Expose
        val weightType: String? = null,

        @SerializedName("charge_taxes")
        @Expose
        val chargeTaxes: Boolean? = null,

        @SerializedName("city")
        @Expose
        val city: Int? = null,

        @SerializedName("productDivision")
        @Expose
        val productDivision: Int? = null,

        @SerializedName("productSubDivision")
        @Expose
        val productSubDivision: Int? = null,
        @SerializedName("errors")
        @Expose
        var errors: List<Error>? = null,
        @SerializedName("error_type")
        @Expose
        var errorType: String? = null
    ) {
        class ProductDescription(
            @SerializedName("id")
            @Expose
            val id: Int? = null,

            @SerializedName("description")
            @Expose
            val description: String? = null,

            @SerializedName("product")
            @Expose
            val product: Int? = null
        )


        class ProductSpecification(

            @SerializedName("id")
            @Expose
            val id: Int? = null,

            @SerializedName("specification")
            @Expose
            val specification: String? = null,

            @SerializedName("product")
            @Expose
            val product: Int? = null
        )


        class ProductInformation(
            @SerializedName("id")
            @Expose
            val id: Int? = null,

            @SerializedName("productInformationLine")
            @Expose
            val productInformationLine: List<ProductInformationLine>? = null,

            @SerializedName("information")
            @Expose
            val information: String? = null,

            @SerializedName("product")
            @Expose
            val product: Int? = null
        ) {
            class ProductInformationLine(
                @SerializedName("id")
                @Expose
                val id: Int? = null,

                @SerializedName("name")
                @Expose
                val name: String? = null,

                @SerializedName("info_value")
                @Expose
                val infoValue: String? = null,

                @SerializedName("type")
                @Expose
                val type: String? = null,

                @SerializedName("information")
                @Expose
                val information: Int? = null
            )
        }


        class ProductInline(
            @SerializedName("name")
            @Expose
            val name: String? = null,

            @SerializedName("baseProduct")
            @Expose
            val baseProduct: Int? = null,

            @SerializedName("baseProduct_name")
            @Expose
            val baseProductName: String? = null,

            @SerializedName("quantity")
            @Expose
            val quantity: Int? = null
        )


        class ProductLongDescription(
            @SerializedName("id")
            @Expose
            val id: Int? = null,

            @SerializedName("description")
            @Expose
            val description: String? = null,

            @SerializedName("product")
            @Expose
            val product: Int? = null
        )
    }

}
