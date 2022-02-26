package com.eggoz.ecommerce.view.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.*
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
    private var token = ""
    var blogsPage = 1
    var blogsPageMax = 1
    val blogsresults: ArrayList<Blogs.Result> = ArrayList()

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
            token = repository.token.buffer().first() ?: ""
        }
    }

    fun productList():LiveData<Products>  {
        val responProduct: MutableLiveData<Products> = MutableLiveData()
        viewModelScope.launch {
            repository.productList(city = city_id,is_available = 1)
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

    fun getSubList() :LiveData<Sublist> {
        val responSublist: MutableLiveData<Sublist> = MutableLiveData()
        viewModelScope.launch {
            repository.getSubList(userid = user_id,token=token)
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

    fun homeSlider() :LiveData<HomeSlider> {

        val responHomeSlider: MutableLiveData<HomeSlider> = MutableLiveData()
        viewModelScope.launch {
            Log.d("data","token $token")
            repository.homeSlider(token = token)
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

    fun orderlist(start:String,end:String) :LiveData<OrderList> {

        Log.d("sk", "orderlist: $start $end")

        val responOrderList: MutableLiveData<OrderList> = MutableLiveData()
        viewModelScope.launch {
            repository.orderList(token = token,user_id=user_id,start,end)
                .catch { e ->

                    var errorResponse: OrderList?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<OrderList>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responOrderList.value=errorResponse!!
                }.collect { response ->
                    responOrderList.value = response
                }
        }
        return responOrderList
    }


    fun blogs() :LiveData<Blogs> {
        val responBloglist: MutableLiveData<Blogs> = MutableLiveData()
        viewModelScope.launch {
            repository.getBlog(blogsPage, token)
                .catch { e ->
                    var errorResponse: Blogs?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Blogs>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responBloglist.value=errorResponse
                }.collect { response ->
                    responBloglist.value = response
                }
        }
        return responBloglist
    }
}