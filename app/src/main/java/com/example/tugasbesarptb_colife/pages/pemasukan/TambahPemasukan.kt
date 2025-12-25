package com.example.tugasbesarptb_colife.pages.pemasukan

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.viewmodel.PemasukanViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TambahPemasukanScreen(navController: NavController) {

    val pemasukanViewModel: PemasukanViewModel = viewModel()
    val context = LocalContext.current

    var sumberPemasukan by remember { mutableStateOf("") }
    var tanggalPemasukan by remember { mutableStateOf("") }
    var jumlahPemasukan by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val isFormValid by remember(sumberPemasukan, tanggalPemasukan, jumlahPemasukan) {
        derivedStateOf {
            sumberPemasukan.isNotBlank() && tanggalPemasukan.isNotBlank() && jumlahPemasukan.isNotBlank()
        }
    }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambahkan Pemasukan", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            FormInput(
                label = "Sumber Pemasukan",
                value = sumberPemasukan,
                onValueChange = { sumberPemasukan = it },
                placeholder = "Masukkan sumber pemasukan"
            )

            TanggalInput(label = "Tanggal Pemasukan", value = tanggalPemasukan, onClick = { showDatePicker = true })

            FormInput(
                label = "Jumlah Pemasukan",
                value = jumlahPemasukan,
                onValueChange = { jumlahPemasukan = it },
                placeholder = "Masukkan nominal pemasukan",
                keyboardType = KeyboardType.Number,
                trailingIcon = { Text("Rp", color = Color.Gray, modifier = Modifier.padding(end = 8.dp)) }
            )

            Text("Bukti Pemasukan", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
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
                Box(modifier = Modifier.fillMaxWidth().height(300.dp).padding(top = 16.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Gambar Pemasukan",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val pemasukan = Pemasukan(
                        sumber = sumberPemasukan,
                        tanggal = tanggalPemasukan,
                        jumlah = jumlahPemasukan,
                        fotoUri = imageUri?.toString()
                    )
                    pemasukanViewModel.insert(pemasukan)
                    navController.popBackStack()
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = hijau30),
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp).height(50.dp)
            ) {
                Text(text = "Simpan", fontSize = 16.sp)
            }
        }

        if (showDatePicker) {
            TanggalPicker(buka = true, saatTutup = { showDatePicker = false }, saatDipilih = { tanggalPemasukan = it })
        }
    }
}

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
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = hijau30, focusedBorderColor = hijau30, cursorColor = hijau30)
        )
    }
}

@Composable
private fun TanggalInput(label: String, value: String, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
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

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun TambahPemasukanScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        TambahPemasukanScreen(navController = rememberNavController())
    }
}
