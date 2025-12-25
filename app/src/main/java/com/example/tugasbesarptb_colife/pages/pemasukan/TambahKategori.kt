package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.util.formatRupiah
import com.example.tugasbesarptb_colife.viewmodel.KategoriViewModel
import com.example.tugasbesarptb_colife.viewmodel.PemasukanViewModel
import com.example.tugasbesarptb_colife.viewmodel.PengeluaranViewModel


data class RingkasanData(
    val jumlahUang: String,
    val totalPengeluaran: String,
    val sisaUang: String
)

data class KategoriSummary(
    val nama: String,
    val target: Long,
    val total: Long,
    val warna: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryPengeluaranScreen(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val pemasukanViewModel: PemasukanViewModel = viewModel()
    val pengeluaranViewModel: PengeluaranViewModel = viewModel()
    val kategoriViewModel: KategoriViewModel = viewModel()

    val pemasukanList by pemasukanViewModel.allPemasukan.observeAsState(initial = emptyList())
    val pengeluaranList by pengeluaranViewModel.allPengeluaran.observeAsState(initial = emptyList())
    val kategoriList by kategoriViewModel.allKategori.observeAsState(initial = emptyList())

    // Calculate total pemasukan
    val totalPemasukan = pemasukanList.sumOf { it.jumlah.toLongOrNull() ?: 0L }

    // Calculate total pengeluaran
    val totalPengeluaran = pengeluaranList.sumOf { it.jumlah }

    val sisaUang = totalPemasukan - totalPengeluaran

    val dataRingkasan = RingkasanData(
        jumlahUang = formatRupiah(totalPemasukan),
        totalPengeluaran = formatRupiah(totalPengeluaran),
        sisaUang = formatRupiah(sisaUang)
    )

    val kategoriSummaryList = kategoriList.map { kategori ->
        val totalPerKategori = pengeluaranList
            .filter { it.kategoriId == kategori.id }
            .sumOf { it.jumlah }
        KategoriSummary(
            nama = kategori.nama,
            target = kategori.target,
            total = totalPerKategori,
            warna = Color(kategori.warna)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Summary Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        containerColor = Color(0xFFF0F4F3)
    ) { innerPadding ->
        // Wrap content in a Column and apply padding here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp) // Only horizontal padding for content
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    RingkasanHeader(data = dataRingkasan)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(kategoriSummaryList) { summary ->
                    KategoriCard(summary = summary)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun RingkasanHeader(data: RingkasanData) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = hijau30)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoRow("Jumlah Uang", data.jumlahUang)
            InfoRow("Total Pengeluaran", data.totalPengeluaran)
            Divider(color = Color.White.copy(alpha = 0.5f))
            InfoRow("Sisa Uang", data.sisaUang)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White, fontSize = 16.sp)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun KategoriCard(summary: KategoriSummary) {
    val sisaBudget = summary.target - summary.total
    val melebihiTarget = sisaBudget < 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(summary.warna))
                Spacer(modifier = Modifier.width(8.dp))
                Text(summary.nama, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                if (melebihiTarget) {
                    Badge(text = "Melebihi target", color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            KategoriInfoRow("Target", formatRupiah(summary.target))
            KategoriInfoRow("Total", formatRupiah(summary.total))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            KategoriInfoRow("Sisa", formatRupiah(sisaBudget), isOver = melebihiTarget)
        }
    }
}

@Composable
fun KategoriInfoRow(label: String, value: String, isOver: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = if(isOver) Color.Red else Color.Black, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun Badge(text: String, color: Color) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(color).padding(horizontal = 8.dp, vertical = 4.dp)){
        Text(text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SummaryPengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        SummaryPengeluaranScreen(navController = rememberNavController())
    }
}