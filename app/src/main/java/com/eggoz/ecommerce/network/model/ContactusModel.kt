package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContactusModel(
    @SerializedName("company_address")
    val companyAddress: String?,
    @SerializedName("company_email")
    val companyEmail: String?,
    @SerializedName("company_mobile")
    val companyMobile: String?,
    @SerializedName("office_timings")
    val officeTimings: List<OfficeTiming>?,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
){
    class OfficeTiming(
        @SerializedName("Mon-Fri")
        val monFri: String?,
        @SerializedName("Sat-Sun")
        val satSun: String?
    )
}