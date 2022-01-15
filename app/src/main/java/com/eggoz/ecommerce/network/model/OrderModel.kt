package com.eggoz.ecommerce.network.model

data class OrderModel(val orderitem: Orderhistory.Result.OrderLines.OrderItem,
        val orderdate : String,
        val orderid : String,
        val orderstatus : String) {
}