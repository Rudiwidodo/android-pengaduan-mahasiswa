package com.example.pengaduan.services

import com.example.pengaduan.model.request.PengaduanRequest
import com.example.pengaduan.model.response.PengaduanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PengaduanMenugguFilter {
    @POST("pengaduan/filter_menunggu")
    fun menugguFilter(
        @Header("Authorization") token: String? = null,
        @Body request: PengaduanRequest
    ) : Call<PengaduanResponse>

}