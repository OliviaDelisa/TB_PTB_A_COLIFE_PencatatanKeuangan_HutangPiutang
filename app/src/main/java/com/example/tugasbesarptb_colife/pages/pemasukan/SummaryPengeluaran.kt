package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

// Data class untuk merepresentasikan data ringkasan dan item kategori
data class RingkasanData(
    val jumlahUang: String,
    val totalPengeluaran: String,
    val sisaUang: String
)

data class KategoriPengeluaran(
    val nama: String,
    val target: String,
    val total: String,
    val warna: Color,
    val melebihiTarget: Boolean = false
)

// Data contoh untuk ditampilkan di UI
val dataRingkasan = RingkasanData(
    jumlahUang = "Rp 33.300.000",
    totalPengeluaran = "Rp 29.529.000",
    sisaUang = "Rp 3.771.000"
)

val daftarKategori = listOf(
    KategoriPengeluaran("MAKANAN", "Rp 10.000.000", "Rp 9.479.000", Color(0xFF80DEEA)),
    KategoriPengeluaran("TRANSPORTASI", "Rp 5.000.000", "Rp 5.050.000", Color(0xFF81C784), melebihiTarget = true),
    KategoriPengeluaran("SKINCARE & MAKE UP", "Rp 12.000.000", "Rp 12.000.000", Color(0xFFDCE775)),
    KategoriPengeluaran("GAME", "Rp 2.000.000", "Rp 3.000.000", Color(0xFFFFB74D), melebihiTarget = true)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryPengeluaranScreen(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Summary Pengeluaran", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Bagian Ringkasan Atas
            item {
                Spacer(modifier = Modifier.height(8.dp))
                RingkasanHeader(data = dataRingkasan)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Daftar Kategori
            items(daftarKategori) { kategori ->
                KategoriCard(kategori = kategori)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RingkasanHeader(data: RingkasanData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoRow("Jumlah Uang", data.jumlahUang)
        InfoRow("Total Pengeluaran", data.totalPengeluaran)
        InfoRow("Sisa Uang", data.sisaUang)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Surface(
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, hijau30),
            color = Color.White
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.Black,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun KategoriCard(kategori: KategoriPengeluaran) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = kategori.warna),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = kategori.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                if (kategori.melebihiTarget) {
                    Badge(text = "Melebihi Target", color = Color(0xFFD32F2F))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Baris Target dan Total
            KategoriInfoRow(label = "Target", value = kategori.target)
            Spacer(modifier = Modifier.height(8.dp))
            KategoriInfoRow(label = "Total", value = kategori.total)

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol Edit dan Rincian
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO: Aksi Edit*/ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Kategori", tint = Color.Black.copy(alpha = 0.7f))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /*TODO: Aksi lihat rincian*/ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black.copy(alpha = 0.1f),
                        contentColor = Color.Black.copy(alpha = 0.7f)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Rincian", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun KategoriInfoRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier.width(60.dp) // Lebar tetap untuk alignment
        )
        Surface(
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color.White),
            color = Color.Transparent
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = Color.Black.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun Badge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SummaryPengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        SummaryPengeluaranScreen(navController = rememberNavController())
    }
}
