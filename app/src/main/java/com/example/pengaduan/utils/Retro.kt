package com.example.pengaduan.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// buat penghubung dengan api server
class Retro {
    fun getClientInstance() : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://pengamas.my.id/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}