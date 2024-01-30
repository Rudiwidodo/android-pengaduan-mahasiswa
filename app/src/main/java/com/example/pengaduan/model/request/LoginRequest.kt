package com.example.pengaduan.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// harus sesuai dengan request dari server
class LoginRequest {
    @SerializedName("userdata")
    @Expose
    var userdata: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
}