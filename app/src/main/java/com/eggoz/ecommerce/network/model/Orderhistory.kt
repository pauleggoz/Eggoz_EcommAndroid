package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Orderhistory(
    @SerializedName("from_date")
    @Expose
    val fromDate: Any? = null,

    @SerializedName("to_date")
    @Expose
    val toDate: String? = null,

    @SerializedName("results")
    @Expose
    val results: List<Result>? = null,
    @SerializedName("errors")
    @Expose
    val errors: List<Error>? = null,

    @SerializedName("error_type")
    @Expose
    val errorType: String? = null
) {


    class Error(
        @SerializedName("message")
        @Expose
        val message: String? = null
    )

    class Result(

        @SerializedName("id")
        @Expose
        val id: Int? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("orderId")
        @Expose
        val orderId: String? = null,

        @SerializedName("order_type")
        @Expose
        val orderType: String? = null,

        @SerializedName("retailer")
        @Expose
        val retailer: Any? = null,
        @SerializedName("customer")
        @Expose
        val customer: Int? = null,

        @SerializedName("retailerName")
        @Expose
        val retailerName: String? = null,

        @SerializedName("code")
        @Expose
        val code: String? = null,

        @SerializedName("salesPerson")
        @Expose
        val salesPerson: Any? = null,

        @SerializedName("salesPersonName")
        @Expose
        val salesPersonName: String? = null,

        @SerializedName("date")
        @Expose
        val date: String? = null,

        @SerializedName("distributor")
        @Expose
        val distributor: Any? = null,

        @SerializedName("distributorName")
        @Expose
        val distributorName: String? = null,

        @SerializedName("retailerSlab")
        @Expose
        val retailerSlab: Int? = null,

        @SerializedName("returned_bill")
        @Expose
        val returnedBill: Any? = null,

        @SerializedName("delivery_date")
        @Expose
        val deliveryDate: String? = null,

        @SerializedName("warehouse")
        @Expose
        val warehouse: Int? = null,

        @SerializedName("generation_date")
        @Expose
        val generationDate: String? = null,

        @SerializedName("retailer_note")
        @Expose
        val retailerNote: String? = null,

        @SerializedName("discount_name")
        @Expose
        val discountName: Any? = null,

        @SerializedName("discount_amount")
        @Expose
        val discountAmount: String? = null,

        @SerializedName("customerName")
        @Expose
        val customerName: String? = null,

        @SerializedName("deviated_amount")
        @Expose
        val deviatedAmount: String? = null,

        @SerializedName("order_price_amount")
        @Expose
        val orderPriceAmount: String? = null,

        @SerializedName("order_lines")
        @Expose
        val orderLines: OrderLines? = null,

        @SerializedName("status")
        @Expose
        val status: String? = null,

        @SerializedName("return_order_lines")
        @Expose
        val returnOrderLines: List<Any>? = null
    ) {


        class OrderLines(
            @SerializedName("total_items")
            @Expose
            val totalItems: Int? = null,

            @SerializedName("order_items")
            @Expose
            val orderItems: List<OrderItem>? = null,

            @SerializedName("products")
            @Expose
            val products: String? = null,

            @SerializedName("total_quantity")
            @Expose
            val totalQuantity: Int? = null
        ) {

            class OrderItem(
                @SerializedName("name")
                @Expose
                val name: String? = null,

                @SerializedName("sku")
                @Expose
                val sku: Int? = null,

                @SerializedName("price")
                @Expose
                val price: Double? = null,

                @SerializedName("quantity")
                @Expose
                val quantity: Int? = null,

                @SerializedName("order_line_id")
                @Expose
                val orderLineId: Int? = null,

                @SerializedName("product_id")
                @Expose
                val productId: Int? = null,

                @SerializedName("single_sku_rate")
                @Expose
                val singleSkuRate: Double? = null,

                @SerializedName("single_sku_mrp")
                @Expose
                val singleSkuMrp: Double? = null,

                @SerializedName("refund_quantity")
                @Expose
                val refundQuantity: Int? = null,

                @SerializedName("replace_quantity")
                @Expose
                val replaceQuantity: Int? = null,

                @SerializedName("product_inlines")
                @Expose
                val productInlines: List<ProductInline>? = null
            ) {

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
            }
        }
    }

}

