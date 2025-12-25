package com.example.tugasbesarptb_colife.pages.piutang

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tugasbesarptb_colife.SessionManager
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModel
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModelFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.let

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuktiPembayaranScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()

    val repository = remember {
        PiutangRepository(
            piutangDao = AppDatabase.getInstance(context).piutangDao(),
            apiService = ApiClient.instance,
            userId = userId
        )
    }

    val viewModel: PiutangViewModel = viewModel(
        factory = remember {
            PiutangViewModelFactory(repository)
        }
    )
    val allPiutang by viewModel.allPiutang.observeAsState(emptyList())
    val piutangSelesai = allPiutang.filter { it.selesai }

    // Map untuk menyimpan URI sementara, gunakan Long sebagai key
    val buktiPembayaran = remember { mutableStateMapOf<Long, Uri?>() }

    var selectedPiutangForImage by remember { mutableStateOf<Piutang?>(null) }
    var selectedImageFullScreen by remember { mutableStateOf<Uri?>(null) }

    // Launcher untuk chooser kamera + galeri
    val chooserLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val dataIntent = result.data
        val bitmap: Bitmap? = dataIntent?.extras?.get("data") as? Bitmap
        val uri: Uri? = dataIntent?.data ?: bitmap?.let { saveBitmapToCache(context, it) }

        uri?.let {
            selectedPiutangForImage?.let { piutang ->
                buktiPembayaran[piutang.id] = it // gunakan Long
                viewModel.uploadBukti(piutang, it, context)
            }
        }
    }

    fun openImageChooser(piutang: Piutang) {
        selectedPiutangForImage = piutang
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pickGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickGalleryIntent.type = "image/*"

        val chooser = Intent.createChooser(pickGalleryIntent, "Pilih Kamera atau Galeri")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePhotoIntent))
        chooserLauncher.launch(chooser)
    }

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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "Piutang yang Sudah Selesai",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (piutangSelesai.isEmpty()) {
                    Text("Belum ada piutang selesai", color = Color.Gray)
                } else {
                    LazyColumn {
                        items(piutangSelesai) { item ->
                            val uriFromDb = item.buktiPembayaranUri?.let { Uri.parse(it) }
                            val currentUri = buktiPembayaran[item.id] ?: uriFromDb

                            BuktiPembayaranCard(
                                piutang = item,
                                imageUri = currentUri,
                                onCameraClick = { openImageChooser(item) },
                                onViewImage = { selectedImageFullScreen = it }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            selectedImageFullScreen?.let { uri ->
                FullScreenImageScreen(navController = navController, imageUri = uri) {
                    selectedImageFullScreen = null
                }
            }
        }
    }
}

@Composable
fun BuktiPembayaranCard(
    piutang: Piutang,
    imageUri: Uri?,
    onCameraClick: () -> Unit,
    onViewImage: (Uri) -> Unit
) {
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
                    Text(piutang.tanggalSelesai ?: "-")
                    Text(piutang.jumlah.toString(), fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = onCameraClick) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Pilih Gambar")
                }
            }

            imageUri?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Bukti Pembayaran",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .clickable { onViewImage(it) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun FullScreenImageScreen(
    navController: NavController,
    imageUri: Uri,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "Gambar Full Screen",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val file = File(context.cacheDir, "bukti_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        Uri.fromFile(file)
    } catch (e: Exception) {
        Log.e("SaveBitmap", "Gagal menyimpan: ${e.message}")
        null
    }
}
