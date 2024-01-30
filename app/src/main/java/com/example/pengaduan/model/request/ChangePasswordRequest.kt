package com.example.pengaduan.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePasswordRequest {
    @SerializedName("password")
    @Expose
    var password: String? = null
}