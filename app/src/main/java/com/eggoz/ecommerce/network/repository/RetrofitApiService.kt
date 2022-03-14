package com.eggoz.ecommerce.network.repository

import com.eggoz.ecommerce.network.model.*
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.view.address.model.CartToken
import com.eggoz.ecommerce.view.membershipPlans.model.Membership
import com.eggoz.ecommerce.view.membershipPlans.model.MembershipRecharge
import com.eggoz.ecommerce.view.subscribe.model.Subscribe
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApiService {

    @FormUrlEncoded
    @POST(Constants.apiend_otpgenerate)
    suspend fun otpgenerate(
        @Field("phone_no") mobile_no: String
    ): Otpgenerate

    @FormUrlEncoded
    @POST(Constants.apiend_otpvalidate)
    suspend fun validate(
        @Field("phone_no") mobile_no: String,
        @Field("otp") otp: String,
        @Field("ecommerce_sector") ecommerce_sector: Int,
        @Field("city") city: Int
    ): Otpverify

    @GET(Constants.ecommerce_zone)
    suspend fun getCity(
    ): CityData

    @GET(Constants.ecommerce_zone_id)
    suspend fun getLocality(
        @Path("id") id: Int
    ): CityData.Result

    @GET(Constants.ecommerce_productList)
    suspend fun productList(
        @Query("city") city: Int,
        @Query("is_available") is_available: Int
    ): Products

    @GET(Constants.ecommerce_wallet)
    suspend fun wallet(
        @Query("customer") customer: Int
    ): Wallet

    @GET(Constants.ecommerce_order)
    suspend fun orderHistory(
        @Query("customer") customer: Int,
        @HeaderMap headers: Map<String, String>
    ): Orderhistory

    @GET(Constants.ecommerce_order)
    suspend fun orderHistory(
        @Query("customer") customer: Int
    ): Orderhistory

    @GET(Constants.ecommerce_walletpromo)
    suspend fun walletPromo(): WalletPromo

    @GET(Constants.ecommerce_productbyid)
    suspend fun productItembyid(
        @Path("id") id: Int
    ): Products.Result


    @GET(Constants.ecommerce_useraddress)
    suspend fun userAddress(
        @Path("id") id: Int
    ): Address

    @POST(Constants.ecommerce_address)
    suspend fun addAddress(
        @Body body: JsonObject
    ): Address


    @FormUrlEncoded
    @PATCH(Constants.ecommerce_address_update)
    suspend fun updateAddress(
        @Path("id") id: Int,
        @Field("address_name") address_name: String,
        @Field("phone_no") phone: String,
        @Field("building_address") buildingAddress: String,
        @Field("street_address") StreetAddress: String,
        @Field("landmark") Landmark: String,
        @Field("city") City: Int,
        @Field("ecommerce_sector") State: Int,
        @Field("pinCode") Pincode: String
    ): Address

    @PATCH(Constants.ecommerce_useraddress)
    suspend fun editUser(
        @Path("id") id: Int,
        @Body body: JsonObject
    ): Address

    //    @DELETE(Constants.ecommerce_deleteaddress)
    @HTTP(method = "DELETE", path = Constants.ecommerce_deleteaddress, hasBody = true)
    suspend fun deleteAddress(
        @Body body: JsonObject
    ): Otpgenerate

    @GET(Constants.ecommerce_membership)
    suspend fun membership(
    ): Membership

    @POST(Constants.ecommerce_membershiprecharge)
    suspend fun membershiprecharge(
        @Body body: JsonObject
    ): MembershipRecharge

    @POST(Constants.ecommerce_customersubscriptions)
    suspend fun subscriptions(
        @Body body: JsonObject
    ): Response<ResponseBody>

    @GET(Constants.ecommerce_subscriptions)
    suspend fun getSubscribe(
        @Query("is_visible") is_visible: Int
    ): Subscribe


    @POST(Constants.ecommerce_cartToken)
    @FormUrlEncoded
    suspend fun getcartToken(
        @FieldMap paramsstring: Map<String, String>,
        @FieldMap paramsdouble: Map<String, Double>,
        @FieldMap paramsarray: Map<String, JsonArray>,
        @FieldMap paramsint: Map<String, Int>,
        @FieldMap paramsbolean: Map<String, Boolean>
    ): Response<CartToken>

    @GET(Constants.ecommerce_customersubscriptions)
    suspend fun getSubList(
        @Query("customer") customer: Int
    ): Sublist

    @POST(Constants.ecommerce_mobile_wallet_recharge)
    @FormUrlEncoded
    suspend fun walletToken(
        @FieldMap paramsint: Map<String, Int>
    ): CartToken

    @POST(Constants.ecommerce_cartToken)
    @FormUrlEncoded
    suspend fun getsubToken(
        @FieldMap paramsstring: Map<String, String>,
        @FieldMap paramsint: Map<String, Int>,
        @FieldMap paramsbolean: Map<String, Boolean>,
        @FieldMap paramsarray: Map<String, JsonObject>,
        @FieldMap paramsdouble: Map<String, Double>,
        @FieldMap paramsdoubledates: Map<String, String>
    ): Response<CartToken>

    @POST(Constants.ecommerce_cartToken)
    @FormUrlEncoded
    fun getsubToken2(
        @FieldMap paramsstring: Map<String, String>,
        @FieldMap paramsint: Map<String, Int>,
        @FieldMap paramsbolean: Map<String, Boolean>,
        @FieldMap paramsarray: Map<String, JsonArray>,
        @FieldMap paramsdouble: Map<String, Double>,
        @Field("dates") paramsarrStrng:ArrayList<String>,
        @Field("days") paramsarrInt:ArrayList<Int>
    ): Call<CartToken>

    @GET(Constants.ecommerce_homeslider)
    suspend fun homeSlider(): HomeSlider

    @POST(Constants.ecommerce_mobile_cart_checkout)
    @FormUrlEncoded
    suspend fun conformPaymentCart(
        @FieldMap paramsint: Map<String, String>
    ): Checkout

    @POST(Constants.ecommerce_mobile_cart_checkoutWallet)
    @FormUrlEncoded
    suspend fun conformPaymentWallet(
        @FieldMap paramsint: Map<String, String>
    ): Checkout

    @POST(Constants.apiTokenRefresh)
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("token") token: String
    ): TokenData


    @GET(Constants.ecommerce_order_list)
    suspend fun orderList(@Query("customer") customer: Int,@Query("from_delivery_date") startDate:String,@Query("to_delivery_date") endDate:String): OrderList


    @GET(Constants.ecommerce_blogs)
    suspend fun getBlog(
        @Query("page") customer: Int
    ): Blogs

    @POST(Constants.ecommerce_payu_hash)
    @FormUrlEncoded
    fun paymentHash(
        @Field("hash_string") hashData: String
    ): Call<CartToken>


    @GET(Constants.ecommerce_orderbyid)
    suspend fun orderDetail(
        @Path("id") id: Int
    ): OrderDetail

    @GET(Constants.ecommerce_referral_code)
    suspend fun referAndEarn(): ReferAndEarn2

    @GET(Constants.ecommerce_products_list_with_given_ids)
    suspend fun getCartList(
        @Query("ids") id: String
    ): CartResponse

    @POST(Constants.ecommerce_customer_referral)
    @FormUrlEncoded
    suspend fun updateReferal(
        @Field("referral_code") referral_code: String,
        @Field("customer_email") customer_email: String,
        @Field("customer_name") customer_name: String,
    ): ReferAndEarn

    @GET(Constants.ecommerce_contact_us)
    suspend fun getContactUs(): ContactusModel

    @FormUrlEncoded
    @POST(Constants.ecommerce_feedback)
    suspend fun faq(
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("phone") phone: String,
        @Field("message") message: String,
        @Field("query_type") query_type: String,
        @Field("is_resolved") is_resolved: Boolean
    ): UserProfileModel


    @GET(Constants.ecommerce_orderEvent)
    suspend fun orderEvent(
        @Query("order") id: Int
    ): OrderEventModel

}