package com.eggoz.ecommerce.view.address.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class AddressViewModel(private val repository: AddressRepository) : ViewModel() {

    private var emptycart: List<RoomCart> = ArrayList()

    var cart2 = MutableStateFlow(emptycart)

    var responAddress: MutableLiveData<Address> = MutableLiveData()
    var responCartToken: MutableLiveData<Response<CartToken>> = MutableLiveData()
    var responTokenforsingle: MutableLiveData<Response<CartToken>> = MutableLiveData()
    private var city_id = -1
    private var user_id = -1
    private var customer_id = -1
    var payment_count = -1
    var token = ""

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
            token = repository.token.buffer().first() ?: ""
            customer_id = repository.customer_id.buffer().first() ?: -1
            userAddress()
        }
    }

    fun userAddress() {
        viewModelScope.launch {
            repository.userAddress(id = customer_id, token = token)
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

                    responAddress.value = errorResponse!!
                }.collect { response ->
                    responAddress.value = response
                }
        }
    }

    fun getCart2(): StateFlow<List<RoomCart>> {
        viewModelScope.launch {
            repository.cartdao.getAll2()?.collect {
                cart2.value = it
            }
        }
        return cart2
    }

    fun getcartToken(
        totalamount: Double,
        addressid: Int,
        cartlist: ArrayList<RoomCart>,
        date: String,
        pay_by_wallet: Boolean
    ) {
        viewModelScope.launch {
            repository.getcartToken(
                customer = user_id,
                token = token,
                totalamount = totalamount,
                addressid = addressid,
                cartlist = cartlist,
                date,
                pay_by_wallet
            ).catch { e ->

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

                    responCartToken.value = errorResponse!!
                }.collect { response ->
                    responCartToken.value = response
                }
        }
    }

    fun getTokenforsingle(
        totalamount: Double,
        addressid: Int,
        item_id: Int,
        date: String,
        pay_by_wallet: Boolean
    ) {
        viewModelScope.launch {
            repository.getTokenforsingle(
                customer = user_id,
                token = token,
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

                    responTokenforsingle.value = errorResponse!!
                }.collect { response ->
                    responTokenforsingle.value = response
                }
        }
    }


    fun getTokenforsubitem(
         totalamount: Double, addressid: Int, item_id: Int,
        start_date: String,
        expiry_date: String,
        quantity: Int,
        subitem: Int,
        days: ArrayList<Int>, date: String, pay_by_wallet: Boolean,
         amount: Double,
    ) {
        viewModelScope.launch {
            repository.getTokenforsubitem(
                customer = customer_id,
                token = token,
                order_price_amount = totalamount,
                addressid = addressid,
                item_id = item_id,
                date,
                pay_by_wallet,
                amount =amount,
                start_date =start_date,
                expiry_date =expiry_date,
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

                    responTokenforsingle.value = errorResponse!!
                }.collect { response ->
                    responTokenforsingle.value = response
                }
        }
    }
}