package com.eggoz.ecommerce.network.repository

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.eggoz.ecommerce.network.model.*
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.view.MembershipPlans.model.Membership
import com.eggoz.ecommerce.view.MembershipPlans.model.MembershipRecharge
import com.eggoz.ecommerce.view.Subscribe.model.Subscribe
import com.eggoz.ecommerce.view.address.model.CartToken
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody

class Retrofithit {

    fun Login(mobile: String): Flow<Otpgenerate> = flow {
        val response =
            RetrofitClient().retrofitApiSerwithoutInterceptor.otpgenerate(mobile_no = mobile)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun validate(mobile: String, otp: String, sector: Int, city: Int): Flow<Otpverify> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.validate(
            mobile_no = mobile,
            otp = otp,
            ecommerce_sector = sector,
            city = city
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getCity(): Flow<CityData> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getCity()
        Log.d("data", "city ${response.toString()}")
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getLocality(id: Int): Flow<CityData.Result> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.getLocality(id = id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun productList(city: Int, is_available: Int): Flow<Products> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.productList(
            city = city,
            is_available = is_available
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun productItembyid(id: Int): Flow<Products.Result> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.productItembyid(id = id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun wallet(customer: Int): Flow<Wallet> = flow {
        val response = RetrofitClient().retrofitApiSerwithoutInterceptor.wallet(customer = customer)
        Log.d("data", "wallet ${response}")
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun walletPromo(context: Context): Flow<WalletPromo?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .walletPromo()
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun orderhistory(customer: Int, context: Context): Flow<Orderhistory?> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .orderHistory(customer = customer)
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun userAddress(id: Int, context: Context): Flow<Address> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .userAddress(id = id)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getcartToken(
        customer: Int,
        context: Context,
        totalamount: Double,
        addressid: Int,
        cartlist: ArrayList<RoomCart>,
        date: String,
        pay_by_wallet: Boolean
    ): Flow<CartToken> = flow {
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
        paramsarray["cart_products"]= flockarray

        Log.d("TAG", "getcartToken: ${cartlist.size}  ${flockarray.size()}")
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).getcartToken(
            paramsstring = paramsstring,
            paramsdouble =paramsdouble ,
            paramsarray=paramsarray,
            paramsint=paramsint,
            paramsbolean=paramsbolean
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getTokenforsingle(
        customer: Int,
        context: Context,
        totalamount: Double,
        addressid: Int,
        item_id: Int,
        date: String,
        pay_by_wallet: Boolean
    ): Flow<CartToken> = flow {
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
            cart.addProperty("single_sku_mrp", totalamount*1.25)
            cart.addProperty("single_sku_rate", totalamount)

            flockarray.add(cart)

        paramsarray["cart_products"]= flockarray

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).getcartToken(
            paramsstring = paramsstring,
            paramsdouble =paramsdouble ,
            paramsarray=paramsarray,
            paramsint=paramsint,
            paramsbolean=paramsbolean
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun updateAddress(
        user_id: Int,
        address_name: String,
        phone: String,
        address_id: String,
        buildingAddress: String,
        StreetAddress: String,
        Landmark: String,
        City: Int,
        State: Int,
        Pincode: String,
        context: Context
    ): Flow<Address> = flow {

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).updateAddress(
            id = address_id.toInt(),
            address_name,
            phone,
            buildingAddress,
            StreetAddress,
            Landmark,
            City,
            State,
            Pincode
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun editUser(id: Int, context: Context, name: String, email: String, phone_no: String): Flow<Address> = flow {
        val body = JsonObject()
        body.addProperty("name", name)
        body.addProperty("email", email)
        body.addProperty("phone_no", phone_no)

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .editUser(id = id, body = body)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun deleteAddress(id: Int, context: Context): Flow<Otpgenerate> = flow {
        val body = JsonObject()
        body.addProperty("address", id)
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .deleteAddress(body = body)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun membership(context: Context): Flow<Membership> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .membership()
        emit(response)
        Log.d("TAG", "membership: $response \n ")
    }.flowOn(Dispatchers.IO)

    fun membershiprecharge(
        start_date: String,
        expiry_date: String,
        customer: Int,
        amount: Double,
        wallet: String,
        membership: String,
        pay_by_wallet: Boolean,
        context: Context
    ): Flow<MembershipRecharge> = flow {

        val structure = JsonObject()
        structure.addProperty("start_date", start_date)
        structure.addProperty("expiry_date", expiry_date)
        structure.addProperty("customer", customer)
        structure.addProperty("amount", amount)
        structure.addProperty("wallet", wallet)
        structure.addProperty("membership", membership)
        structure.addProperty("pay_by_wallet", pay_by_wallet)

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).membershiprecharge(
            body = structure
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun subscriptions(
        start_date: String,
        expiry_date: String,
        quantity: Int,
        customer: Int,
        product: Int,
        slot: String,
        days: ArrayList<Int>,
        context: Context
    ): Flow<ResponseBody> = flow {

        val body = JsonArray()
        val structure = JsonObject()
        structure.addProperty("start_date", start_date)
        structure.addProperty("expiry_date", expiry_date)
        structure.addProperty("quantity", quantity)
        structure.addProperty("customer", customer)
        structure.addProperty("product", product)
        structure.addProperty("slot", slot)
        for (i in 0 until days.size){
            body.add(days[i])
        }
        structure.add("days", body)

        val data=structure.toString()

        Log.d("TAG", "subscriptions: ${structure.toString()}")


        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).subscriptions(
            body = structure
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getSubscribe(context: Context): Flow<Subscribe> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .getSubscribe()
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun addAddress(
        user_id: Int,
        address_name: String,
        phone: String,
        address_id: String,
        buildingAddress: String,
        StreetAddress: String,
        Landmark: String,
        City: Int,
        State: Int,
        Pincode: String,
        context: Context
    ): Flow<Address> = flow {

        val body = JsonObject()
        val structure = JsonObject()
        structure.addProperty("address_name", address_name)
        structure.addProperty("building_address", buildingAddress)
        structure.addProperty("street_address", StreetAddress)
        structure.addProperty("landmark", Landmark)
        structure.addProperty("ecommerce_sector", State)
        structure.addProperty("city", City)
        structure.addProperty("pinCode", Pincode)
        structure.addProperty("phone_no", phone)

        body.add("address", structure)

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).addAddress(
            body = body
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getSubList(userid: Int,context:Context): Flow<Sublist> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).getSubList(
          userid
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getWalletToken(context: Context,walletid:Int,selectpromoid:Int,amount:Int): Flow<CartToken?> = flow {
        val paramsint: MutableMap<String, Int> = HashMap()
        if (selectpromoid!=-1)
        paramsint["voucher"] = selectpromoid
        paramsint["wallet"] = walletid
        paramsint["amount"] = amount
        Log.d("TAG", "getWalletToken: $paramsint")
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .walletToken(paramsint = paramsint)
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun getTokenforsubitem(
        customer: Int,
        context: Context,
        totalamount: Double,
        addressid: Int,
        item_id: Int,
        date: String,
        pay_by_wallet: Boolean
    ): Flow<CartToken> = flow {


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


        paramsjobj["cart_product"]=cart


        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).getsubToken(
            paramsstring = paramsstring,
            paramsarrStrng =paramsarrStrng ,
            paramsarrInt=paramsarrInt,
            paramsint=paramsint,
            paramsbolean=paramsbolean,
            paramsjobj=paramsjobj
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun homeSlider(context: Context): Flow<HomeSlider> = flow {
        val response = RetrofitClient().retrofitApiSerInterceptor(context = context).homeSlider()
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun conformPaymentCart( context: Context,bundle: Bundle): Flow<Checkout> = flow {
        val paramsstring: MutableMap<String, String> = HashMap()

        paramsstring["orderId"] =bundle.getString("orderId","")
        paramsstring["orderAmount"] = bundle.getString("orderAmount","")
        paramsstring["paymentMode"] = bundle.getString("paymentMode","")
        paramsstring["referenceId"] = bundle.getString("referenceId","")
        paramsstring["txStatus"] = bundle.getString("txStatus","")
        paramsstring["txMsg"] = bundle.getString("txMsg","")
        paramsstring["txTime"] = bundle.getString("txTime","")
        paramsstring["type"] = bundle.getString("type","")
        paramsstring["signature"] = bundle.getString("signature","")
        paramsstring["orderStatus"] = "PAID"

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .conformPaymentCart(paramsstring)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun conformPaymentWallet( context: Context,bundle: Bundle): Flow<Checkout> = flow {
        val paramsstring: MutableMap<String, String> = HashMap()

        paramsstring["orderId"] =bundle.getString("orderId","")
        paramsstring["orderAmount"] = bundle.getString("orderAmount","")
        paramsstring["paymentMode"] = bundle.getString("paymentMode","")
        paramsstring["referenceId"] = bundle.getString("referenceId","")
        paramsstring["txStatus"] = bundle.getString("txStatus","")
        paramsstring["txMsg"] = bundle.getString("txMsg","")
        paramsstring["txTime"] = bundle.getString("txTime","")
        paramsstring["type"] = bundle.getString("type","")
        paramsstring["signature"] = bundle.getString("signature","")
        paramsstring["orderStatus"] = "PAID"

        val response = RetrofitClient().retrofitApiSerInterceptor(context = context)
            .conformPaymentWallet(paramsstring)
        Log.d("data", response.toString())
        emit(response)
    }.flowOn(Dispatchers.IO)
}

