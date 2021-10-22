package com.eggoz.ecommerce.view.cart.callback

interface CartListCallback {
fun updateCart(id:Int,qnt:Int,price:String)
fun deleteCart(id:Int)
fun CartSize() :Int
}