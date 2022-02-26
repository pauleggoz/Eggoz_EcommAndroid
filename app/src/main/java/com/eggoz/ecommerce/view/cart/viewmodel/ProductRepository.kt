package com.eggoz.ecommerce.view.cart.viewmodel

import com.eggoz.ecommerce.localdata.room.CartDao
import com.eggoz.ecommerce.localdata.room.RoomCart
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val cartdao: CartDao) {

    //update
    suspend fun update(item: RoomCart, qnt: Int) {
        cartdao.updateCart(mid = item.id, qnt = qnt, price = item.price ?: "0")
    }

    //delete
    suspend fun deleteItem(item: RoomCart) {
        cartdao.deletebyid(mid = item.id)
    }

    //cart size
    suspend fun cartSize():Int {
        return cartdao.Cartsize()
    }

    //cart size
    val cartList :Flow<List<RoomCart>>? by lazy { cartdao.getAll2() }

}