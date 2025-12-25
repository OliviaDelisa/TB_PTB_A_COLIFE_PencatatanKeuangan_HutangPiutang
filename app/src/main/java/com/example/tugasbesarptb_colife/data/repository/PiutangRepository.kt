package com.example.tugasbesarptb_colife.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.model.PiutangRequest
import com.example.tugasbesarptb_colife.model.PiutangUpdateRequest
import com.example.tugasbesarptb_colife.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PiutangRepository(
    private val piutangDao: PiutangDao,
    private val apiService: ApiService,
    private val userId: Int
) {

    fun getAllPiutang(): LiveData<List<Piutang>> =
        piutangDao.getAllPiutang(userId).asLiveData()

    suspend fun insert(piutang: Piutang) {
        val localPiutang = piutang.copy(userId = userId, pendingSync = true)
        val localId = piutangDao.insert(localPiutang)

        try {
            val response = apiService.createPiutang(localPiutang.toRequest())
            Log.d("DEBUG_PIUTANG_RESPONSE", response.body().toString())

            if (response.isSuccessful) {
                response.body()?.id?.let { serverId ->

                    piutangDao.updateServerId(localId, serverId)


                    val updatedPiutang = localPiutang.copy(
                        id = localId,
                        serverId = serverId,
                        pendingSync = false
                    )
                    piutangDao.update(updatedPiutang)
                    Log.d("SYNC_PIUTANG", "Insert sukses serverId: $serverId")
                }
            }
        } catch (e: Exception) {
            Log.e("SYNC_PIUTANG", "Insert server gagal: ${e.message}")
        }
    }

    suspend fun update(piutang: Piutang) {
        val updatedPiutang = piutang.copy(pendingSync = true)
        piutangDao.update(updatedPiutang)

        val serverId = piutang.serverId ?: return

        withContext(Dispatchers.IO) {
            try {
                val response = apiService.updatePiutang(
                    serverId.toLong(),
                    updatedPiutang.toRequestUpdateFull()
                )

                if (response.isSuccessful) {
                    val syncedPiutang = updatedPiutang.copy(pendingSync = false)
                    piutangDao.update(syncedPiutang)
                    Log.d("SYNC_PIUTANG", "Update sukses serverId: $serverId")
                }
            } catch (e: Exception) {
                Log.e("SYNC_PIUTANG", "Update server gagal: ${e.message}")
            }
        }
    }

    suspend fun delete(piutang: Piutang) {
        if (piutang.serverId == null) {
            piutangDao.delete(piutang)
            Log.d("DELETE_PIUTANG", "Delete local only")
            return
        }

        val deletedPiutang = piutang.copy(pendingSync = true)
        piutangDao.update(deletedPiutang)

        withContext(Dispatchers.IO) {
            try {
                val response = apiService.deletePiutang(piutang.serverId.toLong())
                if (response.isSuccessful) {
                    piutangDao.delete(deletedPiutang)
                    Log.d("DELETE_PIUTANG", "Delete sukses serverId ${piutang.serverId}")
                } else {
                    Log.e("DELETE_PIUTANG", "Delete ditolak server")
                }
            } catch (e: Exception) {
                Log.e("DELETE_PIUTANG", "Delete gagal: ${e.message}")
            }
        }
    }

    suspend fun uploadBukti(piutang: Piutang, uri: Uri, context: Context) {
        val localUri = uri.toString()
        val tanggalSelesaiFinal = piutang.tanggalSelesai ?: getCurrentDateString()

        val updatedPiutang = piutang.copy(
            buktiPembayaranUri = localUri,
            selesai = true,
            tanggalSelesai = tanggalSelesaiFinal,
            pendingSync = true
        )
        piutangDao.update(updatedPiutang)

        val serverId = piutang.serverId ?: return

        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext
                val tempFile = File.createTempFile("bukti_", ".jpg", context.cacheDir)
                FileOutputStream(tempFile).use { inputStream.copyTo(it) }

                val body = MultipartBody.Part.createFormData(
                    "bukti",
                    tempFile.name,
                    tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                val response = apiService.uploadBuktiPembayaran(serverId.toLong(), body)
                if (response.isSuccessful) {
                    val syncedPiutang = updatedPiutang.copy(pendingSync = false)
                    piutangDao.update(syncedPiutang)
                    Log.d("UPLOAD_BUKTI", "Upload bukti sukses")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD_BUKTI", "Upload server gagal: ${e.message}")
            }
        }
    }

    private fun Piutang.toRequest(): PiutangRequest =
        PiutangRequest(
            userId = this.userId,
            nama = this.nama,
            jumlah = this.jumlah,
            tanggalTenggat = this.tanggalTenggat,
            tanggalDibuat = this.tanggalDibuat,
            tanggalSelesai = this.tanggalSelesai,
            selesai = this.selesai,
            buktiPembayaranUri = this.buktiPembayaranUri
        )

    private fun Piutang.toRequestUpdateFull(): PiutangUpdateRequest =
        PiutangUpdateRequest(
            nama = this.nama,
            jumlah = this.jumlah,
            tanggalTenggat = this.tanggalTenggat,
            selesai = this.selesai,
            tanggalSelesai = this.tanggalSelesai,
            buktiPembayaranUri = this.buktiPembayaranUri
        )

    private fun getCurrentDateString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


    suspend fun syncPending() {
        withContext(Dispatchers.IO) {
            val pendingList = piutangDao.getPendingSyncPiutang()
            for (piutang in pendingList) {
                try {
                    if (piutang.serverId == null) {
                        val response = apiService.createPiutang(piutang.toRequest())
                        if (response.isSuccessful) {
                            response.body()?.id?.let { serverId ->

                                piutangDao.updateServerId(piutang.id, serverId)
                                val syncedPiutang = piutang.copy(
                                    serverId = serverId,
                                    pendingSync = false
                                )
                                piutangDao.update(syncedPiutang)
                                Log.d("SYNC_PIUTANG", "Insert pending sukses $serverId")
                            }
                        }
                    } else {
                        val response = apiService.updatePiutang(
                            piutang.serverId.toLong(),
                            piutang.toRequestUpdateFull()
                        )
                        if (response.isSuccessful) {
                            val syncedPiutang = piutang.copy(pendingSync = false)
                            piutangDao.update(syncedPiutang)
                            Log.d("SYNC_PIUTANG", "Update pending sukses ${piutang.serverId}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SYNC_PIUTANG", "Sync pending gagal: ${e.message}")
                }
            }
        }
    }
}
