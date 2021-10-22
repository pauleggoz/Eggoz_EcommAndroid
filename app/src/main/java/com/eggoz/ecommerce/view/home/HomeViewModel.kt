package com.eggoz.ecommerce.view.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.HomeSlider
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.network.model.Sublist
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel:ViewModel() {

    var responProduct: MutableLiveData<Products> = MutableLiveData()
    var responSublist: MutableLiveData<Sublist> = MutableLiveData()
    var responHomeSlider: MutableLiveData<HomeSlider> = MutableLiveData()

    fun productList(city: Int,is_available:Int)  {
        viewModelScope.launch {
            Retrofithit().productList(city = city,is_available = is_available)
                .catch { e ->

                    var errorResponse: Products?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Products>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    Log.d("data",responProduct.toString())

                    responProduct.value=errorResponse
                }.collect { response ->
                    responProduct.value = response
                }
        }
    }

    fun getSubList(userid: Int,context:Context)  {
        viewModelScope.launch {
            Retrofithit().getSubList(userid = userid,context=context)
                .catch { e ->

                    var errorResponse: Sublist?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Sublist>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    Log.d("data",responSublist.toString())

                    responSublist.value=errorResponse
                }.collect { response ->
                    responSublist.value = response
                }
        }
    }

    fun homeSlider(context: Context)  {
        viewModelScope.launch {
            Retrofithit().homeSlider(context = context)
                .catch { e ->

                    var errorResponse: HomeSlider?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<HomeSlider>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responHomeSlider.value=errorResponse
                }.collect { response ->
                    responHomeSlider.value = response
                }
        }
    }
}