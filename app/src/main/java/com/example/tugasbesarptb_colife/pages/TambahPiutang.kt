package com.example.tugasbesarptb_colife.pages

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.components.TopBar
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModel
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModelFactory
import kotlinx.coroutines.launch
import com.example.tugasbesarptb_colife.getCurrentDate

@Composable
fun TambahPiutang(navController: NavController) {
    // 1️⃣ Ambil context untuk Room
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val repository = PiutangRepository(database.piutangDao())

    // 2️⃣ Buat ViewModel pakai Factory
    val viewModel: PiutangViewModel = viewModel(
        factory = PiutangViewModelFactory(repository)
    )

    // 3️⃣ State Compose
    var nama by remember { mutableStateOf("") }
    var tanggalTagihan by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Coroutine scope untuk insert
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(navController, currentRoute = "hutang") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Tambahkan Daftar Piutang",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Peminjam") },
                placeholder = { Text("Masukkan nama yang berhutang") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tanggalTagihan,
                onValueChange = {},
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

            Button(
                onClick = {
                    if (nama.isNotBlank() && tanggalTagihan.isNotBlank() && jumlah.isNotBlank()) {
                        val piutang = Piutang(
                            nama = nama,
                            tanggalTenggat = tanggalTagihan,
                            jumlah = jumlah.toInt(),
                            tanggalDibuat = getCurrentDate(),
                            tanggalSelesai = null,
                            selesai = false
                        )
                        scope.launch {
                            viewModel.insertPiutangObject(piutang)
                        }
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

        TanggalPicker(
            buka = showDatePicker,
            saatTutup = { showDatePicker = false },
            saatDipilih = { tanggal -> tanggalTagihan = tanggal }
        )
    }
}



