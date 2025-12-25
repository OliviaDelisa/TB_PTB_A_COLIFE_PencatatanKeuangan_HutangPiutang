package com.example.tugasbesarptb_colife.pages.piutang

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import com.example.tugasbesarptb_colife.getCurrentDate
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModel
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModelFactory
import com.example.tugasbesarptb_colife.SessionManager
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.NotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPiutang(navController: NavController) {
    val context = LocalContext.current
    val userId = SessionManager(context).getUserId().toInt()
    val repository = remember {
        PiutangRepository(
            piutangDao = AppDatabase.getInstance(context).piutangDao(),
            apiService = ApiClient.instance,
            userId = userId
        )
    }

    val viewModel: PiutangViewModel = viewModel(
        factory = remember { PiutangViewModelFactory(repository) }
    )

    var nama by remember { mutableStateOf("") }
    var tanggalTagihan by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val today = getCurrentDate() // yyyy-MM-dd

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Piutang", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Field Nama
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Peminjam") },
                placeholder = { Text("Masukkan nama yang berhutang") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field Tanggal Tagihan
            OutlinedTextField(
                value = tanggalTagihan,
                onValueChange = { tanggalTagihan = it },
                label = { Text("Tanggal Tagihan") },
                placeholder = { Text("Masukkan tanggal pengembalian") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Pilih Tanggal")
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field Jumlah
            OutlinedTextField(
                value = jumlah,
                onValueChange = { if (it.all { c -> c.isDigit() }) jumlah = it },
                label = { Text("Jumlah Pinjaman") },
                placeholder = { Text("Masukkan nominal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = { Text("Rp", color = Color.Gray, fontWeight = FontWeight.Medium) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Simpan
            Button(
                onClick = {
                    if (nama.isNotBlank() && tanggalTagihan.isNotBlank() && jumlah.isNotBlank()) {
                        val piutang = Piutang(
                            userId = 0,
                            nama = nama,
                            jumlah = jumlah.toInt(),
                            tanggalTenggat = tanggalTagihan,
                            tanggalDibuat = getCurrentDate(),
                            tanggalSelesai = null,
                            selesai = false
                        )
                        viewModel.insertPiutang(piutang)

                        // --- Notifikasi lokal setelah berhasil menambahkan piutang ---
                        NotificationHelper.showNotification(
                            context = context,
                            title = " Piutang Ditambahkan",
                            message = "Piutang \"$nama\" berhasil ditambahkan",
                            intent = Intent(context, context.javaClass) // buka MainActivity
                        )

                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E8378)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .align(Alignment.End)
                    .height(48.dp)
                    .width(130.dp)
            ) {
                Text("Simpan", fontSize = 16.sp, color = Color.White)
            }
        }

        // Panggilan TanggalPicker
        TanggalPicker(
            buka = showDatePicker,
            tanggalMin = today,
            saatTutup = { showDatePicker = false },
            saatDipilih = { tanggal -> tanggalTagihan = tanggal }
        )
    }
}
