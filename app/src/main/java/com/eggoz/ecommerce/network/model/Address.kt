package com.eggoz.ecommerce.network.model

import com.eggoz.ecommerce.network.model.Otpverify.User.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Address(
    @SerializedName("id")
    @Expose
     val id: Int? = null,

    @SerializedName("email")
    @Expose
     val email: String? = null,

    @SerializedName("success")
    @Expose
     val success: String? = null,

    @SerializedName("name")
    @Expose
     val name: String? = null,

    @SerializedName("phone_no")
    @Expose
     val phoneNo: String? = null,

    @SerializedName("default_address")
    @Expose
     val defaultAddress: DefaultAddress? = null,

    @SerializedName("addresses")
    @Expose
     val addresses: List<AAddress>? = null,

    @SerializedName("userProfile")
    @Expose
     val userProfile: UserProfile? = null,

    @SerializedName("userTokenData")
    @Expose
     val userTokenData: UserTokenData? = null,

    @SerializedName("userData")
    @Expose
     val userData: Any? = null,

    @SerializedName("userCities")
    @Expose
     val userCities: List<UserCity>? = null,

    @SerializedName("date_joined")
    @Expose
     val dateJoined: String? = null,

    @SerializedName("is_profile_verified")
    @Expose
     val isProfileVerified: Boolean? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
){
    class UserTokenData(

        @SerializedName("token")
        @Expose
        val token: String? = null,
        @SerializedName("token_exp")
        @Expose
        val token_exp: String? = null,
    )

    class AAddress(
        var isSelected:Boolean=false,

        @SerializedName("id")
        @Expose
         val id: Int? = null,

        @SerializedName("default_address_user")
        @Expose
         val defaultAddressUser: List<Int>? = null,

        @SerializedName("user_addresses_user")
        @Expose
         val userAddressesUser: List<Int>? = null,

        @SerializedName("address_name")
        @Expose
         val addressName: String? = null,

        @SerializedName("building_address")
        @Expose
         val buildingAddress: String? = null,

        @SerializedName("street_address")
        @Expose
         val streetAddress: String? = null,

        @SerializedName("city")
        @Expose
         val city: City__1? = null,

        @SerializedName("ecommerce_sector")
        @Expose
         val ecommerceSector: EcommerceSector? = null,

        @SerializedName("name")
        @Expose
         val name: Any? = null,

        @SerializedName("phone_no")
        @Expose
         val phoneNo: String? = null,

        @SerializedName("landmark")
        @Expose
         val landmark: String? = null,

        @SerializedName("pinCode")
        @Expose
         val pinCode: Int? = null,

        @SerializedName("latitude")
        @Expose
         val latitude: Any? = null,

        @SerializedName("longitude")
        @Expose
         val longitude: Any? = null,

        @SerializedName("date_added")
        @Expose
         val dateAdded: String? = null,

        @SerializedName("billing_city")
        @Expose
         val billingCity: Any? = null
    ){

        class EcommerceSector(
            @SerializedName("id")
            @Expose
            val id: Int? = null,

            @SerializedName("sector_name")
            @Expose
            val sector_name: String? = null,

            @SerializedName("city")
            @Expose
            val city: Int? = null,

            @SerializedName("cluster")
            @Expose
            val cluster: Int? = null

        )
        class City__1(
        @SerializedName("id")
        @Expose
         val id: Int? = null,

        @SerializedName("city_name")
        @Expose
         val cityName: String? = null,

        @SerializedName("state")
        @Expose
         val state: String? = null,

        @SerializedName("country")
        @Expose
         val country: String? = null)
    }


}
