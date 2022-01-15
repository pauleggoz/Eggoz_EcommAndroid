package com.eggoz.ecommerce.view.address

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.repository.Retrofithit
import com.eggoz.ecommerce.room.CartDao
import com.eggoz.ecommerce.room.MyDatabase
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class AddressViewModel(application: Application) : AndroidViewModel(application) {

    var cartdao: CartDao = MyDatabase.getInstance(context = application).deatailcart
    private var emptycart: List<RoomCart> = ArrayList()

    var cart2 = MutableStateFlow(emptycart)

    var responAddress: MutableLiveData<Address> = MutableLiveData()
    var responCartToken: MutableLiveData<Response<CartToken>> = MutableLiveData()
    var responTokenforsingle: MutableLiveData<Response<CartToken>> = MutableLiveData()


    fun userAddress(customer: Int, context: Context) {
        viewModelScope.launch {
            Retrofithit().userAddress(id = customer, context = context)
                .catch { e ->

                    var errorResponse: Address? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<Address>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responAddress.value = errorResponse
                }.collect { response ->
                    responAddress.value = response
                }
        }
    }

    fun getCart2(): StateFlow<List<RoomCart>> {
        viewModelScope.launch {
            cartdao.getAll2()?.collect {
                cart2.value = it
            }
        }
        return cart2
    }

    fun getcartToken(
        customer: Int,
        context: Context,
        totalamount: Double,
        addressid: Int,
        cartlist: ArrayList<RoomCart>,
        date: String,
        pay_by_wallet: Boolean
    ) {
        viewModelScope.launch {
            Retrofithit().getcartToken(
                customer = customer,
                context = context,
                totalamount = totalamount,
                addressid = addressid,
                cartlist = cartlist,
                date,
                pay_by_wallet
            )
                .catch { e ->

                    var errorResponse: Response<CartToken>? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CartToken>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responCartToken.value = errorResponse
                }.collect { response ->
                    responCartToken.value = response
                }
        }
    }

    fun getTokenforsingle(
        customer: Int,
        context: Context,
        totalamount: Double,
        addressid: Int,
        item_id: Int,
        date: String,
        pay_by_wallet: Boolean
    ) {
        viewModelScope.launch {
            Retrofithit().getTokenforsingle(
                customer = customer,
                context = context,
                totalamount = totalamount,
                addressid = addressid,
                item_id = item_id,
                date,
                pay_by_wallet
            )
                .catch { e ->

                    var errorResponse: Response<CartToken>? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CartToken>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responTokenforsingle.value = errorResponse
                }.collect { response ->
                    responTokenforsingle.value = response
                }
        }
    }


    fun getTokenforsubitem(
        customer: Int, context: Context, totalamount: Double, addressid: Int, item_id: Int,
        start_date: String,
        expiry_date: String,
        quantity: Int,
        subitem: Int,
        days: ArrayList<Int>, date: String, pay_by_wallet: Boolean
    ) {
        viewModelScope.launch {
            Retrofithit().getTokenforsubitem(
                customer = customer,
                context = context,
                totalamount = totalamount,
                addressid = addressid,
                item_id = item_id,
                date,
                pay_by_wallet
            )
                .catch { e ->

                    var errorResponse: Response<CartToken>? = null
                    when (e) {
                        is HttpException -> {
                            val gson = Gson()
                            val type = object : TypeToken<CartToken>() {}.type
                            errorResponse = gson.fromJson(
                                e.response()?.errorBody()!!.charStream(), type
                            )
                        }
                    }

                    responTokenforsingle.value = errorResponse
                }.collect { response ->
                    responTokenforsingle.value = response
                }
        }
    }
}