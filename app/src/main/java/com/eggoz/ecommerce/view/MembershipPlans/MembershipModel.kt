package com.eggoz.ecommerce.view.MembershipPlans

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.view.MembershipPlans.model.Membership
import com.eggoz.ecommerce.view.MembershipPlans.model.MembershipRecharge
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException

class MembershipModel: ViewModel() {

    var responsemembership: MutableLiveData<Membership?> = MutableLiveData()
    var responsemembershiprecharge: MutableLiveData<MembershipRecharge?> = MutableLiveData()

    fun getMembership(context: Context) {
        viewModelScope.launch {
            Retrofithit().membership( context = context)
                .catch { e ->

                    var errorResponse: Membership ? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Membership>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }
                    Log.d("TAG", "getMembership: $errorResponse")
                    responsemembership.value = errorResponse
                }.collect { response ->
                    responsemembership.value = response
                    Log.d("TAG", "getMembership: $response")
                }
        }
    }





    fun membershiprecharge(
        start_date: String,
        expiry_date: String,
        customer: Int,
        amount: Double,
        wallet: String,
        membership: String,
        pay_by_wallet: Boolean,
        context: Context
    ) {
        viewModelScope.launch {
            Retrofithit().membershiprecharge(
                start_date =start_date,
                expiry_date =expiry_date,
                customer =customer,
                amount =amount,
                wallet =wallet,
                membership =membership,
                pay_by_wallet =pay_by_wallet,
                context = context
            )
                .catch { e ->

                    var errorResponse: MembershipRecharge? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<MembershipRecharge>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responsemembershiprecharge.value = errorResponse
                }.collect { response ->
                    responsemembershiprecharge.value = response
                }
        }
    }


}