package com.example.pengaduan.services

import com.example.pengaduan.model.response.GetProfileMahasiswaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetProfileMahaisswaApi {
    @GET("profile")
    fun getProfileMahasiswa(
        @Header("Authorization") token: String? = null,
    ): Call<GetProfileMahasiswaResponse>
}