package com.example.pengaduan.services

import com.example.pengaduan.model.request.ChangePasswordRequest
import com.example.pengaduan.model.response.ChangePasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChangePasswordApi {
    @POST("profile/changePassword/{id}")
    fun changePassword(
        @Header("Authorization") token: String? = null,
        @Path("id") id: Int?,
        @Body request: ChangePasswordRequest
    ) : Call<ChangePasswordResponse>
}