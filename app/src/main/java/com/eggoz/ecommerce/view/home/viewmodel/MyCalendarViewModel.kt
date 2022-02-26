package com.eggoz.ecommerce.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.model.OrderModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MyCalendarViewModel(val repository: MyCalendarRepository): ViewModel() {

    private var city_id = -1
    private var user_id = -1
    private var token = ""
    val ordermodel = ArrayList<OrderModel>()

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
            token = repository.token.buffer().first() ?: ""
        }
    }

    fun orderlist(start:String,end:String ) : LiveData<OrderList> {

        val responOrderList: MutableLiveData<OrderList> = MutableLiveData()
        viewModelScope.launch {
            repository.orderList(token = token,user_id=user_id,start,end)
                .catch { e ->

                    var errorResponse: OrderList?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<OrderList>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responOrderList.value=errorResponse!!
                }.collect { response ->
                    responOrderList.value = response
                }
        }
        return responOrderList
    }
}