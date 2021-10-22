package com.eggoz.ecommerce.view.wallet

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Wallet
import com.eggoz.ecommerce.network.model.WalletPromo
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WalletViewModel:ViewModel() {
    var responWallet: MutableLiveData<Wallet> = MutableLiveData()
    var responWalletPromo: MutableLiveData<WalletPromo> = MutableLiveData()
    var responCartToken: MutableLiveData<CartToken> = MutableLiveData()

    fun wallet(customer: Int)  {
        viewModelScope.launch {
            Retrofithit().wallet(customer = customer)
                .catch {  e ->

                    var errorResponse: Wallet?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Wallet>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responWallet.value = errorResponse
                }.collect { response ->
                    responWallet.value = response
                }
        }
    }


    fun walletPromo(context: Context) {
        viewModelScope.launch {
            Retrofithit().walletPromo(context = context)
                .catch { e ->

                    var errorResponse: WalletPromo? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<WalletPromo>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responWalletPromo.value = errorResponse
                }.collect { response ->
                    responWalletPromo.value = response
                }
        }
    }

    fun getWalletToken(context: Context,walletid:Int,selectpromoid:Int,amount:Int) {
        viewModelScope.launch {
            Retrofithit().getWalletToken(context = context,walletid,selectpromoid,amount = amount)
                .catch { e ->

                    var errorResponse: CartToken? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CartToken>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responCartToken.value = errorResponse
                }.collect { response ->
                    responCartToken.value = response
                }
        }
    }



}