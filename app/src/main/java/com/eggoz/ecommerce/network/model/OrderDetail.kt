package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

data class OrderDetail(
    @SerializedName("beat_assignment")
    val beatAssignment: Any?,
    @SerializedName("bill_no")
    val billNo: Any?,
    @SerializedName("billingAddress")
    val billingAddress: Any?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("customer")
    val customer: Int?,
    @SerializedName("customerAddress")
    val customerAddress: CustomerAddress?,
    @SerializedName("customerName")
    val customerName: String?,
    @SerializedName("customerPhone")
    val customerPhone: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("delivery_date")
    val deliveryDate: String?,
    @SerializedName("deviated_amount")
    val deviatedAmount: String?,
    @SerializedName("discount_amount")
    val discountAmount: Double?,
    @SerializedName("discount_name")
    val discountName: Any?,
    @SerializedName("distributor")
    val distributor: Int?,
    @SerializedName("distributorName")
    val distributorName: String?,
    @SerializedName("final_amount")
    val finalAmount: Double?,
    @SerializedName("financeName")
    val financeName: String?,
    @SerializedName("financePerson")
    val financePerson: Any?,
    @SerializedName("generation_date")
    val generationDate: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_buy_one_get_one")
    val isBuyOneGetOne: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("order_brand_type")
    val orderBrandType: String?,
    @SerializedName("orderId")
    val orderId: String?,
    @SerializedName("order_lines")
    val orderLines: OrderLines?,
    @SerializedName("order_price_amount")
    val orderPriceAmount: String?,
    @SerializedName("order_type")
    val orderType: String?,
    @SerializedName("po_no")
    val poNo: Any?,
    @SerializedName("retailer")
    val retailer: Any?,
    @SerializedName("retailerGSTIN")
    val retailerGSTIN: String?,
    @SerializedName("retailerName")
    val retailerName: String?,
    @SerializedName("retailer_note")
    val retailerNote: String?,
    @SerializedName("retailerSlab")
    val retailerSlab: Int?,
    @SerializedName("return_order_lines")
    val returnOrderLines: Any?,
    @SerializedName("returned_bill")
    val returnedBill: Any?,
    @SerializedName("salesPerson")
    val salesPerson: Any?,
    @SerializedName("salesPersonName")
    val salesPersonName: String?,
    @SerializedName("secondary_status")
    val secondaryStatus: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("warehouse")
    val warehouse: Int?
){
    class CustomerAddress(
        @SerializedName("address_name")
        val addressName: String?,
        @SerializedName("billing_city")
        val billingCity: Any?,
        @SerializedName("building_address")
        val buildingAddress: String?,
        @SerializedName("city")
        val city: City?,
        @SerializedName("date_added")
        val dateAdded: String?,
        @SerializedName("default_address_user")
        val defaultAddressUser: List<Int>?,
        @SerializedName("ecommerce_sector")
        val ecommerceSector: EcommerceSector?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("landmark")
        val landmark: String?,
        @SerializedName("latitude")
        val latitude: Any?,
        @SerializedName("longitude")
        val longitude: Any?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("phone_no")
        val phoneNo: String?,
        @SerializedName("pinCode")
        val pinCode: Int?,
        @SerializedName("street_address")
        val streetAddress: String?,
        @SerializedName("user_addresses_user")
        val userAddressesUser: List<Int>?
    ){
        class City(
            @SerializedName("city_name")
            val cityName: String?,
            @SerializedName("country")
            val country: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("state")
            val state: String?
        )
        class EcommerceSector(
            @SerializedName("city")
            val city: Int?,
            @SerializedName("cluster")
            val cluster: Any?,
            @SerializedName("distributor")
            val distributor: Int?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("sector_name")
            val sectorName: String?
        )
    }



    class OrderLines(
        @SerializedName("order_items")
        val orderItems: List<OrderItem>?,
        @SerializedName("products")
        val products: String?,
        @SerializedName("total_items")
        val totalItems: Int?,
        @SerializedName("total_quantity")
        val totalQuantity: Double?
    ){
        class OrderItem(
            @SerializedName("name")
            val name: String?,
            @SerializedName("product_image")
            val product_image: String?,
            @SerializedName("order_line_id")
            val orderLineId: Int?,
            @SerializedName("price")
            val price: Double?,
            @SerializedName("product_id")
            val productId: Int?,
            @SerializedName("product_inlines")
            val productInlines: List<ProductInline>?,
            @SerializedName("quantity")
            val quantity: Double?,
            @SerializedName("refund_quantity")
            val refundQuantity: Int?,
            @SerializedName("replace_quantity")
            val replaceQuantity: Int?,
            @SerializedName("single_sku_mrp")
            val singleSkuMrp: Double?,
            @SerializedName("single_sku_rate")
            val singleSkuRate: Double?,
            @SerializedName("sku")
            val sku: Int?
        ){
            class ProductInline(
                @SerializedName("baseProduct")
                val baseProduct: Int?,
                @SerializedName("baseProduct_name")
                val baseProductName: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("quantity")
                val quantity: Int?
            )
        }
    }
}