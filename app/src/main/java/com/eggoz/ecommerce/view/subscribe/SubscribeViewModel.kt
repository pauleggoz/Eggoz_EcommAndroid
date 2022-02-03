package com.eggoz.ecommerce.view.subscribe

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.view.subscribe.model.Subscribe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class SubscribeViewModel : ViewModel() {

    var responProduct: MutableLiveData<Products> = MutableLiveData()
    var responsemembershiprecharge: MutableLiveData<Response<ResponseBody>?> = MutableLiveData()
    var responseSubscribe: MutableLiveData<Subscribe?> = MutableLiveData()

    fun productList(city: Int, is_available: Int) {
        viewModelScope.launch {
            Retrofithit().productList(city = city, is_available = is_available)
                .catch { e ->

                    var errorResponse: Products? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Products>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responProduct.value = errorResponse
                }.collect { response ->
                    responProduct.value = response
                }
        }
    }


    fun subscriptions(
        start_date: String,
        expiry_date: String,
        customer: Int,
        quantity: Int,
        product: Int,
        slot: String,
        days: ArrayList<Int>,
        context: Context
    ) {
        viewModelScope.launch {
            Retrofithit().subscriptions(
                start_date = start_date,
                expiry_date = expiry_date,
                quantity = quantity,
                customer = customer,
                product = product,
                slot = slot,
                days = days,
                context = context
            ).catch { e ->

                    var errorResponse: Response<ResponseBody>? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseBody>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    //Log.d("main", "subscriptions: ${e.message} ")
                    responsemembershiprecharge.value = errorResponse
                }.collect { response ->
                    responsemembershiprecharge.value = response
                    Log.d("main", "subscriptions: ${response.body()} ")
                }
        }
    }

    fun getSubscribe(context: Context) {
        viewModelScope.launch {
            Retrofithit().getSubscribe(context = context)
                .catch { e ->

                    var errorResponse: Subscribe? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Subscribe>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    Log.d("TAG", "getMembership: $errorResponse")
                    responseSubscribe.value = errorResponse
                }.collect { response ->
                    responseSubscribe.value = response
                    Log.d("TAG", "getMembership: $response")
                }
        }
    }


}