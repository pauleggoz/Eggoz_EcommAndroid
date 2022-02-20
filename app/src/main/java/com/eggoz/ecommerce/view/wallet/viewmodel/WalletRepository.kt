package com.eggoz.ecommerce.view.wallet.viewmodel

import android.content.Context
import android.util.Log
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.Wallet
import com.eggoz.ecommerce.network.model.WalletPromo
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.view.address.model.CartToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WalletRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val authtoken: Flow<String?> by lazy { userPreferences.authtoken }


    fun wallet(customer: Int): Flow<Wallet> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.wallet(customer = customer)
        Log.d("data", "wallet ${response}")
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun walletPromo(token: String): Flow<WalletPromo?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .walletPromo()
        emit(response)
    }.flowOn(Dispatchers.IO)



    fun getWalletToken(
        token: String,
        walletid: Int,
        selectpromoid: Int,
        amount: Int
    ): Flow<CartToken?> = flow {
        val paramsint: MutableMap<String, Int> = HashMap()
        if (selectpromoid != -1)
            paramsint["voucher"] = selectpromoid
        paramsint["wallet"] = walletid
        paramsint["amount"] = amount
        Log.d("TAG", "getWalletToken: $paramsint")
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .walletToken(paramsint = paramsint)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getPaymentHash(
        token: String,
        hashData: String
    ): Flow<CartToken?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .paymentHash(hashData = hashData)
        emit(response)
    }.flowOn(Dispatchers.IO)
}