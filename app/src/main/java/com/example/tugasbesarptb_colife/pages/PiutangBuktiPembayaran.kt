package com.example.tugasbesarptb_colife.pages

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuktiPembayaranScreen(navController: NavController) {
    val savedPiutang = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<Piutang>>("piutangSelesai")

    var daftarPiutang by remember { mutableStateOf(savedPiutang ?: emptyList()) }
    val buktiPembayaran = remember { mutableStateMapOf<String, Uri?>() }

    var selectedImage by remember { mutableStateOf<Uri?>(null) } // untuk dialog lihat bukti

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bukti Pembayaran", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                "Piutang yang Sudah Selesai",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (daftarPiutang.isEmpty()) {
                Text("Belum ada piutang selesai", color = Color.Gray)
            } else {
                LazyColumn {
                    items(daftarPiutang) { item ->
                        BuktiPembayaranCard(
                            piutang = item,
                            imageUri = buktiPembayaran[item.nama],
                            onImageSelected = { uri -> buktiPembayaran[item.nama] = uri },
                            onViewImage = { selectedImage = it }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        if (selectedImage != null) {
            AlertDialog(
                onDismissRequest = { selectedImage = null },
                confirmButton = {
                    TextButton(onClick = { selectedImage = null }) {
                        Text("Tutup")
                    }
                },
                text = {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage),
                        contentDescription = "Bukti Pembayaran",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            )
        }
    }
}

@Composable
fun BuktiPembayaranCard(
    piutang: Piutang,
    imageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    onViewImage: (Uri) -> Unit
) {
    val context = LocalContext.current

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                val uri = saveBitmapToCache(context, bitmap)
                onImageSelected(uri)
            }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9E7E2))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(piutang.nama, fontWeight = FontWeight.Bold)
                    Text(piutang.tanggalTenggat)
                    Text(piutang.jumlah, fontWeight = FontWeight.Medium)
                }

                // Kalau belum upload → tampilkan kamera
                if (imageUri == null) {
                    IconButton(onClick = { cameraLauncher.launch(null) }) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Ambil Foto",
                            tint = Color(0xFF5E8378)
                        )
                    }
                } else {
                    IconButton(onClick = { onViewImage(imageUri) }) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = "Lihat Bukti",
                            tint = Color(0xFF2E554A)
                        )
                    }
                }
            }

            if (imageUri != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Bukti Pembayaran",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val file = File(context.cacheDir, "bukti_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        Uri.parse(file.toURI().toString())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
