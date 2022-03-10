package com.eggoz.ecommerce.network.repository

import android.content.Context
import android.util.Log
import com.eggoz.ecommerce.network.model.*
import com.eggoz.ecommerce.view.membershipPlans.model.Membership
import com.eggoz.ecommerce.view.membershipPlans.model.MembershipRecharge
import com.eggoz.ecommerce.view.subscribe.model.Subscribe
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response

class Retrofithit {

    fun Login(mobile: String): Flow<Otpgenerate> = flow {
        val response =
            RetrofitClient().retrofitApiSerwithoutInterceptor.otpgenerate(mobile_no = mobile)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun validate(mobile: String, otp: String, sector: Int, city: Int): Flow<Otpverify> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.validate(
            mobile_no = mobile,
            otp = otp,
            ecommerce_sector = sector,
            city = city
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getCity(): Flow<CityData> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getCity()
        Log.d("data", "city ${response.toString()}")
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getLocality(id: Int): Flow<CityData.Result> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getLocality(id = id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun productList(city: Int, is_available: Int): Flow<Products> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.productList(
            city = city,
            is_available = is_available
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun productItembyid(id: Int): Flow<Products.Result> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.productItembyid(id = id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun membership(context: Context): Flow<Membership> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .membership()
        emit(response)
        Log.d("TAG", "membership: $response \n ")
    }.flowOn(Dispatchers.IO)

    fun membershiprecharge(
        start_date: String,
        expiry_date: String,
        customer: Int,
        amount: Double,
        wallet: String,
        membership: String,
        pay_by_wallet: Boolean,
        context: Context
    ): Flow<MembershipRecharge> = flow {

        val structure = JsonObject()
        structure.addProperty("start_date", start_date)
        structure.addProperty("expiry_date", expiry_date)
        structure.addProperty("customer", customer)
        structure.addProperty("amount", amount)
        structure.addProperty("wallet", wallet)
        structure.addProperty("membership", membership)
        structure.addProperty("pay_by_wallet", pay_by_wallet)

        val response =
            RetrofitClient().retrofitApiSerInterceptor(context = context).membershiprecharge(
                body = structure
            )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun subscriptions(
        start_date: String,
        expiry_date: String,
        quantity: Int,
        customer: Int,
        product: Int,
        slot: String,
        days: ArrayList<Int>,
        context: Context
    ): Flow<Response<ResponseBody>> = flow {

        val body = JsonArray()
        val structure = JsonObject()
        structure.addProperty("start_date", start_date)
        structure.addProperty("expiry_date", expiry_date)
        structure.addProperty("quantity", quantity)
        structure.addProperty("customer", customer)
        structure.addProperty("product", product)
        structure.addProperty("slot", slot)
        for (i in 0 until days.size) {
            body.add(days[i])
        }
        structure.add("days", body)

        val data = structure.toString()

        Log.d("TAG", "subscriptions: ${structure.toString()}")


        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).subscriptions(
            body = structure
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getSubscribe(context: Context): Flow<Subscribe> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .getSubscribe(is_visible = 1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getCartList(ids:String): Flow<CartResponse> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getCartList(ids)
        emit(response)
    }.flowOn(Dispatchers.IO)

}

