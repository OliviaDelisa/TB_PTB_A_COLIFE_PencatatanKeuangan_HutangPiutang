package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.pages.pengeluaran.viewmodel.PengeluaranViewModel
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.ui.theme.merah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPengeluaranScreen(
    navController: NavController,
    pengeluaranId: Int,
    viewModel: PengeluaranViewModel
) {
    val pengeluaran by viewModel.getPengeluaranById(pengeluaranId).collectAsState(initial = null)

    if (pengeluaran == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        EditPengeluaranContent(navController, pengeluaran!!, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPengeluaranContent(
    navController: NavController,
    pengeluaran: Pengeluaran,
    viewModel: PengeluaranViewModel
) {
    var namaState by remember { mutableStateOf(pengeluaran.nama) }
    var tanggalState by remember { mutableStateOf(pengeluaran.tanggal) }
    var jumlahState by remember { mutableStateOf(pengeluaran.jumlah) }
    var kategoriState by remember { mutableStateOf(pengeluaran.kategori) }
    var bukaTanggalPicker by remember { mutableStateOf(false) }

    LaunchedEffect(pengeluaran) {
        namaState = pengeluaran.nama
        tanggalState = pengeluaran.tanggal
        jumlahState = pengeluaran.jumlah
        kategoriState = pengeluaran.kategori
    }

    TanggalPicker(
        buka = bukaTanggalPicker,
        saatTutup = { bukaTanggalPicker = false },
        saatDipilih = { tanggalState = it }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text("Nama", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = namaState,
                onValueChange = { namaState = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tanggal", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = tanggalState,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { bukaTanggalPicker = true }) {
                        Icon(Icons.Default.DateRange, "Pilih Tanggal")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Jumlah", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = jumlahState,
                onValueChange = { jumlahState = it },
                trailingIcon = { Text("Rp", fontWeight = FontWeight.Medium) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Kategori", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = kategoriState,
                onValueChange = { kategoriState = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        viewModel.delete(pengeluaran)
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = merah)
                ) {
                    Text("Hapus", color = Color.White, fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        val updatedPengeluaran = pengeluaran.copy(
                            nama = namaState,
                            tanggal = tanggalState,
                            jumlah = jumlahState,
                            kategori = kategoriState
                        )
                        viewModel.update(updatedPengeluaran)
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = hijau30)
                ) {
                    Text("Simpan", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}