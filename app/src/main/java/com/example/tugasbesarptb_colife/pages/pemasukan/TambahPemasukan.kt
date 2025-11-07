package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPemasukanScreen(navController: NavController) {

    // State untuk menyimpan nilai dari setiap TextField
    var sumberPemasukan by remember { mutableStateOf("") }
    var tanggalPemasukan by remember { mutableStateOf("") }
    var jumlahPemasukan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tambahkan Pemasukan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Aksi untuk kembali
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            // Kita panggil lagi BottomNavBar yang sudah ada di file DaftarPemasukan.kt
            BottomNavBar(navController = navController)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Input: Sumber Pemasukan
            FormInput(
                label = "Sumber Pemasukan",
                value = sumberPemasukan,
                onValueChange = { sumberPemasukan = it },
                placeholder = "Masukkan sumber pemasukan"
            )

            // Input: Tanggal Pemasukan
            FormInput(
                label = "Tanggal Pemasukan",
                value = tanggalPemasukan,
                onValueChange = { tanggalPemasukan = it },
                placeholder = "Masukkan tanggal pemasukan",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Kalender",
                        tint = Color.Gray
                    )
                }
            )

            // Input: Jumlah Pemasukan
            FormInput(
                label = "Jumlah Pemasukan",
                value = jumlahPemasukan,
                onValueChange = { jumlahPemasukan = it },
                placeholder = "Masukkan nominal pemasukan",
                keyboardType = KeyboardType.Number,
                trailingIcon = {
                    Text(
                        text = "Rp",
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )

            // Input: Bukti Pemasukan
            Text(
                text = "Bukti Pemasukan",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(
                onClick = { /* TODO: Aksi untuk upload bukti */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0EFED),
                    contentColor = hijau30
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FileUpload,
                        contentDescription = "Upload Icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Upload")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Simpan
            Button(
                onClick = { /* TODO: Aksi untuk menyimpan data */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = hijau30),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(50.dp)
            ) {
                Text(text = "Simpan", fontSize = 16.sp)
            }
        }
    }
}

// Composable terpisah untuk input form agar kode tidak berulang
@Composable
private fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = hijau30,
                focusedBorderColor = hijau30,
                cursorColor = hijau30
            )
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun TambahPemasukanScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        TambahPemasukanScreen(navController = rememberNavController())
    }
}

