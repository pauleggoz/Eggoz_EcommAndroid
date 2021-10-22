package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Otpverify(
    @SerializedName("success")
    @Expose
    var success: String? = null,
    @SerializedName("token")
    @Expose
    var token: String? = null,
    @SerializedName("user")
    @Expose
    var user: User? = null,
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
) {
    class User(
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        @SerializedName("email")
        @Expose
        var email: String? = null,
        @SerializedName("name")
        @Expose
        var name: String? = null,
        @SerializedName("phone_no")
        @Expose
        var phoneNo: String? = null,
        @SerializedName("default_address")
        @Expose
        var defaultAddress: DefaultAddress? = null,
        @SerializedName("addresses")
        @Expose
        var addresses: List<Address>? = null,
        @SerializedName("userProfile")
        @Expose
        var userProfile: UserProfile? = null,
        @SerializedName("userData")
        @Expose
        var userData: Any? = null,
        @SerializedName("userCities")
        @Expose
        var userCities: List<UserCity>? = null,
        @SerializedName("date_joined")
        @Expose
        var dateJoined: String? = null,
        @SerializedName("is_profile_verified")
        @Expose
        var isProfileVerified: Boolean? = null
    ) {

        class Address(
            @SerializedName("id")
            @Expose
            var id: Int? = null,
            @SerializedName("default_address_user")
            @Expose
            var defaultAddressUser: List<Int>? = null,
            @SerializedName("user_addresses_user")
            @Expose
            var userAddressesUser: List<Int>? = null,
            @SerializedName("address_name")
            @Expose
            var addressName: String? = null,
            @SerializedName("building_address")
            @Expose
            var buildingAddress: String? = null,
            @SerializedName("street_address")
            @Expose
            var streetAddress: String? = null,
            @SerializedName("city")
            @Expose
            var city: City? = null,
            @SerializedName("ecommerce_sector")
            @Expose
            var ecommerceSector: EcommerceSector? = null,
            @SerializedName("name")
            @Expose
            var name: Any? = null,
            @SerializedName("phone_no")
            @Expose
            var phoneNo: String? = null,
            @SerializedName("landmark")
            @Expose
            var landmark: Any? = null,
            @SerializedName("pinCode")
            @Expose
            var pinCode: Any? = null,
            @SerializedName("latitude")
            @Expose
            var latitude: Any? = null,
            @SerializedName("longitude")
            @Expose
            var longitude: Any? = null,
            @SerializedName("date_added")
            @Expose
            var dateAdded: String? = null,
            @SerializedName("billing_city")
            @Expose
            var billingCity: Any? = null
        ) {
            class EcommerceSector(
                @SerializedName("id")
                @Expose
                var id: Int? = null,
                @SerializedName("sector_name")
                @Expose
                var sectorName: String? = null,
                @SerializedName("city")
                @Expose
                var city: Int? = null,
                @SerializedName("cluster")
                @Expose
                var cluster: Int? = null
            )

            class City(
                @SerializedName("id")
                @Expose
                var id: Int? = null,
                @SerializedName("city_name")
                @Expose
                var cityName: String? = null,
                @SerializedName("state")
                @Expose
                var state: String? = null,
                @SerializedName("country")
                @Expose
                var country: String? = null,
            )
        }

        class DefaultAddress(
            @SerializedName("id")
            @Expose
            var id: Int? = null,
            @SerializedName("default_address_user")
            @Expose
            var defaultAddressUser: List<Int>? = null,
            @SerializedName("user_addresses_user")
            @Expose
            var userAddressesUser: List<Int>? = null,
            @SerializedName("address_name")
            @Expose
            var addressName: String? = null,
            @SerializedName("building_address")
            @Expose
            var buildingAddress: String? = null,
            @SerializedName("street_address")
            @Expose
            var streetAddress: String? = null,
            @SerializedName("city")
            @Expose
            var city: City? = null,
            @SerializedName("ecommerce_sector")
            @Expose
            var ecommerceSector: EcommerceSector? = null,
            @SerializedName("name")
            @Expose
            var name: Any? = null,
            @SerializedName("phone_no")
            @Expose
            var phoneNo: String? = null,
            @SerializedName("landmark")
            @Expose
            var landmark: Any? = null,
            @SerializedName("pinCode")
            @Expose
            var pinCode: Any? = null,
            @SerializedName("latitude")
            @Expose
            var latitude: Any? = null,
            @SerializedName("longitude")
            @Expose
            var longitude: Any? = null,
            @SerializedName("date_added")
            @Expose
            var dateAdded: String? = null,
            @SerializedName("billing_city")
            @Expose
            var billingCity: Any? = null,
        ) {
            class EcommerceSector(
                @SerializedName("id")
                @Expose
                var id: Int? = null,
                @SerializedName("sector_name")
                @Expose
                var sectorName: String? = null,
                @SerializedName("city")
                @Expose
                var city: Int? = null,
                @SerializedName("cluster")
                @Expose
                var cluster: Int? = null
            )

            class City(
                @SerializedName("id")
                @Expose
                var id: Int? = null,
                @SerializedName("city_name")
                @Expose
                var cityName: String? = null,
                @SerializedName("state")
                @Expose
                var state: String? = null,
                @SerializedName("country")
                @Expose
                var country: String? = null,
            )
        }

        class UserCity(
            @SerializedName("id")
            @Expose
            var id: Int? = null,
            @SerializedName("city_name")
            @Expose
            var cityName: String? = null
        )

        class UserProfile(
            @SerializedName("id")
            @Expose
            var id: Int? = null,
            @SerializedName("departments")
            @Expose
            var departments: List<String>? = null,
            @SerializedName("department_profiles")
            @Expose
            var departmentProfiles: List<DepartmentProfile>? = null,
            @SerializedName("warehouse_ids")
            @Expose
            var warehouseIds: List<Any>? = null
        ) {
            class DepartmentProfile(
                @SerializedName("customerProfile")
                @Expose
                val customerProfile: Int? = null,

                @SerializedName("wallet")
                @Expose
                val wallet: Wallet? = null
            ) {
                class Wallet(
                    @SerializedName("id")
                    @Expose
                    val id: Int? = null,

                    @SerializedName("customer")
                    @Expose
                    val customer: Customer? = null,

                    @SerializedName("total_balance")
                    @Expose
                    val totalBalance: String? = null,

                    @SerializedName("is_recharged_once")
                    @Expose
                    val isRechargedOnce: Boolean? = null,

                    @SerializedName("recharge_balance")
                    @Expose
                    val rechargeBalance: String? = null,

                    @SerializedName("note")
                    @Expose
                    val note: String? = null
                ) {
                    class Customer(
                        @SerializedName("id")
                        @Expose
                        val id: Int? = null,

                        @SerializedName("name")
                        @Expose
                        val name: Any? = null,

                        @SerializedName("code_string")
                        @Expose
                        val codeString: Any? = null,

                        @SerializedName("code_int")
                        @Expose
                        val codeInt: Any? = null,

                        @SerializedName("code")
                        @Expose
                        val code: String? = null,

                        @SerializedName("billing_shipping_address_same")
                        @Expose
                        val billingShippingAddressSame: Boolean? = null,

                        @SerializedName("onboarding_date")
                        @Expose
                        val onboardingDate: String? = null,

                        @SerializedName("last_order_date")
                        @Expose
                        val lastOrderDate: Any? = null,

                        @SerializedName("phone_no")
                        @Expose
                        val phoneNo: String? = null,

                        @SerializedName("is_test_profile")
                        @Expose
                        val isTestProfile: Boolean? = null,

                        @SerializedName("current_order_amount")
                        @Expose
                        val currentOrderAmount: String? = null,

                        @SerializedName("user")
                        @Expose
                        val user: Int? = null,

                        @SerializedName("shipping_address")
                        @Expose
                        val shippingAddress: Int? = null,

                        @SerializedName("billing_address")
                        @Expose
                        val billingAddress: Int? = null
                    )
                }
            }
        }

    }
}
