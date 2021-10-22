package com.eggoz.ecommerce.view.product.callback

interface ProductCallback {
    fun itemclick(
        id: Int,
        name: String,
        Image: String,
        Priceval: String,
        qnt: Int,
        des: String,
        status: Boolean,
        sKUCount: Int
    )

    fun updateCart(id: Int, qnt: Int, price: String)
    fun deleteCart(id: Int)
    fun incCart(id: Int)
    fun decCart(id: Int)
    fun qntCart(id: Int): Int
    fun updateqNT(id: Int, qnt: Int)
}