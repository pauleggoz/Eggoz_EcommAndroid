package com.eggoz.ecommerce.view.home.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.HomeSlider
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.network.model.Sublist
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.view.cart.viewmodel.ProductRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val repository: HomeRepository) :ViewModel() {

    private var city_id = -1
    private var user_id = -1

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
        }
    }

    fun productList():LiveData<Products>  {
        val responProduct: MutableLiveData<Products> = MutableLiveData()
        viewModelScope.launch {
            Retrofithit().productList(city = city_id,is_available = 1)
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
        return responProduct
    }

    fun getSubList(userid: Int,context:Context) :LiveData<Sublist> {
        val responSublist: MutableLiveData<Sublist> = MutableLiveData()
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
                    responSublist.value=errorResponse
                }.collect { response ->
                    responSublist.value = response
                }
        }
        return responSublist
    }

    fun homeSlider(context: Context) :LiveData<HomeSlider> {

        val responHomeSlider: MutableLiveData<HomeSlider> = MutableLiveData()
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

                    responHomeSlider.value=errorResponse!!
                }.collect { response ->
                    responHomeSlider.value = response
                }
        }
        return responHomeSlider
    }
}