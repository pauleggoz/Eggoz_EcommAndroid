package com.eggoz.ecommerce.view.order.viewmodel

import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.OrderDetail
import com.eggoz.ecommerce.network.model.OrderEventModel
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderStatusRepository(private var userPreferences: UserPreferences, orderid:Int) {


    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val auth_token: Flow<String?> by lazy { userPreferences.authtoken }

    val orderId=orderid

    fun orderDetail(token:String): Flow<OrderDetail?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .orderDetail(id = orderId)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun orderEvent(token:String): Flow<OrderEventModel?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .orderEvent(id = orderId)
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun orderhistory(customer: Int, token: String): Flow<Orderhistory?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .orderHistory(customer = customer)
        emit(response)
    }.flowOn(Dispatchers.IO)
}