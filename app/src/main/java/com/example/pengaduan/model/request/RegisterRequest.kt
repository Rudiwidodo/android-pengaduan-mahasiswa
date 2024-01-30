package com.example.pengaduan.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// di bagian ini itu harus sesuai dengan apa yang diminta dari server
class RegisterRequest {
    @SerializedName("no_identitas")
    @Expose
    var inputNim : String? = null
    @SerializedName("nama_lengkap")
    @Expose
    var inputNama : String? = null
    @SerializedName("email")
    @Expose
    var inputEmail : String? = null
    @SerializedName("password")
    @Expose
    var inputPassword : String? = null
}