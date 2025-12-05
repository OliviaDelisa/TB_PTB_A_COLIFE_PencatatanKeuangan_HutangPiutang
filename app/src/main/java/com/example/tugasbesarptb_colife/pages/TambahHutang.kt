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
import com.example.tugasbesarptb_colife.model.HutangRequest
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahHutangScreen(navController: NavHostController) {

    val context = LocalContext.current

    TugasBesarPTB_COLIFETheme {

        var namaPeminjam by remember { mutableStateOf("") }
        var tanggalTagihan by remember { mutableStateOf<Long?>(null) }
        var jumlahPinjaman by remember { mutableStateOf("") }
        var showDatePicker by remember { mutableStateOf(false) }

        val datePickerState = rememberDatePickerState()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

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

                Text(
                    text = "Tambahkan Hutang",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // === INPUT NAMA ===
                Text("Nama Peminjam")
                OutlinedTextField(
                    value = namaPeminjam,
                    onValueChange = { namaPeminjam = it },
                    placeholder = { Text("Masukkan nama peminjam", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                // === INPUT TANGGAL ===
                Text("Tanggal Tagihan")
                OutlinedTextField(
                    value = if (tanggalTagihan != null)
                        dateFormatter.format(Date(tanggalTagihan!!))
                    else "",
                    onValueChange = {},
                    placeholder = { Text("Masukkan tanggal tagihan", fontSize = 12.sp) },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Calendar")
                        }
                    },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                // === INPUT NOMINAL ===
                Text("Nominal Pinjaman")
                OutlinedTextField(
                    value = jumlahPinjaman,
                    onValueChange = { newValue ->
                        val digitsOnly = newValue.filter { it.isDigit() }

                        if (digitsOnly.isEmpty()) {
                            jumlahPinjaman = ""
                            return@OutlinedTextField
                        }

                        jumlahPinjaman = digitsOnly.reversed()
                            .chunked(3)
                            .joinToString(".")
                            .reversed()
                    },
                    placeholder = { Text("Masukkan nominal pinjaman", fontSize = 12.sp) },
                    trailingIcon = { Text("Rp", fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp)
                )
            }

            // =============== TOMBOL SIMPAN ===============
            Button(
                onClick = {

                    // VALIDASI
                    if (namaPeminjam.isEmpty() || tanggalTagihan == null || jumlahPinjaman.isEmpty()) {
                        Toast.makeText(context, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val jumlahBersih = jumlahPinjaman.replace(".", "").toInt()

                    val request = HutangRequest(
                        nama = namaPeminjam,
                        tanggal = dateFormatter.format(Date(tanggalTagihan!!)),
                        jumlah = jumlahBersih
                    )

                    // CALL API
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = ApiClient.instance.tambahHutang(request)

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "Hutang berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Gagal menyimpan hutang!", Toast.LENGTH_SHORT).show()
                            }
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
                Text("Selesai", color = Color.White)
            }
        }

        // POPUP DATE PICKER
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        tanggalTagihan = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
