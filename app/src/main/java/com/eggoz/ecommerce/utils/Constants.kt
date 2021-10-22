package com.eggoz.ecommerce.utils

class Constants {

    companion object {
        const val AppUrl = "https://stage.backend.eggoz.in/"

        //        const val AppUrl="https://backend.eggoz.in/"
        const val RESOLVE_HINT = 301
        const val DB_NAME = "EggozCart"


        const val cashFree_Server = "TEST"


        const val apiend_otpgenerate = "ecomm_login_otp/generate/"
        const val apiend_otpvalidate = "ecomm_login_otp/validate/"
        const val ecommerce_zone = "base/ecommerce_zone/"
        const val ecommerce_zone_id = "base/ecommerce_zone/{id}/"
        const val ecommerce_productList = "product/ecommerce/"
        const val ecommerce_productbyid = "product/ecommerce/{id}/"
        const val ecommerce_wallet = "ecommerce/customer_wallet/"
        const val ecommerce_walletpromo = "ecommerce/recharge_voucher/"
        const val ecommerce_order = "order/"
        const val ecommerce_useraddress = "users/{id}/"
        const val ecommerce_deleteaddress = "users/delete_address/"
        const val ecommerce_address = "users/add_address/"
        const val ecommerce_address_update = "/addresses/{id}/"
        const val ecommerce_membership = "ecommerce/membership/"
        const val ecommerce_membershiprecharge = "ecommerce/membership/recharge/"
        const val ecommerce_customersubscriptions = "ecommerce/customer_subscriptions/"
        const val ecommerce_subscriptions = "ecommerce/subscription/"
        const val ecommerce_cartToken = "ecommerce/cart/mobile_cart_checkout/"
        const val ecommerce_mobile_wallet_recharge = "ecommerce/wallet/mobile_wallet_recharge/"
        const val ecommerce_homeslider = "ecommerce/slider/"
        const val ecommerce_mobile_cart_checkout = "payment/return_mobile_wallet_recharge/"
        const val ecommerce_mobile_cart_checkoutWallet = "payment/return_mobile_wallet_recharge/"
    }
}