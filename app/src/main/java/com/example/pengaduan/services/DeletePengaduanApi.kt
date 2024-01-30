package com.example.pengaduan.services

import com.example.pengaduan.model.response.DeletePengaduanResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface DeletePengaduanApi {
    @DELETE("pengaduan/delete/{id}")
    fun deletePengaduan(
        @Header("Authorization") token: String? = null,
        @Path("id") id: Int? = null
    ) : Call<DeletePengaduanResponse>
}