package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.R
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30

// Warna dari gambar
val hijauTua = Color(0xFF5D9B7C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahKategoriPengeluaranScreen(navController: NavController) {

    // State untuk menyimpan nilai dari setiap input
    var kategori by remember { mutableStateOf("") }
    var targetPengeluaran by remember { mutableStateOf("") }
    var showColorPicker by remember { mutableStateOf(false) }

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tambah Kategori Pengeluaran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Spacer untuk menyeimbangkan judul di tengah
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Input Kategori
            CustomOutlinedTextField(
                value = kategori,
                onValueChange = { kategori = it },
                placeholder = "Masukkan Kategori"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Target Pengeluaran
            CustomOutlinedTextField(
                value = targetPengeluaran,
                onValueChange = { targetPengeluaran = it },
                placeholder = "Masukkan Target Pengeluaran",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Text("Rp", color = Color.Gray, modifier = Modifier.padding(end = 12.dp))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Pilih Warna
            Button(
                onClick = { showColorPicker = true },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, hijau30),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    // Ikon warna gradasi
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color.Red, Color.Yellow, Color.Green, Color.Cyan,
                                        Color.Blue, Color.Magenta, Color.Red
                                    )
                                )
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Pilih Warna")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Tambah
            Button(
                onClick = { /* TODO: Aksi untuk menambah kategori */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = hijauTua),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .align(Alignment.End) // Posisi di kanan
            ) {
                Text(text = "Tambah", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog untuk memilih warna (placeholder)
    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Pilih Warna") },
            text = { Text("Fitur pemilih warna akan diimplementasikan di sini.") },
            confirmButton = {
                Button(onClick = { showColorPicker = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}

// Composable kustom untuk Text Field agar sesuai desain (private agar hanya untuk file ini)
@Composable
private fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray) },
        shape = RoundedCornerShape(50), // Sudut sangat bulat
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = hijau30,
            focusedBorderColor = hijau30,
            cursorColor = hijau30
        )
    )
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun TambahKategoriPengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        TambahKategoriPengeluaranScreen(navController = rememberNavController())
    }
}
