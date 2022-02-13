package com.eggoz.ecommerce.view.order.viewmodel

import androidx.lifecycle.LiveData
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

    init {
        viewModelScope.launch {
            user_id = repository.user_id.buffer().first() ?: -1
            authtoken = repository.auth_token.buffer().first() ?: ""
        }
    }

    fun orderhistory(customer: Int): LiveData<Orderhistory> {
        val responOrderhistory: MutableLiveData<Orderhistory> = MutableLiveData()
        viewModelScope.launch {
            repository.orderhistory(customer = customer, token = authtoken)
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
        return responOrderhistory
    }
}