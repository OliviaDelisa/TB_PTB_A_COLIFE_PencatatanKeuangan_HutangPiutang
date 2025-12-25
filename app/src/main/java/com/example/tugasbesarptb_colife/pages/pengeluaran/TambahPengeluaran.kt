package com.example.tugasbesarptb_colife.pages.pengeluaran

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import java.io.File

@Composable
fun TambahPengeluaranScreen(
    navController: NavController,
    onAddPengeluaran: (Pengeluaran) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var bukaTanggalPicker by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // Image captured successfully, URI is already set
            }
        }
    )

    // DATE PICKER
    TanggalPicker(
        buka = bukaTanggalPicker,
        saatTutup = { bukaTanggalPicker = false },
        saatDipilih = { tanggal = it }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Pilih Sumber Gambar") },
            text = { Text("Pilih dari galeri atau ambil foto baru.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Text("Galeri")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        val file = File(context.cacheDir, "camera_photo.jpg")
                        val uri = FileProvider.getUriForFile(context, "com.example.tugasbesarptb_colife.provider", file)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    }
                ) {
                    Text("Kamera")
                }
            }
        )
    }

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
            
            if (imageUri != null) {
                Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

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

            Spacer(Modifier.height(16.dp))

            Text("Kategori Pengeluaran")
            OutlinedTextField(
                value = kategori,
                onValueChange = { kategori = it },
                placeholder = { Text("Masukkan kategori") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = { showDialog = true },
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
                            Pengeluaran(
                                nama = nama, 
                                tanggal = tanggal, 
                                jumlah = jumlah, 
                                kategori = kategori,
                                fotoUri = imageUri?.toString()
                            )
                        )
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
