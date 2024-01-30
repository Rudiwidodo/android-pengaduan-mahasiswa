package com.example.pengaduan.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailPengaduanResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("id")
        @Expose
        var id: Int? = null
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
        var isActive: Int? = null
        @SerializedName("is_archive")
        @Expose
        var isArchive: Int? = null
        @SerializedName("user_id")
        @Expose
        var userId: Int? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}