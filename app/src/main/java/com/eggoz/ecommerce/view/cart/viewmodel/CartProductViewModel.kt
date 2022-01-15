package com.eggoz.ecommerce.view.cart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.room.RoomCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartProductViewModel(private val repository: ProductRepository) : ViewModel() {

    val cartList: Flow<List<RoomCart>>? by lazy { repository.cartList }
    var price: Double = 0.0

    fun deleteCart(item: RoomCart) {
        viewModelScope.launch {
            repository.deleteItem(item = item)
        }
    }

    fun cartSize(): LiveData<Int> {
        val cartSizeMut: MutableLiveData<Int> = MutableLiveData()
        viewModelScope.launch { cartSizeMut.postValue(repository.cartSize()) }
        return cartSizeMut
    }

    fun updateCart(item: RoomCart, qnt: Int) {
        viewModelScope.launch {
            repository.update(item = item, qnt = qnt)
        }
    }


}

