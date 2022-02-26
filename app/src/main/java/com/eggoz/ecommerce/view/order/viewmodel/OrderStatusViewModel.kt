package com.eggoz.ecommerce.view.order.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.OrderDetail
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.model.Orderhistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OrderStatusViewModel(val repository:OrderStatusRepository): ViewModel() {

    private var user_id = -1
    public var order_id = -1
    private var authtoken = ""
    private var responOrderdetail: MutableLiveData<OrderDetail> = MutableLiveData()
    val order get() = responOrderdetail

    init {
        viewModelScope.launch {
            user_id = repository.user_id.buffer().first() ?: -1
            authtoken = repository.auth_token.buffer().first() ?: ""
            order_id = repository.orderId
            orderDetail()
        }
    }


    private fun orderDetail() {
        viewModelScope.launch {
            repository.orderDetail( token = authtoken)
                .catch { e ->

                    var errorResponse: OrderDetail? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<OrderDetail>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responOrderdetail.value = errorResponse!!
                }.collect { response ->
                    responOrderdetail.value = response
                }
        }
    }
}