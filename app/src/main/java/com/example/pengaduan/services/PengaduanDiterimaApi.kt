package com.example.pengaduan.services

import com.example.pengaduan.model.response.PengaduanResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PengaduanDiterimaApi {
    @GET("pengaduan/index_diterima")
    fun getPengaduanDiterima(
        @Header("Authorization") token: String? = null
    ) : Call<PengaduanResponse>
}