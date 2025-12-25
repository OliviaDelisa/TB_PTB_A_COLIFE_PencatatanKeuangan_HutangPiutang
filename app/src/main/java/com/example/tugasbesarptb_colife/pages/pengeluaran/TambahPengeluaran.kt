package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.viewmodel.KategoriViewModel
import com.example.tugasbesarptb_colife.viewmodel.PengeluaranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPengeluaranScreen(navController: NavController) {
    val pengeluaranViewModel: PengeluaranViewModel = viewModel()
    val kategoriViewModel: KategoriViewModel = viewModel()

    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val allKategori by kategoriViewModel.allKategori.observeAsState(initial = emptyList())
    var selectedKategori by remember { mutableStateOf<KategoriPengeluaran?>(null) }
    var isKategoriExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormInput(label = "Nama Pengeluaran", value = nama, onValueChange = { nama = it })

            TanggalInput(label = "Tanggal", value = tanggal, onClick = { showDatePicker = true })

            FormInput(label = "Jumlah", value = jumlah, onValueChange = { jumlah = it }, keyboardType = KeyboardType.Number, trailingIcon = { Text("Rp") })

            ExposedDropdownMenuBox(
                expanded = isKategoriExpanded,
                onExpandedChange = { isKategoriExpanded = !isKategoriExpanded }
            ) {
                OutlinedTextField(
                    value = selectedKategori?.nama ?: "",
                    onValueChange = {}, // read-only
                    readOnly = true,
                    label = { Text("Kategori") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isKategoriExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
                )
                ExposedDropdownMenu(
                    expanded = isKategoriExpanded,
                    onDismissRequest = { isKategoriExpanded = false }
                ) {
                    allKategori.forEach { kategori ->
                        DropdownMenuItem(
                            text = { Text(kategori.nama) },
                            onClick = {
                                selectedKategori = kategori
                                isKategoriExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val jumlahLong = jumlah.toLongOrNull() ?: 0L
                    if (nama.isNotBlank() && jumlahLong > 0 && tanggal.isNotBlank() && selectedKategori != null) {
                        val pengeluaran = Pengeluaran(
                            nama = nama,
                            jumlah = jumlahLong,
                            tanggal = tanggal,
                            kategoriId = selectedKategori!!.id
                        )
                        pengeluaranViewModel.insert(pengeluaran)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = hijau30)
            ) {
                Text("Simpan", color = Color.White, fontSize = 16.sp)
            }
        }

        if (showDatePicker) {
            TanggalPicker(buka = true, saatTutup = { showDatePicker = false }, saatDipilih = { tanggal = it })
        }
    }
}

@Composable
private fun FormInput(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, trailingIcon: @Composable (() -> Unit)? = null) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
        )
    }
}

@Composable
private fun TanggalInput(label: String, value: String, onClick: () -> Unit) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Pilih tanggal") },
            trailingIcon = { IconButton(onClick = onClick) { Icon(Icons.Default.CalendarToday, null) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = hijau30, unfocusedBorderColor = hijau30)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTambahPengeluaran() {
    TugasBesarPTB_COLIFETheme {
        TambahPengeluaranScreen(navController = rememberNavController())
    }
}
