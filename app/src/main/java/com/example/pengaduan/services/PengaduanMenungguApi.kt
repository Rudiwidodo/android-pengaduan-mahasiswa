package com.example.pengaduan.services

import com.example.pengaduan.model.response.PengaduanResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PengaduanMenungguApi {
    @GET("pengaduan")
    fun getPengaduanMenunggu(
        @Header("Authorization") token: String? = null
    ) : Call<PengaduanResponse>
}