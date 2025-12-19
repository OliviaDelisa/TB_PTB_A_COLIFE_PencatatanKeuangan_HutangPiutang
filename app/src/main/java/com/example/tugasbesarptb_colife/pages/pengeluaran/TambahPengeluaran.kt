package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

// ✅ DIGABUNG DI FILE INI (AMAN)
data class Pengeluaran(
    val nama: String,
    val tanggal: String,
    val jumlah: String
)

@Composable
fun TambahPengeluaranScreen(
    navController: NavController,
    onAddPengeluaran: (Pengeluaran) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var bukaTanggalPicker by remember { mutableStateOf(false) }

    // DATE PICKER
    TanggalPicker(
        buka = bukaTanggalPicker,
        saatTutup = { bukaTanggalPicker = false },
        saatDipilih = { tanggal = it }
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Tambahkan Pengeluaran",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            Text("Nama Pengeluaran")
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text("Tanggal Pengeluaran")
            OutlinedTextField(
                value = tanggal,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Pilih tanggal") },
                trailingIcon = {
                    IconButton(onClick = { bukaTanggalPicker = true }) {
                        Icon(Icons.Default.DateRange, null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text("Jumlah Pengeluaran")
            OutlinedTextField(
                value = jumlah,
                onValueChange = { jumlah = it },
                trailingIcon = { Text("Rp") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(32.dp))
            Text("Kategori Pengeluaran")
            OutlinedTextField(
                value = jumlah,
                onValueChange = { jumlah = it },
                trailingIcon = { Text("Rp") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB2DFDB)
                    )
                ) {
                    Icon(Icons.Default.ArrowUpward, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Upload")
                }

                Button(
                    onClick = {
                        onAddPengeluaran(
                            Pengeluaran(nama, tanggal, jumlah)
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4DB6AC)
                    )
                ) {
                    Text("Simpan", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTambahPengeluaran() {
    TugasBesarPTB_COLIFETheme {
        TambahPengeluaranScreen(
            navController = rememberNavController(),
            onAddPengeluaran = {}
        )
    }
}