/*

data class Orderhistory(
    @SerializedName("from_date")
    @Expose
    var fromDate: Any? = null,

    @SerializedName("to_date")
    @Expose
    var toDate: String? = null,

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
        var id: Int? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("orderId")
        @Expose
        var orderId: String? = null,

        @SerializedName("order_type")
        @Expose
        var orderType: String? = null,

        @SerializedName("retailer")
        @Expose
        var retailer: Any? = null,

        @SerializedName("customer")
        @Expose
        var customer: Int? = null,

        @SerializedName("retailerName")
        @Expose
        var retailerName: String? = null,

        @SerializedName("code")
        @Expose
        var code: String? = null,

        @SerializedName("salesPerson")
        @Expose
        var salesPerson: Any? = null,

        @SerializedName("salesPersonName")
        @Expose
        var salesPersonName: String? = null,

        @SerializedName("date")
        @Expose
        var date: String? = null,

        @SerializedName("distributor")
        @Expose
        var distributor: Any? = null,

        @SerializedName("distributorName")
        @Expose
        var distributorName: String? = null,

        @SerializedName("retailerSlab")
        @Expose
        var retailerSlab: Int? = null,

        @SerializedName("returned_bill")
        @Expose
        var returnedBill: Any? = null,

        @SerializedName("delivery_date")
        @Expose
        var deliveryDate: String? = null,

        @SerializedName("warehouse")
        @Expose
        var warehouse: Int? = null,

        @SerializedName("generation_date")
        @Expose
        var generationDate: String? = null,

        @SerializedName("retailer_note")
        @Expose
        var retailerNote: String? = null,

        @SerializedName("discount_name")
        @Expose
        var discountName: Any? = null,

        @SerializedName("discount_amount")
        @Expose
        var discountAmount: String? = null,

        @SerializedName("customerName")
        @Expose
        var customerName: String? = null,

        @SerializedName("deviated_amount")
        @Expose
        var deviatedAmount: String? = null,

        @SerializedName("order_price_amount")
        @Expose
        var orderPriceAmount: String? = null,

        @SerializedName("order_lines")
        @Expose
        var orderLines: OrderLines? = null,

        @SerializedName("status")
        @Expose
        var status: String? = null,

        @SerializedName("return_order_lines")
        @Expose
        var returnOrderLines: List<Any>? = null
    ) {


        class OrderLines(
            @SerializedName("total_items")
            @Expose
            var totalItems: Int? = null,

            @SerializedName("order_items")
            @Expose
            var orderItems: List<OrderItem>? = null,

            @SerializedName("products")
            @Expose
            var products: String? = null,

            @SerializedName("total_quantity")
            @Expose
            var totalQuantity: Int? = null
        ){

            class OrderItem(
                @SerializedName("name")
                @Expose
                var name: String? = null,

                @SerializedName("sku")
                @Expose
                var sku: Int? = null,

                @SerializedName("price")
                @Expose
                var price: Int? = null,

                @SerializedName("quantity")
                @Expose
                var quantity: Int? = null,

                @SerializedName("order_line_id")
                @Expose
                var orderLineId: Int? = null,

                @SerializedName("product_id")
                @Expose
                var productId: Int? = null,

                @SerializedName("single_sku_rate")
                @Expose
                var singleSkuRate: Int? = null,

                @SerializedName("single_sku_mrp")
                @Expose
                var singleSkuMrp: Int? = null,

                @SerializedName("refund_quantity")
                @Expose
                var refundQuantity: Int? = null,

                @SerializedName("replace_quantity")
                @Expose
                var replaceQuantity: Int? = null,

                @SerializedName("product_inlines")
                @Expose
                var productInlines: List<ProductInline>? = null
            ){

                class ProductInline(
                @SerializedName("name")
                @Expose
                 var name: String? = null,

                @SerializedName("baseProduct")
                @Expose
                 var baseProduct: Int? = null,

                @SerializedName("baseProduct_name")
                @Expose
                 var baseProductName: String? = null,

                @SerializedName("quantity")
                @Expose
                 var quantity: Int? = null
                )

            }
        }
    }


}
*/
