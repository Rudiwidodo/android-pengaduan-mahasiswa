package com.example.pengaduan.services

import com.example.pengaduan.model.response.UpdatePengaduanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UpdatePengaduanApi {
    @Multipart
    @POST("pengaduan/update/{id}")
    fun updatePengaduan(
        @Header("Authorization") token: String? = null,
        @Path("id") id: Int? = null,
        @Part("no_identitas") nim: String? = null,
        @Part("isi_pengaduan") isiPengaduan: RequestBody,
        @Part foto: MultipartBody.Part
    ) : Call<UpdatePengaduanResponse>
}