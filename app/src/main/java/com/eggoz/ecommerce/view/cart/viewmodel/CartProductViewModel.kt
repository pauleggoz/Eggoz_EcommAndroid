package com.eggoz.ecommerce.view.cart.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.eggoz.ecommerce.localdata.room.CartDao
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.eggoz.ecommerce.network.model.CartResponse
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CartProductViewModel(private val repository: ProductRepository) : ViewModel() /*AndroidViewModel(repository.application)*/{
//    var cartdao: CartDao = MyDatabase.getInstance(context = repository.application).deatailcart

//    val cartList: Flow<List<RoomCart>>? by lazy { repository.cartList }
    var price: Double = 0.0
    var localCartListIds=""

    var localcartlist:List<RoomCart> =ArrayList()


    private var responseCart: MutableLiveData<CartResponse> = MutableLiveData()
    val apicart :LiveData<CartResponse>  get() = responseCart

    fun deleteCart(item: RoomCart) {
        viewModelScope.launch {
//        cartdao.deletebyid(mid = item.id)
            repository.deleteCart(id = item.id)
//            repository.cartdao.delete(item)
        }
    }
    suspend fun deleteCart(id: Int) {
        repository.cartdao.deletebyid(mid = id)
    }

    suspend fun updateQnt(id: Int,qnt: Int) {
        return repository.cartdao.updateQnt(mid = id,qnt = qnt)
    }

    suspend fun getCart(): Flow<List<RoomCart>> {
//        return cartdao.getAll2()!!
        return  repository.getCart()!!
//        return repository.cartdao.getAll2()!!
    }

    fun updateCart(item: RoomCart, qnt: Int) {
        viewModelScope.launch {
//            cartdao.updateQnt(mid = item.id, qnt = qnt)
        repository.update(item = item,qnt = qnt)
//        repository.cartdao.updateQnt(item.id,qnt = qnt)
        }
    }

    fun updateCartPrice(item_id: Int, price: String) {
        viewModelScope.launch {
//            cartdao.updateCartPrice(mid = item_id, price = price)
            repository.cartdao.updateCartPrice(mid = item_id, price = price)
//            repository.cartdao.updateCartPrice(item_id, price = price)
        }
    }


    fun getCartList(ids:String)  {
        Log.d("TAG", "getCartList: $ids")
        viewModelScope.launch {
            Retrofithit().getCartList(ids)
                .catch { e ->

                    var errorResponse: CartResponse?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CartResponse>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responseCart.value=errorResponse!!
                }.collect { response ->
                    responseCart.value = response
                }
        }
    }


}

