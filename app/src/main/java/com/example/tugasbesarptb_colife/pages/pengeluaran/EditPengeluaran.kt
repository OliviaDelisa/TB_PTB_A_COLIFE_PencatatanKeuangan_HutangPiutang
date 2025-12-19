package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.ui.theme.merah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPengeluaranScreen(
    navController: NavController,
    pengeluaran: Pengeluaran, // Parameter untuk menerima data yang akan diedit
    onSave: (Pengeluaran) -> Unit, // Parameter fungsi untuk menyimpan
    onDelete: () -> Unit // Parameter fungsi untuk menghapus
) {
    // Inisialisasi state dengan data dari parameter `pengeluaran`
    var namaState by remember { mutableStateOf(pengeluaran.nama) }
    var tanggalState by remember { mutableStateOf(pengeluaran.tanggal) }
    var jumlahState by remember { mutableStateOf(pengeluaran.jumlah) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Daftar Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                text = "Nama",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = namaState,
                onValueChange = { namaState = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = hijau30,
                    unfocusedBorderColor = hijau30,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tanggal Pengeluaran",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = tanggalState,
                onValueChange = { tanggalState = it },
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Pilih Tanggal")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = hijau30,
                    unfocusedBorderColor = hijau30,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jumlah Pengeluaran",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = jumlahState,
                onValueChange = { jumlahState = it },
                trailingIcon = { Text("Rp", fontWeight = FontWeight.Medium) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = hijau30,
                    unfocusedBorderColor = hijau30,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tombol Hapus sekarang memanggil fungsi onDelete
                Button(
                    onClick = { onDelete() },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = merah)
                ) {
                    Text("Hapus", color = Color.White, fontSize = 16.sp)
                }

                // Tombol Simpan sekarang memanggil fungsi onSave
                Button(
                    onClick = {
                        val updatedPengeluaran = Pengeluaran(
                            nama = namaState,
                            tanggal = tanggalState,
                            jumlah = jumlahState
                        )
                        onSave(updatedPengeluaran)
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = hijau30)
                ) {
                    Text("Simpan Pengeditan", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
