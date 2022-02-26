package com.eggoz.ecommerce.view.home.viewmodel

import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DetailDateRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val token: Flow<String?> by lazy { userPreferences.authtoken }

    fun orderList(token: String, user_id: Int, start_date: String, endDate: String): Flow<OrderList> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).orderList(customer = user_id,startDate=start_date, endDate = endDate)
        emit(response)
    }.flowOn(Dispatchers.IO)

}