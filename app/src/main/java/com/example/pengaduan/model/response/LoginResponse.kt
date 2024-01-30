package com.example.pengaduan.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
}