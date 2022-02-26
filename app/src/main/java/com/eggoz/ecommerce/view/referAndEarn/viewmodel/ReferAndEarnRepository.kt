package com.eggoz.ecommerce.view.referAndEarn.viewmodel

import android.util.Log
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.ReferAndEarn
import com.eggoz.ecommerce.network.model.Wallet
import com.eggoz.ecommerce.network.repository.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ReferAndEarnRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val authtoken: Flow<String?> by lazy { userPreferences.authtoken }


    fun referAndEarn(token:String): Flow<ReferAndEarn> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).referAndEarn()
        emit(response)
    }.flowOn(Dispatchers.IO)
}