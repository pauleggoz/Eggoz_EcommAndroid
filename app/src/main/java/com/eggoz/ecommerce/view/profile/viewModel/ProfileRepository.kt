package com.eggoz.ecommerce.view.profile.viewModel

import android.content.Context
import android.util.Log
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.network.model.Otpgenerate
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val auth_token: Flow<String?> by lazy { userPreferences.authtoken }


    fun editUser(
        id: Int,
        token: String,
        name: String,
        email: String,
        phone_no: String
    ): Flow<Address> = flow {
        val body = JsonObject()
        body.addProperty("name", name)
        body.addProperty("email", email)
        body.addProperty("phone_no", phone_no)

        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .editUser(id = id, body = body)
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getLocality(id: Int): Flow<CityData.Result> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getLocality(id = id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getCity(): Flow<CityData> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getCity()
        Log.d("data", "city ${response.toString()}")
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun updateAddress(
        user_id: Int,
        address_name: String,
        phone: String,
        address_id: String,
        buildingAddress: String,
        StreetAddress: String,
        Landmark: String,
        City: Int,
        State: Int,
        Pincode: String,
        token: String
    ): Flow<Address> = flow {

        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).updateAddress(
            id = address_id.toInt(),
            address_name,
            phone,
            buildingAddress,
            StreetAddress,
            Landmark,
            City,
            State,
            Pincode
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun addAddress(
        user_id: Int,
        address_name: String,
        phone: String,
        address_id: String,
        buildingAddress: String,
        StreetAddress: String,
        Landmark: String,
        City: Int,
        State: Int,
        Pincode: String,
        token: String
    ): Flow<Address> = flow {

        val body = JsonObject()
        val structure = JsonObject()
        structure.addProperty("address_name", address_name)
        structure.addProperty("building_address", buildingAddress)
        structure.addProperty("street_address", StreetAddress)
        structure.addProperty("landmark", Landmark)
        structure.addProperty("ecommerce_sector", State)
        structure.addProperty("city", City)
        structure.addProperty("pinCode", Pincode)
        structure.addProperty("phone_no", phone)

        body.add("address", structure)

        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).addAddress(
            body = body
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun deleteAddress(id: Int, token: String): Flow<Otpgenerate> = flow {
        val body = JsonObject()
        body.addProperty("address", id)
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .deleteAddress(body = body)
        emit(response)
    }.flowOn(Dispatchers.IO)



    fun userAddress(id: Int, token: String): Flow<Address> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .userAddress(id = id)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun orderhistory(customer: Int, token: String): Flow<Orderhistory?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .orderHistory(customer = customer)
        emit(response)
    }.flowOn(Dispatchers.IO)




}