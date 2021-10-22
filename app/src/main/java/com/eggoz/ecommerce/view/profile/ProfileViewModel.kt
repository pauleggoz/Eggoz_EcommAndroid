package com.eggoz.ecommerce.view.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.network.model.Otpgenerate
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel : ViewModel() {

    var responOrderhistory: MutableLiveData<Orderhistory> = MutableLiveData()
    var responUser: MutableLiveData<Address> = MutableLiveData()
    var responeditUser: MutableLiveData<Address> = MutableLiveData()
    var responOtpgenerate: MutableLiveData<Otpgenerate> = MutableLiveData()
    var responsecity: MutableLiveData<CityData> = MutableLiveData()
    var responselocality: MutableLiveData<CityData.Result?> = MutableLiveData()

    fun orderhistory(customer: Int, context: Context) {
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
                    responOrderhistory.value = errorResponse
                }.collect { response ->
                    responOrderhistory.value = response
                }
        }
    }

    fun user(customer: Int, context: Context) {
        viewModelScope.launch {
            Retrofithit().userAddress(id = customer, context = context)
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

                    responUser.value = errorResponse
                }.collect { response ->
                    responUser.value = response
                }
        }
    }
    fun deleteAddress(id: Int, context: Context) {
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
                    responOtpgenerate.value = errorResponse
                }.collect { response ->
                    responOtpgenerate.value = response
                }
        }
    }

    fun addAddress(
        user_id:Int,
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
    ) {
        viewModelScope.launch {
            Retrofithit().addAddress(
                user_id=user_id,
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

                    responUser.value = errorResponse
                }.collect { response ->
                    responUser.value = response
                }
        }
    }

    fun UpdateAddress(
        user_id:Int,
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
    ) {
        viewModelScope.launch {
            Retrofithit().updateAddress(
                user_id=user_id,
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

                    responUser.value = errorResponse
                }.collect { response ->
                    responUser.value = response
                }
        }
    }

    fun getCity(){
        viewModelScope.launch {
            Retrofithit().getCity()
                .catch {e ->
                    var errorResponse: CityData?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CityData>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responsecity.value=errorResponse
                }.collect { response ->
                    responsecity.value = response
                }
        }
    }

    fun getLocality(id: Int)  {
        viewModelScope.launch {
            Retrofithit().getLocality(id = id)
                .catch { e ->

                    var errorResponse: CityData.Result?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CityData.Result>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responselocality.value=errorResponse
                }.collect { response ->
                    responselocality.value = response
                }
        }
    }


    fun Edituser(customer: Int, context: Context,name:String,email:String,phone_no: String) {
        viewModelScope.launch {
            Retrofithit().editUser(id = customer, context = context,name = name, email = email,phone_no =  phone_no)
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
                    responeditUser.value = errorResponse
                }.collect { response ->
                    responeditUser.value = response
                }
        }
    }
}