package com.example.pengaduan.services

import com.example.pengaduan.model.response.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UpdateProfileApi {
    @Multipart
    @POST("profile/update/{id}")
    fun updateProfile(
        @Header("Authorization") token: String? = null,
        @Path("id") id: Int,
        @Part("no_identitas") nim: RequestBody,
        @Part("nama_lengkap") nama: RequestBody,
        @Part("email") email: RequestBody,
        @Part sampul: MultipartBody.Part
    ) : Call<UpdateProfileResponse>
}