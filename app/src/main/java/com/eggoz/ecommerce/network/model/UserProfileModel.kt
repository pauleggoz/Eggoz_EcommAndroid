package com.eggoz.ecommerce.network.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileModel(
    @SerializedName("email")
    val email: String?,
    @SerializedName("feedback_date")
    val feedbackDate: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("is_resolved")
    val isResolved: Boolean?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("query_type")
    val queryType: String?,
    @SerializedName("errors")
    @Expose
    var errors: List<Errors>? = null,
    @SerializedName("error_type")
    @Expose
    var errorType: String? = null
)