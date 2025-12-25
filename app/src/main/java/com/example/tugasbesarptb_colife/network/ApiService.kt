package com.example.tugasbesarptb_colife.network

import com.example.tugasbesarptb_colife.model.GambarResponse // <--- Import Baru (Wajib ada)
import com.example.tugasbesarptb_colife.model.HutangRequest
import com.example.tugasbesarptb_colife.model.HutangListResponse
import com.example.tugasbesarptb_colife.model.ServerResponse
import com.example.tugasbesarptb_colife.model.UserLoginRequest
import com.example.tugasbesarptb_colife.model.UserRegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    // ================= HUTANG (CRUD DATA) =================

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

    @POST("api/hutang/selesai/{id}")
    suspend fun selesaiHutang(
        @Path("id") id: Int
    ): Response<ServerResponse>


    // ================= GAMBAR BUKTI (BARU & LENGKAP) =================

    // 1. UPLOAD (Auto Save)
    @Multipart
    @POST("api/hutang/upload-bukti")
    suspend fun uploadBuktiPermanen(
        @Part("hutang_id") hutangId: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ServerResponse>

    // 2. AMBIL LIST GAMBAR (Auto Load)
    // Mengambil semua gambar berdasarkan ID Hutang
    @GET("api/hutang/gambar/{id}")
    suspend fun getGambarHutang(
        @Path("id") id: String
    ): Response<GambarResponse>

    // 3. HAPUS GAMBAR SATUAN
    // Menghapus satu foto bukti saja
    @DELETE("api/hutang/gambar/delete/{id}")
    suspend fun deleteGambarBukti(
        @Path("id") id: Int // Ini ID Gambar, bukan ID Hutang
    ): Response<ServerResponse>
}