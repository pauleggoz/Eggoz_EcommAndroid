package com.eggoz.ecommerce.view.contactus.viewmodel

import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.ContactusModel
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ContactusRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val token: Flow<String?> by lazy { userPreferences.authtoken }

    fun getContactUs(): Flow<ContactusModel> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getContactUs()
        emit(response)
    }.flowOn(Dispatchers.IO)

}