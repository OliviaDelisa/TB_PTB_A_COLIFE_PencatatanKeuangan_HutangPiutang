package com.example.tugasbesarptb_colife.model

import com.google.gson.annotations.SerializedName

data class GambarHutang(
    @SerializedName("id")
    val id: Int,

    @SerializedName("hutang_id")
    val hutangId: Int,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("created_at")
    val createdAt: String?
)

data class GambarResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: List<GambarHutang>
)