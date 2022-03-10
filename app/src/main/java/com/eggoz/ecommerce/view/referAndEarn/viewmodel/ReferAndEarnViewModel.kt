package com.eggoz.ecommerce.view.referAndEarn.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.network.model.ReferAndEarn
import com.eggoz.ecommerce.network.model.ReferAndEarn2
import com.eggoz.ecommerce.network.model.WalletPromo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ReferAndEarnViewModel(private val repository: ReferAndEarnRepository): ViewModel() {


    var token = ""

    private var responReferAndEarn: MutableLiveData<ReferAndEarn2> = MutableLiveData()
    val referAndEarn get() = responReferAndEarn

    init {
        viewModelScope.launch {
            token = repository.authtoken.buffer().first() ?: ""
            referAndEarn()
        }
    }

    private fun referAndEarn() {
        viewModelScope.launch {
            repository.referAndEarn(token = token)
                .catch { e ->

                    var errorResponse: ReferAndEarn2? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<ReferAndEarn2>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responReferAndEarn.value = errorResponse!!
                }.collect { response ->
                    responReferAndEarn.value = response
                }
        }
    }
}