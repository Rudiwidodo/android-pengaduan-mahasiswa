package com.example.pengaduan.services

import com.example.pengaduan.model.response.LogoutResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutApi {
    @POST("auth/logout")
    fun logout(
        @Header("Authorization") token: String? = null
    ) : Call<LogoutResponse>
}