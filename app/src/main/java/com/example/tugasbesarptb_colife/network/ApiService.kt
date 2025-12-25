package com.example.tugasbesarptb_colife.network

import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ===== User =====
    @POST("api/user/register")
    suspend fun registerUser(@Body request: UserRegisterRequest): Response<ServerResponse>

    @POST("api/user/login")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<ServerResponse>

    // ===== Piutang =====
    @POST("api/piutang")
    suspend fun createPiutang(@Body request: PiutangRequest): Response<ServerResponse>

    @PUT("api/piutang/{id}")
    suspend fun updatePiutang(@Path("id") id: Long, @Body request: PiutangUpdateRequest): Response<ServerResponse>

    @GET("api/piutang/user/{id}")
    suspend fun getPiutangByUser(@Path("id") userId: Int): List<Piutang>

    @Multipart
    @POST("api/piutang/{id}/upload")
    suspend fun uploadBuktiPembayaran(@Path("id") piutangId: Long, @Part file: MultipartBody.Part): Response<ServerResponse>

    @DELETE("api/piutang/{id}")
    suspend fun deletePiutang(@Path("id") id: Long): Response<ResponseBody>

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

    @POST("api/pengeluaran")
    suspend fun postPengeluaran(
        @Body pengeluaran: Pengeluaran
    ): Response<ServerResponse>

    @POST("api/kategori")
    suspend fun addKategori(
        @Body request: KategoriRequest
    ): Response<ServerResponse>

}



