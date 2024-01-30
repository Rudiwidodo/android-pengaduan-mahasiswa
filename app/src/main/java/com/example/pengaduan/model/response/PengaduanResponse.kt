package com.example.pengaduan.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PengaduanResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("result")
    @Expose
    var result: List<Pengaduan>? = null

    class Pengaduan {
        @SerializedName("id")
        @Expose
        var id: Int? = 0
        @SerializedName("no_identitas")
        @Expose
        var nim: String? = null
        @SerializedName("isi_pengaduan")
        @Expose
        var isiPengaduan: String? = null
        @SerializedName("foto")
        @Expose
        var foto: String? = null
        @SerializedName("is_active")
        @Expose
        var isActive: Int? = 0
        @SerializedName("is_archive")
        @Expose
        var isArchive: Int? = 0
        @SerializedName("user_id")
        @Expose
        var userId: Int? = 0
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}