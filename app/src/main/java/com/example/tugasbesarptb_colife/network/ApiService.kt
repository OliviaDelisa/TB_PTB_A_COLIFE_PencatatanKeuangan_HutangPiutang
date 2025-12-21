package com.example.tugasbesarptb_colife.network

import com.example.tugasbesarptb_colife.model.HutangRequest
import com.example.tugasbesarptb_colife.model.HutangListResponse
import com.example.tugasbesarptb_colife.model.ServerResponse
import com.example.tugasbesarptb_colife.model.UserLoginRequest
import com.example.tugasbesarptb_colife.model.UserRegisterRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ================= USER =================

    @POST("api/user/register")
    suspend fun registerUser(
        @Body request: UserRegisterRequest
    ): Response<ServerResponse>

    @POST("api/user/login")
    suspend fun loginUser(
        @Body request: UserLoginRequest
    ): Response<ServerResponse>


    // ================= HUTANG =================

    @POST("api/hutang/add")
    suspend fun tambahHutang(
        @Body request: HutangRequest
    ): Response<ServerResponse>

    @GET("api/hutang")
    suspend fun getHutang(): Response<HutangListResponse>

    @DELETE("api/hutang/delete/{id}")
    suspend fun deleteHutang(
        @Path("id") id: Int
    ): Response<ServerResponse>

    @PUT("api/hutang/update/{id}")
    suspend fun updateHutang(
        @Path("id") id: Int,
        @Body request: HutangRequest
    ): Response<ServerResponse>

    @GET("api/hutang/history")
    suspend fun getHistoryHutang(): Response<HutangListResponse>

    @Multipart
    @POST("api/hutang/upload/{id}")
    suspend fun uploadStruk(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Response<ServerResponse>

    @POST("api/hutang/selesai/{id}")
    suspend fun selesaiHutang(
        @Path("id") id: Int
    ): Response<ServerResponse>
}
