package com.eggoz.ecommerce.view.order.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Orderhistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OrderListViewModel(val repository: OrderListRepository): ViewModel() {

    private var user_id = -1
    private var authtoken = ""
    private var responOrderhistory: MutableLiveData<Orderhistory> = MutableLiveData()
    val order get() = responOrderhistory

    init {
        viewModelScope.launch {
            user_id = repository.user_id.buffer().first() ?: -1
            authtoken = repository.auth_token.buffer().first() ?: ""
            orderhistory()
        }
    }

    private fun orderhistory() {
        viewModelScope.launch {
            repository.orderhistory(customer = user_id, token = authtoken)
                .catch { e ->

                    var errorResponse: Orderhistory? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Orderhistory>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responOrderhistory.value = errorResponse!!
                }.collect { response ->
                    responOrderhistory.value = response
                }
        }
    }
}