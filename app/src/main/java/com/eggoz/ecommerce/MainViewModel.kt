package com.eggoz.ecommerce

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.model.Checkout
import com.eggoz.ecommerce.network.model.TokenData
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.room.CartDao
import com.eggoz.ecommerce.room.MyDatabase
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var responUser: MutableLiveData<Address> = MutableLiveData()
    var responCheckout: MutableLiveData<Checkout> = MutableLiveData()
    var responCheckoutWallet: MutableLiveData<Checkout> = MutableLiveData()
    var refreshToken: MutableLiveData<TokenData> = MutableLiveData()

    private var database: MyDatabase =
        Room.databaseBuilder(application, MyDatabase::class.java, Constants.DB_NAME)
            .allowMainThreadQueries().build()
    var cartdao: CartDao = database.deatailcart()
    private var emptycart: List<RoomCart> = ArrayList()

    fun user(customer: Int, context: Context) {
        viewModelScope.launch {
            Retrofithit().userAddress(id = customer, context = context)
                .catch { e ->

                    var errorResponse: Address? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Address>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responUser.value = errorResponse
                }.collect { response ->
                    responUser.value = response
                }
        }
    }

    fun conformPaymentCart(context: Context, bundle: Bundle) {
        viewModelScope.launch {
            Retrofithit().conformPaymentCart(context = context, bundle)
                .catch { e ->

                    var errorResponse: Checkout? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Checkout>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responCheckout.value = errorResponse
                }.collect { response ->
                    responCheckout.value = response
                }
        }
    }

    fun conformPaymentWallet(context: Context, bundle: Bundle) {
        viewModelScope.launch {
            Retrofithit().conformPaymentWallet(context = context, bundle)
                .catch { e ->

                    var errorResponse: Checkout? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Checkout>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responCheckoutWallet.value = errorResponse
                }.collect { response ->
                    responCheckoutWallet.value = response
                }
        }
    }

    fun cartclear() {
        viewModelScope.launch {
            cartdao.clearCart()
        }
    }

    fun setRefreshToken(token: String) = viewModelScope.launch {
        Retrofithit().refreshToken(token)
            .catch { e ->
                Log.d("main", "getRefreshToken:${e.message} ")
            }.collect {
                Log.d("main", "setRefreshToken: ${it.token} ")
                refreshToken.value = it
            }
    }
}