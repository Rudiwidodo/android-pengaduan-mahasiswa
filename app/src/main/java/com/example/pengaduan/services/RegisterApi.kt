package com.example.pengaduan.services

import com.example.pengaduan.model.request.RegisterRequest
import com.example.pengaduan.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// memangggil url api dari register
interface RegisterApi {
    @POST("auth/register")
    fun register(
        @Body req : RegisterRequest
    ) : Call<RegisterResponse>
}