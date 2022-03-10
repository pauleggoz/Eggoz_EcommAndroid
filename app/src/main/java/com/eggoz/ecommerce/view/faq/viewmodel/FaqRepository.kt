package com.eggoz.ecommerce.view.faq.viewmodel

import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.model.ReferAndEarn2
import com.eggoz.ecommerce.network.model.UserProfileModel
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.view.address.model.CartToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FaqRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val authtoken: Flow<String?> by lazy { userPreferences.authtoken }


    fun faq(token:String,firstName:String,lastName:String,email:String,mobile:String,message:String): Flow<UserProfileModel> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).faq(email,firstName,lastName,mobile,message,"Faq Query",false)
        emit(response)
    }.flowOn(Dispatchers.IO)
}
