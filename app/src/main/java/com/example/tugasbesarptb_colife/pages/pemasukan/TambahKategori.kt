package com.example.tugasbesarptb_colife.pages.pemasukan

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.model.KategoriRequest
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahKategoriPengeluaranScreen(navController: NavController) {

    val context = LocalContext.current

    // 1. Tambahkan Scope untuk menjalankan API
    val scope = rememberCoroutineScope()

    var kategori by remember { mutableStateOf("") }
    var targetPengeluaran by remember { mutableStateOf("") }
    var showColorPicker by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }
    var isLoading by remember { mutableStateOf(false) } // Loading state

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Tambahkan Kategori", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
            navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Kembali") } },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            )
        ) },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            FormInput(
                label = "Kategori",
                value = kategori,
                onValueChange = { kategori = it },
                placeholder = "Masukkan Kategori"
            )

            FormInput(
                label = "Target Pengeluaran",
                value = targetPengeluaran,
                onValueChange = { targetPengeluaran = it },
                placeholder = "Masukkan Target Pengeluaran",
                keyboardType = KeyboardType.Number,
                trailingIcon = { Text("Rp", color = Color.Gray, modifier = Modifier.padding(end = 12.dp)) }
            )

            Button(
                onClick = { showColorPicker = true },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, hijau30),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                modifier = Modifier.align(Alignment.Start).height(48.dp).width(180.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(selectedColor ?: hijau30)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Pilih Warna")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // TOMBOL SIMPAN KE SERVER
            Button(
                onClick = {
                    val target = targetPengeluaran.toLongOrNull()
                    val namaKategoriTrim = kategori.trim()

                    when {
                        namaKategoriTrim.isBlank() -> {
                            Toast.makeText(context, "Nama kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                        target == null || target <= 0 -> {
                            Toast.makeText(context, "Target pengeluaran harus angka dan lebih dari 0", Toast.LENGTH_SHORT).show()
                        }
                        selectedColor == null -> {
                            Toast.makeText(context, "Silakan pilih warna untuk kategori", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // --- MULAI KIRIM KE SERVER ---
                            isLoading = true
                            scope.launch {
                                try {
                                    val request = KategoriRequest(
                                        nama = namaKategoriTrim,
                                        target = target,
                                        warna = selectedColor!!.toArgb() // Kirim warna sebagai Int
                                    )

                                    Log.d("API_KATEGORI", "Mengirim: $request")
                                    val response = ApiClient.instance.addKategori(request)

                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Kategori berhasil disimpan ke Server", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Log.e("API_KATEGORI", "Gagal: ${response.code()} - ${response.message()}")
                                        Toast.makeText(context, "Gagal menyimpan: ${response.message()}", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("API_KATEGORI", "Error: ${e.message}")
                                    Toast.makeText(context, "Error koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                            // -----------------------------
                        }
                    }
                },
                enabled = !isLoading, // Disable tombol saat loading
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E8378)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.align(Alignment.End).height(48.dp).width(130.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Tambah", fontSize = 16.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showColorPicker) {
        ColorPickerDialog(
            onColorSelected = {
                selectedColor = it
                showColorPicker = false
            },
            onDismiss = { showColorPicker = false }
        )
    }
}

// Komponen FormInput dan ColorPickerDialog sama seperti sebelumnya (tidak perlu diubah)
@Composable
private fun FormInput(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, keyboardType: KeyboardType = KeyboardType.Text, trailingIcon: @Composable (() -> Unit)? = null) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
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
fun ColorPickerDialog(onColorSelected: (Color) -> Unit, onDismiss: () -> Unit) {
    val colors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFFF5722)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pilih Warna", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { onColorSelected(color) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun TambahKategoriPengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        TambahKategoriPengeluaranScreen(navController = rememberNavController())
    }
}
