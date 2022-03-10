package com.eggoz.ecommerce.view.contactus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.ContactusModel
import com.eggoz.ecommerce.network.model.HomeSlider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ContactUsViewModel(private val repository: ContactusRepository) : ViewModel() {

    private var _mutablecontact: MutableLiveData<ContactusModel> = MutableLiveData()
    val contact: LiveData<ContactusModel>
        get() = _mutablecontact
    var companyAddress: String? = null
    var companyMobile: String? = null

    init {
        getContactUs()
    }

    fun getContactUs() {
        viewModelScope.launch {
            repository.getContactUs().catch { e ->

                var errorResponse: ContactusModel? = null
                when (e) {
                    is HttpException -> {
                        val gson = Gson()
                        val type = object : TypeToken<ContactusModel>() {}.type
                        errorResponse = gson.fromJson(
                            e.response()?.errorBody()!!.charStream(), type
                        )
                    }
                }

                _mutablecontact.value = errorResponse!!
            }.collect { response ->
                _mutablecontact.value = response
            }
        }
    }
}