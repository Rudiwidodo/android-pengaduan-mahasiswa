package com.example.pengaduan.services

import com.example.pengaduan.model.request.LoginRequest
import com.example.pengaduan.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    fun login(
        @Body req: LoginRequest
    ) : Call<LoginResponse>
}