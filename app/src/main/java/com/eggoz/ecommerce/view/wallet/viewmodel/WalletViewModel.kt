package com.eggoz.ecommerce.view.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Wallet
import com.eggoz.ecommerce.network.model.WalletPromo
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WalletViewModel(private val repository: WalletRepository) : ViewModel() {
    private var cityId = -1
    private var userId = -1
    private var token = ""
    var walletBalanceval = 0.0
    var walletId = -1

    init {
        viewModelScope.launch {
            cityId = repository.city_id.buffer().first() ?: -1
            userId = repository.user_id.buffer().first() ?: -1
            token = repository.authtoken.buffer().first() ?: ""
        }
    }

    var responWallet: MutableLiveData<Wallet> = MutableLiveData()
    var responWalletPromo: MutableLiveData<WalletPromo> = MutableLiveData()
    var responCartToken: MutableLiveData<CartToken> = MutableLiveData()

    private var _totalbal: MutableLiveData<Double> = MutableLiveData()
    val totalbal: LiveData<Double>
        get() = _totalbal
    private var _promotnalbal: MutableLiveData<Double> = MutableLiveData()
    val promotnalbal: LiveData<Double>
        get() = _promotnalbal

    fun wallet() {
        viewModelScope.launch {

            repository.wallet(customer = userId)
                .catch { e ->
                    Log.d("TAG", "wallet: " + e.message)
                }
                .collect { response ->
                    responWallet.value=response
                        walletBalanceval = response.results!![0].totalBalance.toString().toDouble()
                        walletId = response.results[0].id ?: -1
                        _totalbal.value = response.results[0].totalBalance.toString().toDouble()
                        _promotnalbal.value = response.results[0].totalBalance.toString()
                            .toDouble() - response.results[0].rechargeBalance.toString().toDouble()
                }
        }
    }


    fun walletPromo() {
        viewModelScope.launch {
            repository.walletPromo(token = token)
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
                    responWalletPromo.value = errorResponse!!
                }.collect { response ->
                    responWalletPromo.value = response
                }
        }
    }

    fun getWalletToken( walletid: Int, selectpromoid: Int, amount: Int) {
        viewModelScope.launch {
            repository.getWalletToken(
                token = token,
                walletid,
                selectpromoid,
                amount = amount
            )
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
                    responCartToken.value = errorResponse!!
                }.collect { response ->
                    responCartToken.value = response
                }
        }
    }


}