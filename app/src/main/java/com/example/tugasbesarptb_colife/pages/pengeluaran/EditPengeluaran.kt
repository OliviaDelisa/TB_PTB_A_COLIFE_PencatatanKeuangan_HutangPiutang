package com.example.tugasbesarptb_colife.pages.pengeluaran

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.viewmodel.KategoriViewModel
import com.example.tugasbesarptb_colife.viewmodel.PengeluaranViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditPengeluaranScreen(navController: NavController) {
    val pengeluaranId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("pengeluaranId")
    val pengeluaranViewModel: PengeluaranViewModel = viewModel()
    val kategoriViewModel: KategoriViewModel = viewModel()

    if (pengeluaranId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ID Pengeluaran tidak ditemukan.")
        }
        return
    }

    val pengeluaranToEdit by pengeluaranViewModel.getPengeluaranById(pengeluaranId).observeAsState()
    val allKategori by kategoriViewModel.allKategori.observeAsState(initial = emptyList())

    val content = pengeluaranToEdit

    if (content == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        EditPengeluaranContent(
            navController = navController,
            pengeluaran = content,
            listKategori = allKategori,
            viewModel = pengeluaranViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun EditPengeluaranContent(
    navController: NavController,
    pengeluaran: Pengeluaran,
    listKategori: List<KategoriPengeluaran>,
    viewModel: PengeluaranViewModel
) {
    var nama by remember { mutableStateOf(pengeluaran.nama) }
    var jumlah by remember { mutableStateOf(pengeluaran.jumlah.toString()) }
    var tanggal by remember { mutableStateOf(pengeluaran.tanggal) }
    var isKategoriExpanded by remember { mutableStateOf(false) }
    var selectedKategori by remember { mutableStateOf(listKategori.find { it.id == pengeluaran.kategoriId }) }
    var showDatePicker by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(pengeluaran.fotoUri?.let { Uri.parse(it) }) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = tempImageUri
            }
        }
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
        }
    )

    fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(null)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        return FileProvider.getUriForFile(context, "com.example.tugasbesarptb_colife.provider", image)
    }

    LaunchedEffect(pengeluaran, listKategori) {
        nama = pengeluaran.nama
        jumlah = pengeluaran.jumlah.toString()
        tanggal = pengeluaran.tanggal
        imageUri = pengeluaran.fotoUri?.let { Uri.parse(it) }
        if (listKategori.isNotEmpty()){
            selectedKategori = listKategori.find { it.id == pengeluaran.kategoriId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())) { // Make the column scrollable
            FormInput(label = "Nama Pengeluaran", value = nama, onValueChange = { nama = it })
            Spacer(modifier = Modifier.height(16.dp))
            TanggalInput(label = "Tanggal", value = tanggal, onClick = { showDatePicker = true })
            Spacer(modifier = Modifier.height(16.dp))
            FormInput(label = "Jumlah", value = jumlah, onValueChange = { jumlah = it }, keyboardType = KeyboardType.Number, trailingIcon = { Text("Rp") })
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = isKategoriExpanded,
                onExpandedChange = { isKategoriExpanded = !isKategoriExpanded }
            ) {
                OutlinedTextField(
                    value = selectedKategori?.nama ?: "Pilih Kategori",
                    onValueChange = {},
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
                    listKategori.forEach { kategori ->
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
            
            Spacer(modifier = Modifier.height(16.dp))

            // Foto Section
            Text("Foto", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            val newImageUri = createImageUri()
                            tempImageUri = newImageUri
                            takePictureLauncher.launch(newImageUri)
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Ambil Foto")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kamera")
                }
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Pilih dari Galeri")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galeri")
                }
            }

            imageUri?.let {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Increase height to show more of the image
                    .padding(top = 16.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Gambar Pengeluaran",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit // Change to Fit
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        viewModel.delete(pengeluaran)
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Hapus", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        val updatedJumlah = jumlah.toLongOrNull() ?: pengeluaran.jumlah
                        val updatedPengeluaran = pengeluaran.copy(
                            nama = nama,
                            jumlah = updatedJumlah,
                            tanggal = tanggal,
                            kategoriId = selectedKategori?.id ?: pengeluaran.kategoriId,
                            fotoUri = imageUri?.toString()
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
