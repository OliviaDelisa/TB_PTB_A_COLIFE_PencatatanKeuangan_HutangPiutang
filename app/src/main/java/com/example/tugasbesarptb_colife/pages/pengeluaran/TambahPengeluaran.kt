package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.model.Pengeluaran
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.viewmodel.PengeluaranViewModel

@Composable
fun TambahPengeluaranScreen(navController: NavController, pengeluaranViewModel: PengeluaranViewModel) {
    var namaPengeluaran by remember { mutableStateOf("") }
    var tanggalPengeluaran by remember { mutableStateOf("") }
    var jumlahPengeluaran by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Tambahkan Daftar Pengeluaran",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
            )

            Text("Nama Pengeluaran", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = namaPengeluaran,
                onValueChange = { namaPengeluaran = it },
                placeholder = { Text("Masukkan nama pengeluaran") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4DB6AC),
                    unfocusedBorderColor = Color(0xFF4DB6AC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tanggal Pengeluaran", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = tanggalPengeluaran,
                onValueChange = { tanggalPengeluaran = it },
                placeholder = { Text("Masukkan tanggal pengeluaran") },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Tanggal", tint = Color(0xFF4DB6AC))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4DB6AC),
                    unfocusedBorderColor = Color(0xFF4DB6AC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Jumlah Pengeluaran", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = jumlahPengeluaran,
                onValueChange = { jumlahPengeluaran = it },
                placeholder = { Text("Masukkan nominal yang pengeluaran") },
                trailingIcon = { Text("Rp", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4DB6AC),
                    unfocusedBorderColor = Color(0xFF4DB6AC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Bukti Pemasukan", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Handle Upload */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2DFDB)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.ArrowUpward,
                        contentDescription = "Upload",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload", color = Color.Black)
                }

                Button(
                    onClick = {
                        val pengeluaran = Pengeluaran(namaPengeluaran, tanggalPengeluaran, jumlahPengeluaran)
                        pengeluaranViewModel.tambahPengeluaran(pengeluaran)
                        navController.navigate("daftarpengeluaran") {
                            popUpTo("daftarpengeluaran") {
                                inclusive = true
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Simpan", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TambahPengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        TambahPengeluaranScreen(rememberNavController(), viewModel())
    }
}
