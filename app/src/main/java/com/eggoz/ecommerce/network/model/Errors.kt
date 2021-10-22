package com.eggoz.ecommerce.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("field")
    @Expose
    var field: String?=null,
    @SerializedName("message")
    @Expose
    var message: String?=null
)
