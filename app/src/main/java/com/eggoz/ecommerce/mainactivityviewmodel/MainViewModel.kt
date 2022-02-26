package com.eggoz.ecommerce.mainactivityviewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.model.Checkout
import com.eggoz.ecommerce.network.model.TokenData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    var responUser: MutableLiveData<Address> = MutableLiveData()
    var responCheckout: MutableLiveData<Checkout> = MutableLiveData()
    var responCheckoutWallet: MutableLiveData<Checkout> = MutableLiveData()
    var refreshToken: MutableLiveData<TokenData> = MutableLiveData()

    private var city_id = -1
    private var user_id = -1
    private var customer_id = -1
    private var token = ""

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
            token = repository.token.buffer().first() ?: ""
            customer_id = repository.customer_id.buffer().first() ?: -1
            if (customer_id != -1) {
                user()
            }
        }
    }

    fun user() {
        viewModelScope.launch {
            repository.userAddress(id = customer_id, token = token)
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

                    responUser.value = errorResponse!!
                }.collect { response ->
                    responUser.value = response
                }
        }
    }

    fun conformPaymentCart( bundle: Bundle) {
        viewModelScope.launch {
            repository.conformPaymentCart(token = token, bundle)
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

                    responCheckout.value = errorResponse!!
                }.collect { response ->
                    responCheckout.value = response
                }
        }
    }

    fun conformPaymentWallet( bundle: Bundle) {
        viewModelScope.launch {
            repository.conformPaymentWallet(token = token, bundle)
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

                    responCheckoutWallet.value = errorResponse!!
                }.collect { response ->
                    responCheckoutWallet.value = response
                }
        }
    }

    fun cartclear() {
        viewModelScope.launch {
            repository.cartDao.clearCart()
        }
    }

    fun upDateToken(token: String){
        viewModelScope.launch {
            repository.getuserPreferences()?.authtoken?.collect { response ->
                setRefreshToken(response!!)
            }
            repository.getuserPreferences().saveAuthtoke(token)
        }
    }

    fun userDataClear(){
        viewModelScope.launch {
            repository.getuserPreferences().clear()
        }
    }

    fun setRefreshToken(token: String) = viewModelScope.launch {
        repository.refreshToken(token)
            .catch { e ->
                Log.d("main", "getRefreshToken:${e.message} ")
            }.collect {
                Log.d("main", "setRefreshToken: ${it.token} ")
                refreshToken.value = it
            }
    }
}