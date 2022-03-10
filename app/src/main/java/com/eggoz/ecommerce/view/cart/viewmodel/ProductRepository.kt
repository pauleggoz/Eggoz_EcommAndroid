package com.eggoz.ecommerce.view.cart.viewmodel

import android.app.Application
import com.eggoz.ecommerce.localdata.room.CartDao
import com.eggoz.ecommerce.localdata.room.RoomCart
import kotlinx.coroutines.flow.Flow

class ProductRepository(val cartdao: CartDao) {

    lateinit var application: Application

    //update
    suspend fun update(item: RoomCart, qnt: Int) {
        cartdao.updateCart(mid = item.id, qnt = qnt, price = item.price ?: "0")
    }

    //update
    suspend fun updatePrice(item_id: Int, price: String) {
        cartdao.updateCartPrice(mid = item_id, price = price)
    }

    //delete
    suspend fun deleteCart(id: Int) {
        cartdao.deletebyid(mid = id)
    }

    //cart size
    suspend fun cartSize():Int {
        return cartdao.Cartsize()
    }

    //cart size
    suspend fun getCart():Flow<List<RoomCart>>? {
        return cartdao.getAll2()
    }

    //cart size
    val cartList :Flow<List<RoomCart>>? by lazy { cartdao.getAll2() }

}