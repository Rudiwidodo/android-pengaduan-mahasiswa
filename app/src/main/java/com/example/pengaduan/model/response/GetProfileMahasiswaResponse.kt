package com.example.pengaduan.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetProfileMahasiswaResponse {
    @SerializedName("result")
    @Expose
    var result: Mahasiswa? = null

    class Mahasiswa {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("no_identitas")
        @Expose
        var nim: String? = null
        @SerializedName("nama_lengkap")
        @Expose
        var nama: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("password")
        @Expose
        var password: String? = null
        @SerializedName("sampul")
        @Expose
        var sampul: String? = null
        @SerializedName("role")
        @Expose
        var role: String? = null
        @SerializedName("is_active")
        @Expose
        var isActive: Int? = 0
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}