package com.eggoz.ecommerce.view.profile.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.*
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.view.home.viewmodel.HomeRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {


    var name = ""
    var email = ""
    var mobile = ""
    var isverifide = false
    var userid: Int = -1
    val ordermodel = ArrayList<OrderModel>()

    private var user_id = -1
    private var authtoken = ""

    init {
        viewModelScope.launch {
            user_id = repository.user_id.buffer().first() ?: -1
            authtoken = repository.auth_token.buffer().first() ?: ""
        }
    }


    fun orderhistory(customer: Int, context: Context): LiveData<Orderhistory> {
        val responOrderhistory: MutableLiveData<Orderhistory> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().orderhistory(customer = customer, context = context)
                .catch { e ->

                    var errorResponse: Orderhistory? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Orderhistory>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responOrderhistory.value = errorResponse!!
                }.collect { response ->
                    responOrderhistory.value = response
                }
        }
        return responOrderhistory
    }

    fun user( context: Context): LiveData<Address> {
        val responUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().userAddress(id = user_id, context = context)
                .catch { e ->

                    var errorResponse: Address? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Address>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responUser.value = errorResponse!!
                }.collect { response ->
                    responUser.value = response
                }
        }
        return responUser
    }

    fun deleteAddress(id: Int, context: Context) :LiveData<Otpgenerate>{
        val responOtpgenerate: MutableLiveData<Otpgenerate> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().deleteAddress(id = id, context = context)
                .catch { e ->
                    var errorResponse: Otpgenerate? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Otpgenerate>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responOtpgenerate.value = errorResponse!!
                }.collect { response ->
                    responOtpgenerate.value = response
                }
        }
        return responOtpgenerate
    }

    fun addAddress(
        user_id: Int,
        addressName: String,
        phone: String,
        address_id: String,
        BuildingAddress: String,
        StreetAddress: String,
        Landmark: String,
        City: Int,
        State: Int,
        Pincode: String,
        context: Context
    ): LiveData<Address> {
        val responUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().addAddress(
                user_id = user_id,
                address_name = addressName,
                phone = phone,
                address_id = address_id,
                buildingAddress = BuildingAddress,
                StreetAddress = StreetAddress,
                Landmark = Landmark,
                City = City,
                State = State,
                Pincode = Pincode,
                context = context
            )
                .catch { e ->

                    var errorResponse: Address? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Address>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responUser.value = errorResponse!!
                }.collect { response ->
                    responUser.value = response
                }
        }
        return responUser
    }

    fun UpdateAddress(
        user_id: Int,
        addressName: String,
        phone: String,
        address_id: String,
        BuildingAddress: String,
        StreetAddress: String,
        Landmark: String,
        City: Int,
        State: Int,
        Pincode: String,
        context: Context
    ): LiveData<Address> {
        val responUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().updateAddress(
                user_id = user_id,
                address_name = addressName,
                phone = phone,
                address_id = address_id,
                buildingAddress = BuildingAddress,
                StreetAddress = StreetAddress,
                Landmark = Landmark,
                City = City,
                State = State,
                Pincode = Pincode,
                context = context
            )
                .catch { e ->

                    var errorResponse: Address? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Address>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responUser.value = errorResponse!!
                }.collect { response ->
                    responUser.value = response
                }
        }
        return responUser
    }

    fun getCity() :LiveData<CityData>{
        val responsecity: MutableLiveData<CityData> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().getCity()
                .catch { e ->
                    var errorResponse: CityData? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CityData>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responsecity.value = errorResponse!!
                }.collect { response ->
                    responsecity.value = response
                }
        }
        return responsecity
    }

    fun getLocality(id: Int) : LiveData<CityData.Result?>{
        val responselocality: MutableLiveData<CityData.Result?> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().getLocality(id = id)
                .catch { e ->

                    var errorResponse: CityData.Result? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CityData.Result>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responselocality.value = errorResponse
                }.collect { response ->
                    responselocality.value = response
                }
        }
        return responselocality
    }


    fun Edituser(
        customer: Int,
        context: Context,
        name: String,
        email: String,
        phone_no: String
    ): LiveData<Address> {
        val responeditUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().editUser(
                id = customer,
                context = context,
                name = name,
                email = email,
                phone_no = phone_no
            )
                .catch { e ->

                    var errorResponse: Address? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Address>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responeditUser.value = errorResponse!!
                }.collect { response ->
                    responeditUser.value = response
                }
        }
        return responeditUser;
    }
}