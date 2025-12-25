package com.example.tugasbesarptb_colife.network

import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.model.KategoriDto
import com.example.tugasbesarptb_colife.model.KategoriRequest
import com.example.tugasbesarptb_colife.model.ServerResponse
import com.example.tugasbesarptb_colife.model.UserLoginRequest
import com.example.tugasbesarptb_colife.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("api/user/register")
    suspend fun registerUser(
        @Body request: UserRegisterRequest
    ): Response<ServerResponse>

    @POST("api/user/login")
    suspend fun loginUser(
        @Body request: UserLoginRequest
    ): Response<ServerResponse>

    @POST("api/pengeluaran")
    suspend fun postPengeluaran(
        @Body pengeluaran: Pengeluaran
    ): Response<ServerResponse>

    @POST("api/kategori")
    suspend fun addKategori(
        @Body request: KategoriRequest
    ): Response<ServerResponse>

    // --- KATEGORI ---
    @GET("api/kategori")
    suspend fun getKategori(): Response<List<KategoriDto>>

}