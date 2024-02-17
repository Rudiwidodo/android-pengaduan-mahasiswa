package com.example.pengaduan.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PengaduanRequest {

    @SerializedName("tanggal")
    @Expose
    var tanggal: String? = null

}