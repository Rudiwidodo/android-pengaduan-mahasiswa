package com.example.pengaduan.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeletePengaduanResponse {
    @SerializedName("message")
    @Expose
    var message: String? = null
}