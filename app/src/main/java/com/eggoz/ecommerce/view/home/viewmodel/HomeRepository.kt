package com.eggoz.ecommerce.view.home.viewmodel

import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.*
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val token: Flow<String?> by lazy { userPreferences.authtoken }

    fun orderList(token: String, user_id: Int, start_date: String, endDate: String): Flow<OrderList> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).orderList(customer = user_id,startDate=start_date, endDate = endDate)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun productList(city: Int, is_available: Int): Flow<Products> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.productList(
            city = city,
            is_available = is_available
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun homeSlider(token: String): Flow<HomeSlider> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).homeSlider()
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getSubList(userid: Int, token: String): Flow<Sublist> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).getSubList(
            userid
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getBlog(page: Int, token: String): Flow<Blogs> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).getBlog(
            page
        )
        emit(response)
    }.flowOn(Dispatchers.IO)
}