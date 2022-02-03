package com.eggoz.ecommerce.mainactivityviewmodel

import android.os.Bundle
import android.util.Log
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.model.Checkout
import com.eggoz.ecommerce.network.model.TokenData
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.room.CartDao
import com.eggoz.ecommerce.room.MyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepository(private var userPreferences: UserPreferences,private var database: MyDatabase) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val token: Flow<String?> by lazy { userPreferences.authtoken }

    val cartDao: CartDao by lazy { database.deatailcart }

    fun getuserPreferences() :UserPreferences{
        return userPreferences
    }

    fun conformPaymentWallet(token:String, bundle: Bundle): Flow<Checkout> = flow {
        val paramsstring: MutableMap<String, String> = HashMap()

        paramsstring["orderId"] = bundle.getString("orderId", "")
        paramsstring["orderAmount"] = bundle.getString("orderAmount", "")
        paramsstring["paymentMode"] = bundle.getString("paymentMode", "")
        paramsstring["referenceId"] = bundle.getString("referenceId", "")
        paramsstring["txStatus"] = bundle.getString("txStatus", "")
        paramsstring["txMsg"] = bundle.getString("txMsg", "")
        paramsstring["txTime"] = bundle.getString("txTime", "")
        paramsstring["type"] = bundle.getString("type", "")
        paramsstring["signature"] = bundle.getString("signature", "")
        paramsstring["orderStatus"] = "PAID"

        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .conformPaymentWallet(paramsstring)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun conformPaymentCart(token: String, bundle: Bundle): Flow<Checkout> = flow {
        val paramsstring: MutableMap<String, String> = HashMap()

        paramsstring["orderId"] = bundle.getString("orderId", "")
        paramsstring["orderAmount"] = bundle.getString("orderAmount", "")
        paramsstring["paymentMode"] = bundle.getString("paymentMode", "")
        paramsstring["referenceId"] = bundle.getString("referenceId", "")
        paramsstring["txStatus"] = bundle.getString("txStatus", "")
        paramsstring["txMsg"] = bundle.getString("txMsg", "")
        paramsstring["txTime"] = bundle.getString("txTime", "")
        paramsstring["type"] = bundle.getString("type", "")
        paramsstring["signature"] = bundle.getString("signature", "")
        paramsstring["orderStatus"] = "PAID"

        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .conformPaymentCart(paramsstring)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun userAddress(id: Int, token: String): Flow<Address> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .userAddress(id = id)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun refreshToken(token: String): Flow<TokenData> = flow {
        val response =
            RetrofitClient().retrofitApiSerwithoutInterceptor.refreshToken(token)
        emit(response)
    }.flowOn(Dispatchers.IO)
}