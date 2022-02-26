package com.eggoz.ecommerce.view.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.localdata.room.CartDao
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductMoreViewModel(application: Application) : AndroidViewModel(application) {

    var cartdao: CartDao = MyDatabase.getInstance(context = application).deatailcart

    private var emptycart: List<RoomCart> = ArrayList()
    var cart2 = MutableStateFlow(emptycart)

    var responProduct: MutableLiveData<Products> = MutableLiveData()

    fun getCart2(): StateFlow<List<RoomCart>> {
        viewModelScope.launch {
            cartdao.getAll2()?.collect {
                cart2.value = it
            }
        }
        return cart2
    }

    fun productList(city: Int,is_available:Int)  {
        viewModelScope.launch {
            Retrofithit().productList(city = city,is_available = is_available)
                .catch { e ->

                    var errorResponse: Products?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Products>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responProduct.value=errorResponse
                }.collect { response ->
                    responProduct.value = response
                }
        }
    }

    suspend fun insertdata(cart: RoomCart) {
        cartdao.insertAll(cart)
    }
    suspend fun updateCart(id: Int, qnt: Int,price:String) {
        cartdao.updateCart(mid = id,qnt = qnt,price = price)
    }

    suspend fun CartSize():Int {
        return cartdao.Cartsize()
    }
    suspend fun deleteCart(id: Int) {
        cartdao.deletebyid(mid = id)
    }
    suspend fun qntCart(id: Int):Int? {
        return cartdao.qntCart(mid = id)
    }

    suspend fun incCart(id: Int):Int {
        return cartdao.incCart(mid = id)
    }
    suspend fun updateQnt(id: Int,qnt: Int) {
        return cartdao.updateQnt(mid = id,qnt = qnt)
    }
}