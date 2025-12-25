package com.example.tugasbesarptb_colife.network

import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.model.ServerResponse
import com.example.tugasbesarptb_colife.model.UserLoginRequest
import com.example.tugasbesarptb_colife.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
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

}