package com.eggoz.ecommerce.view.referAndEarn.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Blogs
import com.eggoz.ecommerce.network.model.ReferAndEarn
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UpdateReferEarnViewModel(private val repository: UpdateReferEarnRepository): ViewModel() {

    private var city_id = -1
    private var user_id = -1
    var customer_id = -1
    var token = ""

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
            token = repository.token.buffer().first() ?: ""
            customer_id = repository.customer_id.buffer().first() ?: -1

            Log.d("UpdateReferEarnViewModel", "UpdateReferEarnViewModel: $token")

        }
    }

    fun  referAndEarn(name:String,email:String,ref_id:String) : LiveData<ReferAndEarn> {
        val responUpdateReferEarn: MutableLiveData<ReferAndEarn> = MutableLiveData()
        viewModelScope.launch {
            repository.referAndEarn(token, name, email, ref_id)
                .catch { e ->
                    var errorResponse: ReferAndEarn?=null
                    when(e){
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<ReferAndEarn>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    responUpdateReferEarn.value=errorResponse
                }.collect { response ->
                    responUpdateReferEarn.value = response
                }
        }
        return responUpdateReferEarn
    }
}