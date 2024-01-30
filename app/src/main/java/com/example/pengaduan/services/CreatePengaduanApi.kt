package com.example.pengaduan.services

import com.example.pengaduan.model.response.CreatePengaduanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CreatePengaduanApi {
    @Multipart
    @POST("pengaduan/store")
    fun createPengaduan(
        @Header("Authorization") token: String? = null,
        @Part("isi_pengaduan") isiPengaduan: RequestBody,
        @Part foto: MultipartBody.Part
    ) : Call<CreatePengaduanResponse>
}