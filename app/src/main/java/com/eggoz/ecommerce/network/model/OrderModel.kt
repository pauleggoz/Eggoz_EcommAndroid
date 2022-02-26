package com.eggoz.ecommerce.network.model

data class OrderModel(val orderitem: OrderDetail.OrderLines.OrderItem,
        val orderdate : String,
        val orderid : Int,
        val orderstatus : String) {
}