package com.example.tugasbesarptb_colife.pages

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.model.HutangItem
import com.example.tugasbesarptb_colife.model.HutangRequest
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHutangScreen(navController: NavHostController) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Ambil data dari HutangScreen
    val editData = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<HutangItem>("editData")

    // Formatter tanggal
    val dbFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())   // format dari API
    val uiFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())   // format tampilan UI

    // ========== STATE ==========
    var namaPeminjam by remember { mutableStateOf(editData?.nama ?: "") }

    // Parse tanggal dari editData
    var tanggalTagihan by remember {
        mutableStateOf(
            editData?.tanggal?.let {
                try { dbFormat.parse(it)?.time }
                catch (_: Exception) { null }
            }
        )
    }

    var jumlahPinjaman by remember { mutableStateOf(editData?.jumlah.toString()) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    TugasBesarPTB_COLIFETheme {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                // Judul
                Text(
                    text = "Edit Hutang",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // ================== NAMA ==================
                Text("Nama Peminjam")
                OutlinedTextField(
                    value = namaPeminjam,
                    onValueChange = { namaPeminjam = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                // ================== TANGGAL ==================
                Text("Tanggal Tagihan")
                OutlinedTextField(
                    value = tanggalTagihan?.let { uiFormat.format(Date(it)) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                // ================== JUMLAH ==================
                Text("Jumlah Pinjaman")
                OutlinedTextField(
                    value = jumlahPinjaman,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        jumlahPinjaman =
                            if (digits.isEmpty()) ""
                            else digits.reversed().chunked(3).joinToString(".").reversed()
                    },
                    trailingIcon = {
                        Text("Rp", fontWeight = FontWeight.Bold)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp)
                )
            }

            // ================== BUTTON SIMPAN ==================
            Button(
                onClick = {
                    scope.launch {
                        // 1. Validasi Data
                        if (editData == null) {
                            Toast.makeText(context, "Data hutang tidak ditemukan!", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        if (tanggalTagihan == null) {
                            Toast.makeText(context, "Tanggal wajib diisi!", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val jumlahInt = jumlahPinjaman.replace(".", "").toIntOrNull() ?: 0

                        // 2. Siapkan Data
                        val apiDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                            .format(Date(tanggalTagihan!!))

                        val req = HutangRequest(
                            nama = namaPeminjam,
                            tanggal = apiDateString,
                            jumlah = jumlahInt
                        )

                        // 3. Request API
                        try {
                            val res = ApiClient.instance.updateHutang(editData.id, req)

                            if (res.isSuccessful && res.body()?.success == true) {
                                Toast.makeText(context, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                
                                // Trigger refresh di halaman sebelumnya
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("refresh", true)

                                // Kembali ke halaman list
                                navController.popBackStack()
                            } else {
                                val errorMsg = res.errorBody()?.string() ?: "Gagal update dari server"
                                Toast.makeText(context, "Gagal: $errorMsg", Toast.LENGTH_LONG).show()
                            }

                        } catch (e: Exception) {
                            // Error koneksi atau exception lain
                            Toast.makeText(context, "Error koneksi: ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A9C90)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Simpan Perubahan",
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            // ================== DATE PICKER ==================
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                tanggalTagihan = datePickerState.selectedDateMillis
                                showDatePicker = false
                            }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false }
                        ) { Text("Batal") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}
