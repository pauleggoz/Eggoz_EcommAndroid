package com.eggoz.ecommerce.view.address.viewmodel

import android.content.Context
import android.util.Log
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.room.CartDao
import com.eggoz.ecommerce.room.MyDatabase
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class AddressRepository(private var userPreferences: UserPreferences, myDatabase: MyDatabase) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val token: Flow<String?> by lazy { userPreferences.authtoken }

    var cartdao: CartDao = myDatabase.deatailcart


    fun userAddress(id: Int, token: String): Flow<Address> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token)
            .userAddress(id = id)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getcartToken(
        customer: Int,
        token: String,
        totalamount: Double,
        addressid: Int,
        cartlist: ArrayList<RoomCart>,
        date: String,
        pay_by_wallet: Boolean
    ): Flow<Response<CartToken>> = flow {
        val paramsstring: MutableMap<String, String> = HashMap()
        val paramsarray: MutableMap<String, JsonArray> = HashMap()
        val paramsdouble: MutableMap<String, Double> = HashMap()
        val paramsint: MutableMap<String, Int> = HashMap()
        val paramsbolean: MutableMap<String, Boolean> = HashMap()
        paramsstring["delivery_date"] = date
        paramsstring["discount_name"] = ""

        paramsint["customer"] = customer
        paramsint["shipping_address"] = addressid

        paramsbolean["pay_by_wallet"] = pay_by_wallet


        paramsdouble["amount"] = 0.0
        paramsdouble["discount_amount"] = 0.0
        paramsdouble["order_price_amount"] = totalamount

        val flockarray = JsonArray()

        for (i in 0 until cartlist.size) {
            val cart = JsonObject()
            cart.addProperty("product", cartlist[i].id)
            cart.addProperty("quantity", cartlist[i].quantaty)
            cart.addProperty("single_sku_mrp", cartlist[i].price)
            cart.addProperty("single_sku_rate", cartlist[i].price)

            flockarray.add(cart)

        }
        paramsarray["cart_products"] = flockarray

        Log.d("TAG", "getcartToken: ${cartlist.size}  ${flockarray.size()}")
        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).getcartToken(
            paramsstring = paramsstring,
            paramsdouble = paramsdouble,
            paramsarray = paramsarray,
            paramsint = paramsint,
            paramsbolean = paramsbolean
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getTokenforsingle(
        customer: Int,
        token: String,
        totalamount: Double,
        addressid: Int,
        item_id: Int,
        date: String,
        pay_by_wallet: Boolean
    ): Flow<Response<CartToken>> = flow {
        val paramsstring: MutableMap<String, String> = HashMap()
        val paramsarray: MutableMap<String, JsonArray> = HashMap()
        val paramsdouble: MutableMap<String, Double> = HashMap()
        val paramsint: MutableMap<String, Int> = HashMap()
        val paramsbolean: MutableMap<String, Boolean> = HashMap()
        paramsstring["delivery_date"] = date
        paramsstring["discount_name"] = ""

        paramsint["customer"] = customer
        paramsint["shipping_address"] = addressid

        paramsbolean["pay_by_wallet"] = pay_by_wallet


        paramsdouble["amount"] = 0.0
        paramsdouble["discount_amount"] = 0.0
        paramsdouble["order_price_amount"] = totalamount

        val flockarray = JsonArray()

        val cart = JsonObject()
        cart.addProperty("product", item_id)
        cart.addProperty("quantity", 1)
        cart.addProperty("single_sku_mrp", totalamount * 1.25)
        cart.addProperty("single_sku_rate", totalamount)

        flockarray.add(cart)

        paramsarray["cart_products"] = flockarray

        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).getcartToken(
            paramsstring = paramsstring,
            paramsdouble = paramsdouble,
            paramsarray = paramsarray,
            paramsint = paramsint,
            paramsbolean = paramsbolean
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getTokenforsubitem(
        customer: Int,
        token: String,
        totalamount: Double,
        addressid: Int,
        item_id: Int,
        date: String,
        pay_by_wallet: Boolean
    ): Flow<Response<CartToken>> = flow {


        val paramsstring: MutableMap<String, String> = HashMap()
        val paramsint: MutableMap<String, Int> = HashMap()
        val paramsbolean: MutableMap<String, Boolean> = HashMap()
        val paramsarrStrng: MutableMap<String, ArrayList<String>> = HashMap()
        val paramsarrInt: MutableMap<String, ArrayList<Int>> = HashMap()
        val paramsjobj: MutableMap<String, JsonObject> = HashMap()

        paramsint["customer"] = -1
        paramsint["order_price_amount"] = 0
        paramsint["shipping_address"] = -1
        paramsint["amount"] = -1

        paramsarrInt["days"] = ArrayList()

        paramsstring["start_date"] = ""
        paramsstring["expiry_date"] = ""

        paramsarrStrng["dates"] = ArrayList()

        paramsint["wallet"] = -1
        paramsint["subscription"] = -1
        paramsbolean["pay_by_wallet"] = false


        paramsstring["slot"] = ""

        val cart = JsonObject()
        cart.addProperty("product", -1)
        cart.addProperty("quantity", -1)
        cart.addProperty("single_sku_mrp", -1)
        cart.addProperty("single_sku_rate", -1)


        paramsjobj["cart_product"] = cart


        val response = RetrofitClient().retrofitApiSerInterceptor(token = token).getsubToken(
            paramsstring = paramsstring,
            paramsarrStrng = paramsarrStrng,
            paramsarrInt = paramsarrInt,
            paramsint = paramsint,
            paramsbolean = paramsbolean,
            paramsjobj = paramsjobj
        )
        emit(response)
    }.flowOn(Dispatchers.IO)
}