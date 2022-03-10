package com.eggoz.ecommerce.view.faq.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.UserProfileModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FaqViewModel(private val repository: FaqRepository): ViewModel() {

    var token = ""
    private var responFaq: MutableLiveData<UserProfileModel> = MutableLiveData()
    val faq get() = responFaq

    init {
        viewModelScope.launch {
            token = repository.authtoken.buffer().first() ?: ""
        }
    }

    fun faq(firstName:String,lastName:String,email:String,mobile:String,message:String){
        viewModelScope.launch {
            repository.faq(token = token,firstName,lastName,email,mobile,message)
                .catch { e ->

                    var errorResponse: UserProfileModel? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<UserProfileModel>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responFaq.value = errorResponse!!
                }.collect { response ->
                    responFaq.value = response
                }
        }
    }

}