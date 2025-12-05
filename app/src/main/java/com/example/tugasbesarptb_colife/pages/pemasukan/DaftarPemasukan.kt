package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30

// Asumsi Anda punya file NavRoutes.kt untuk rute yang aman
object NavRoutes {
    const val DAFTAR_PEMASUKAN = "daftarpemasukan"
    const val TAMBAH_PEMASUKAN = "tambahpemasukan"
    const val TAMBAH_KATEGORI = "tambahkategori" // Rute baru untuk kategori
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPemasukanScreen(navController: NavController) {

    // State untuk mengetahui rute saat ini, berguna untuk BottomNavBar
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // State untuk mengontrol apakah menu FAB sedang terbuka atau tertutup
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pemasukan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Aksi notifikasi */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            // Memanggil BottomNavBar dari file terpisah (components)
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        // floatingActionButton TIDAK DISET di sini, kita akan menempatkannya secara manual
        containerColor = Color.White
    ) { innerPadding ->

        // Box digunakan agar kita bisa menumpuk konten utama dengan FAB di atasnya
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Konten utama halaman (teks "Belum ada Pemasukan")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada Pemasukan",
                    color = hijau30,
                    fontSize = 18.sp
                )
            }

            // --- Bagian FAB yang Diperbarui ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Tombol Pilihan yang akan muncul dan hilang dengan animasi
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it }
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        OptionButton(
                            text = "Kategori",
                            onClick = {
                                navController.navigate(NavRoutes.TAMBAH_KATEGORI)
                                isExpanded = false
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OptionButton(
                            text = "Pemasukan",
                            onClick = {
                                navController.navigate(NavRoutes.TAMBAH_PEMASUKAN)
                                isExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // FAB utama yang mengontrol ekspansi
                FloatingActionButton(
                    onClick = { isExpanded = !isExpanded },
                    containerColor = hijau30,
                    contentColor = Color.White,
                    shape = CircleShape,
                ) {
                    // Animasi rotasi untuk ikon + menjadi x
                    val rotation by animateFloatAsState(targetValue = if (isExpanded) 45f else 0f)
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
        }
    }
}

// Composable baru untuk tombol pilihan ("Kategori" & "Pemasukan")
@Composable
private fun OptionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = hijau30)
    ) {
        Text(text = text, color = Color.White)
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun DaftarPemasukanScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        DaftarPemasukanScreen(navController = rememberNavController())
    }
}