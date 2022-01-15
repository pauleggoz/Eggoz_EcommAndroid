package com.eggoz.ecommerce.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.network.model.Otpgenerate
import com.eggoz.ecommerce.network.model.Otpverify
import com.eggoz.ecommerce.network.model.TokenData
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    fun Login(mobile: String) :LiveData<Otpgenerate> {
        val responOtpgenerate: MutableLiveData<Otpgenerate> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().Login(mobile = mobile)
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

    fun validate(mobile: String, otp: String, loc_id: Int, city_id: Int) :LiveData<Otpverify> {
        val responOtpverify: MutableLiveData<Otpverify> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().validate(mobile = mobile, otp = otp, sector = loc_id, city = city_id)
                .catch { e ->
                    var errorResponse: Otpverify? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Otpverify>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    Log.d("TAG", "validate: $errorResponse")

                    responOtpverify.value = errorResponse!!
                }.collect { response ->
                    responOtpverify.value = response
                    Log.d("TAG", "validate: $response")
                }
        }
        return responOtpverify
    }

    fun getCity() : LiveData<CityData> {
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

                    responsecity.value = errorResponse
                }.collect { response ->
                    responsecity.value = response
                }
        }
        return responsecity
    }

    fun getLocality(id: Int) :LiveData<CityData.Result?> {
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




}