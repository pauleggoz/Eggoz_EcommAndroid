package com.eggoz.ecommerce.view.order.viewmodel

import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderListRepository(private var userPreferences: UserPreferences) {


    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val auth_token: Flow<String?> by lazy { userPreferences.authtoken }

    fun orderhistory(customer: Int, token: String): Flow<Orderhistory?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .orderHistory(customer = customer)
        emit(response)
    }.flowOn(Dispatchers.IO)
}