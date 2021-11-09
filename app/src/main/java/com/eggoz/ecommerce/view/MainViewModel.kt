package com.eggoz.ecommerce.view

import android.util.Log
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
    var responselocality: MutableLiveData<CityData.Result?> = MutableLiveData()
    var responsecity: MutableLiveData<CityData> = MutableLiveData()
    var responOtpgenerate: MutableLiveData<Otpgenerate> = MutableLiveData()
    var responOtpverify: MutableLiveData<Otpverify> = MutableLiveData()


    fun Login(mobile: String) {
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

                    responOtpgenerate.value = errorResponse
                }.collect { response ->
                    responOtpgenerate.value = response
                }
        }
    }

    fun validate(mobile: String, otp: String, loc_id: Int, city_id: Int) {
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

                    responOtpverify.value = errorResponse
                }.collect { response ->
                    responOtpverify.value = response
                    Log.d("TAG", "validate: $response")
                }
        }
    }

    fun getCity() {
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
    }

    fun getLocality(id: Int) {
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
    }




}