package com.eggoz.ecommerce.utils

class Constants {

    companion object {
        const val AppUrl = "https://stage.backend.eggoz.in/"

        //        const val AppUrl="https://backend.eggoz.in/"
        const val RESOLVE_HINT = 301
        const val DB_NAME = "EggozCart"


        //        const val cashFree_Server = "TEST"
        const val payU_IsProduction = false


        const val apiend_otpgenerate = "ecomm_login_otp/generate/"
        const val apiTokenRefresh = "api-token-refresh/"
        const val apiend_otpvalidate = "ecomm_login_otp/validate/"
        const val ecommerce_zone = "base/ecommerce_zone/"
        const val ecommerce_zone_id = "base/ecommerce_zone/{id}/"
        const val ecommerce_productList = "product/ecommerce/"
        const val ecommerce_productbyid = "product/ecommerce/{id}/"
        const val ecommerce_wallet = "ecommerce/customer_wallet/"
        const val ecommerce_walletpromo = "ecommerce/recharge_voucher/"
        const val ecommerce_order = "order/ecommerce_order/"
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
        const val ecommerce_order_list = "order/ecommerce_order/"
        const val ecommerce_blogs = "ecommerce/app-blogs/"
        const val ecommerce_payu_hash = "payu/generate_second_hash/"
        const val ecommerce_orderbyid = "order/{id}/"
        const val ecommerce_referral_code= "ecommerce/customer_membership/get_user_referral_code/"
        const val ecommerce_products_list_with_given_ids= "product/ecommerce/products_list_with_given_ids/"
        const val ecommerce_customer_referral= "ecommerce/customer_subscriptions/customer_referral/"
        const val ecommerce_contact_us= "api/feedback/contact_us/"
        const val ecommerce_feedback= "api/feedback/"
    }
}