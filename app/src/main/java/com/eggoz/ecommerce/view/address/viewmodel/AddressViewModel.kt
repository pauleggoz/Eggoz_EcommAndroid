package com.eggoz.ecommerce.view.address.viewmodel

import android.util.Log
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

//    private var emptycart: List<RoomCart> = ArrayList()

    var responAddress: MutableLiveData<Address> = MutableLiveData()
    var responCartToken: MutableLiveData<Response<CartToken>> = MutableLiveData()
    var responTokenforsingle: MutableLiveData<Response<CartToken>> = MutableLiveData()
    var responTokenforsub: MutableLiveData<Response<CartToken>> = MutableLiveData()
    private var city_id = -1
    private var user_id = -1
    var customer_id = -1
    var payment_count = -1
    var token = ""
    var isCart = false
    var item_id: Int = -1
    var walletId: Int = -1
    var subId: Int = -1

    var totalamount = 0.0
    var ordertype = ""
    var addressid = -1
    var selectedLoc = 0

    var start_date = ""
    var expiry_date = ""
    var quantity = ""
    var schduleTime = ""
    var subitem = "-1"
    var days: java.util.ArrayList<Int>? = java.util.ArrayList()
    var dates: java.util.ArrayList<String>? = java.util.ArrayList()

    var amount = 0.0
    var slot = ""
    var qnt = 0
    var addresses: List<Address.AAddress> = ArrayList()

    init {
        viewModelScope.launch {
            city_id = repository.city_id.buffer().first() ?: -1
            user_id = repository.user_id.buffer().first() ?: -1
            token = repository.token.buffer().first() ?: ""
            customer_id = repository.customer_id.buffer().first() ?: -1
            userAddress()
            wallet()
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

    fun getCart(): Flow<List<RoomCart>> {
        return repository.getCart()!!
    }

    fun CartClear() {
        viewModelScope.launch {
            repository.cartdao.clearCart()
        }
    }

    fun getcartToken(
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

                responTokenforsingle.value = errorResponse!!
            }.collect { response ->
                responTokenforsingle.value = response
            }
        }
    }


    fun getTokenforsubitem(
        pay_by_wallet: Boolean
    ) {
        viewModelScope.launch {
            repository.getTokenforsubitem(
                customer_id,
                token,
                amount,
                addressid,
                item_id,
                pay_by_wallet,
                "$start_date 00:00:00",
                "$expiry_date 00:00:00",
                days!!,
                dates!!,
                slot,
                qnt,
                walletId,
                subId
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

                responTokenforsub.value = errorResponse!!
            }.collect { response ->
                responTokenforsub.value = response
            }
        }
    }

    fun wallet() {
        viewModelScope.launch {

            repository.wallet(customer = user_id)
                .catch { e ->
                    Log.d("TAG", "wallet: " + e.message)
                }
                .collect { response ->
                    walletId = response.results?.get(0)?.id ?: -1
                }
        }
    }
}