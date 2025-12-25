package com.example.tugasbesarptb_colife.pages.hutang

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.tugasbesarptb_colife.FileUtil
import com.example.tugasbesarptb_colife.model.GambarHutang
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Composable
fun StrukHutangScreen(
    navController: NavHostController
) {

    val BASE_URL_SERVER = "http://10.0.2.2:3000/"


    val currentHutangId = "1" // ID Hutang (Hardcode sementara)

    // Setup ViewModel & Navigasi
    val backStackEntry by navController.currentBackStackEntryAsState()
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry("hutang")
    }

    // State Setup
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Data dari Server
    var listGambarServer by remember { mutableStateOf<List<GambarHutang>>(emptyList()) }

    // Loading & UI States
    var isLoading by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedIdToDelete by remember { mutableStateOf<Int?>(null) }

    // ================= FUNGSI LOGIKA =================

    // 1. REFRESH (Ambil data)
    fun refreshData() {
        scope.launch {
            isLoading = true
            try {
                val api = ApiClient.instance
                val response = api.getGambarHutang(currentHutangId)

                if (response.isSuccessful && response.body()?.success == true) {
                    listGambarServer = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal load gambar: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    // 2. UPLOAD
    fun uploadGambar(uri: Uri) {
        scope.launch {
            isUploading = true
            val file = FileUtil.getFileFromUri(context, uri)

            if (file != null) {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val bodyImage = MultipartBody.Part.createFormData("image", file.name, requestFile)
                val bodyId = currentHutangId.toRequestBody("text/plain".toMediaTypeOrNull())

                try {
                    val api = ApiClient.instance
                    val response = api.uploadBuktiPermanen(bodyId, bodyImage)

                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(context, "Berhasil diupload!", Toast.LENGTH_SHORT).show()
                        refreshData()
                    } else {
                        Toast.makeText(context, "Gagal upload: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            isUploading = false
        }
    }

    // 3. HAPUS
    fun hapusGambar(idGambar: Int) {
        scope.launch {
            try {
                val api = ApiClient.instance
                val response = api.deleteGambarBukti(idGambar)

                if (response.isSuccessful) {
                    Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                    refreshData()
                } else {
                    Toast.makeText(context, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Auto-Load saat pertama buka
    LaunchedEffect(Unit) {
        refreshData()
    }

    // Launchers (Kamera & Galeri)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { uploadGambar(it) } }

    var cameraUriTemp by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) { cameraUriTemp?.let { uploadGambar(it) } } }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File.createTempFile("struk_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            cameraUriTemp = uri
            cameraLauncher.launch(uri)
        }
    }

    // ================= TAMPILAN UI =================
    TugasBesarPTB_COLIFETheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // JUDUL
            Text(
                text = "Bukti Struk Hutang",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            // LIST GAMBAR
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (listGambarServer.isEmpty()) {
                Text("Belum ada struk tersimpan", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(listGambarServer) { gambar ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                // === INI PERBAIKANNYA ===
                                // Kita gabungkan Alamat Server + Nama File
                                // Contoh jadi: http://192.168.1.8:3000/uploads/foto.jpg
                                val fullUrl = BASE_URL_SERVER + gambar.imageUrl

                                Image(
                                    painter = rememberAsyncImagePainter(fullUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .background(Color.LightGray),
                                    contentScale = ContentScale.Crop
                                )

                                // Tombol Hapus
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectedIdToDelete = gambar.id
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Loading Upload
            if (isUploading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha=0.5f)).clickable(enabled=false){},
                    contentAlignment = Alignment.Center
                ) {
                    Card(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator()
                            Spacer(Modifier.width(15.dp))
                            Text("Sedang mengupload...")
                        }
                    }
                }
            }

            // Popup Menu
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 30.dp, bottom = 100.dp)
            ) {
                Column(modifier = Modifier.background(Color(0xFF4A9C90), RoundedCornerShape(20.dp)).padding(15.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().clickable { showMenu = false; cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhotoCamera, null, tint = Color.White); Spacer(Modifier.width(15.dp)); Text("Ambil foto", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth().clickable { showMenu = false; galleryLauncher.launch("image/*") }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Folder, null, tint = Color.White); Spacer(Modifier.width(15.dp)); Text("Buka Penyimpanan", color = Color.White)
                    }
                }
            }

            // FAB Tambah
            FloatingActionButton(
                onClick = { showMenu = !showMenu },
                modifier = Modifier.align(Alignment.BottomEnd).padding(30.dp),
                containerColor = Color(0xFF4A9C90)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }

            // Dialog Hapus
            if (showDeleteDialog && selectedIdToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false; selectedIdToDelete = null },
                    title = { Text("Hapus Struk") },
                    text = { Text("Yakin mau hapus struk ini permanen dari database?") },
                    confirmButton = {
                        TextButton(onClick = { hapusGambar(selectedIdToDelete!!); selectedIdToDelete = null; showDeleteDialog = false }) {
                            Text("Hapus", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { selectedIdToDelete = null; showDeleteDialog = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}