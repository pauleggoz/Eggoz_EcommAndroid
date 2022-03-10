package com.eggoz.ecommerce.view.profile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.databinding.FragmentProfileBinding
import com.eggoz.ecommerce.network.model.*
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
    val ordermodel = ArrayList<OrderModel>()

    private var user_id = -1
    private var customer_id = -1
    private var authtoken = ""


    private var responUser: MutableLiveData<Address> = MutableLiveData()
    val user get() = responUser
    private var responOrderhistory: MutableLiveData<Orderhistory> = MutableLiveData()
    val order get() = responOrderhistory

    init {
        viewModelScope.launch {
            user_id = repository.user_id.buffer().first() ?: -1
            authtoken = repository.auth_token.buffer().first() ?: ""
            customer_id = repository.customer_id.buffer().first() ?: -1
            user()
            orderhistory()
        }
    }


    private fun orderhistory(){
        viewModelScope.launch {
            repository.orderhistory(customer = user_id, token = authtoken)
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
    }

    private fun user() {
        viewModelScope.launch {
            repository.userAddress(id = customer_id, token = authtoken)
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
    }

    fun deleteAddress(id: Int) :LiveData<Otpgenerate>{
        val responOtpgenerate: MutableLiveData<Otpgenerate> = MutableLiveData()
        viewModelScope.launch {
            repository.deleteAddress(id = id, token = authtoken)
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

    suspend fun saveToken(token:String){
        repository.userPreferences!!.saveAuthtoke(token = token)
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
        Pincode: String
    ): LiveData<Address> {
        val responUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            repository.addAddress(
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
                token = authtoken
            ).catch { e ->

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
        Pincode: String
    ): LiveData<Address> {
        val responUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            repository.updateAddress(
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
                token = authtoken
            ).catch { e ->

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
            repository.getCity()
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
            repository.getLocality(id = id)
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
        name: String,
        email: String,
        phone_no: String
    ): LiveData<Address> {
        val responeditUser: MutableLiveData<Address> = MutableLiveData()
        viewModelScope.launch {
            repository.editUser(
                id = customer,
                token = authtoken,
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