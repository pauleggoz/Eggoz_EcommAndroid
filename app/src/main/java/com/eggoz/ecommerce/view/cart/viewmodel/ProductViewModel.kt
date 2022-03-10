package com.eggoz.ecommerce.view.cart.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.localdata.room.CartDao
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    var cartdao: CartDao = MyDatabase.getInstance(context = application).deatailcart


    var responProduct: MutableLiveData<Products> = MutableLiveData()
    var responProductbyid: MutableLiveData<Products.Result> = MutableLiveData()
    private var userPreferences: UserPreferences? = null
    private var city_id = -1

    init {
        userPreferences = UserPreferences(application)
        viewModelScope.launch {
            city_id = userPreferences?.city?.buffer()?.first() ?: -1
        }
    }


    fun getCart2(): Flow<List<RoomCart>> {
        return cartdao.getAll2()!!
    }

    suspend fun insertdata(cart: RoomCart) {
        cartdao.insertAll(cart)
    }
    suspend fun updateCart(id: Int, qnt: Int,price:String) {
        cartdao.updateCart(mid = id,qnt = qnt,price = price)
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

    fun productList(is_available:Int)  {
        viewModelScope.launch {
            Retrofithit().productList(city = city_id,is_available = is_available)
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

                    responProduct.value=errorResponse!!
                }.collect { response ->
                    responProduct.value = response
                }
        }
    }

    fun productItembyid(id: Int)  {
        viewModelScope.launch {
            Retrofithit().productItembyid(id = id)
                .catch { e ->

                    var errorResponse: Products.Result?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Products.Result>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responProductbyid.value=errorResponse!!
                }.collect { response ->
                    responProductbyid.value = response
                }
        }
    }



}
