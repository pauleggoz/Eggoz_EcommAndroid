package com.eggoz.ecommerce.view.referAndEarn.viewmodel

import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.ReferAndEarn
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UpdateReferEarnRepository(private var userPreferences: UserPreferences) {
    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val token: Flow<String?> by lazy { userPreferences.authtoken }

    val customer_id: Flow<Int?> by lazy { userPreferences.Customer_id }

    fun referAndEarn(token:String,name:String,email:String,ref_id:String): Flow<ReferAndEarn> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).updateReferal(ref_id,
        email,name)
        emit(response)
    }.flowOn(Dispatchers.IO)
}