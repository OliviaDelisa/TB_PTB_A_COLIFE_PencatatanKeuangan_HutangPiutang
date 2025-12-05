package com.example.tugasbesarptb_colife.network


import com.example.tugasbesarptb_colife.model.HutangRequest
import com.example.tugasbesarptb_colife.model.HutangListResponse
import com.example.tugasbesarptb_colife.model.ServerResponse
import com.example.tugasbesarptb_colife.model.UserLoginRequest
import com.example.tugasbesarptb_colife.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path

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

    // Tambah hutang
    @POST("api/hutang/add")
    suspend fun tambahHutang(
        @Body request: HutangRequest
    ): Response<ServerResponse>

    // Ambil semua hutang
    @GET("api/hutang")
    suspend fun getHutang(): Response<HutangListResponse>

    // Hapus hutang
    @DELETE("api/hutang/delete/{id}")
    suspend fun deleteHutang(
        @Path("id") id: Int
    ): Response<ServerResponse>

    @PUT("api/hutang/selesai/{id}")
    suspend fun selesaiHutang(
        @Path("id") id: Int
    ): Response<ServerResponse>

    // Update hutang
    @PUT("api/hutang/update/{id}")
    suspend fun updateHutang(
        @Path("id") id: Int,
        @Body request: HutangRequest
    ): Response<ServerResponse>
}
