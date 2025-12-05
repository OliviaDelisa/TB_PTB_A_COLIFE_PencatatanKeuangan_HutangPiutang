package com.example.tugasbesarptb_colife.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.components.TopBar
import java.text.SimpleDateFormat
import java.util.*

data class Piutang(
    val nama: String,
    val tanggalTenggat: String,
    val jumlah: String,
    val tanggalDibuat: String = getCurrentDate(),
    var tanggalSelesai: String? = null,
    var selesai: Boolean = false
)

fun getCurrentDate(): String {
    val format = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    return format.format(Date())
}

@Composable
fun DaftarPiutang(navController: NavController) {
    var daftarPiutang by remember {
        mutableStateOf(listOf(Piutang("Olivia Delisa", "20/10/25", "Rp40.000")))
    }

    val piutangBaru =
        navController.currentBackStackEntry?.savedStateHandle
            ?.getStateFlow<Triple<String, String, String>?>("piutangBaru", null)
            ?.collectAsState()

    LaunchedEffect(piutangBaru?.value) {
        piutangBaru?.value?.let { (nama, tanggal, jumlah) ->
            daftarPiutang = daftarPiutang + Piutang(nama, tanggal, "Rp$jumlah")
            navController.currentBackStackEntry?.savedStateHandle?.set("piutangBaru", null)
        }
    }

    var fabExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(navController, currentRoute = "hutang") },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp, bottom = 20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (fabExpanded) {
                    Column(
                        modifier = Modifier
                            .widthIn(min = 180.dp, max = 320.dp)
                            .offset(y = (-70).dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp))
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(vertical = 6.dp, horizontal = 8.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    fabExpanded = false
                                    navController.navigate("tambahPiutang")
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.List, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tambahkan Daftar Piutang", fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    fabExpanded = false
                                    val selesaiList = daftarPiutang.filter { it.selesai }
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("piutangSelesai", selesaiList)
                                    navController.navigate("uploadBuktiPembayaran")
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Upload Bukti Pembayaran", fontSize = 14.sp)
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { fabExpanded = !fabExpanded },
                    containerColor = Color(0xFF5E8378),
                    contentColor = Color.White,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah atau Upload")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            val aktif = daftarPiutang.filter { !it.selesai }
            val selesai = daftarPiutang.filter { it.selesai }

            if (aktif.isNotEmpty()) {
                Text(
                    "Dibuat pada ${aktif.firstOrNull()?.tanggalDibuat ?: getCurrentDate()}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(aktif) { item ->
                        PiutangCard(
                            piutang = item,
                            onMarkDone = {
                                val tanggalSelesai = getCurrentDate()
                                daftarPiutang = daftarPiutang.map {
                                    if (it == item) it.copy(selesai = true, tanggalSelesai = tanggalSelesai)
                                    else it
                                }
                            },
                            onEdit = { navController.navigate("editPiutang") },
                            onDelete = { daftarPiutang = daftarPiutang - item },
                            isDone = false
                        )
                    }
                }
            } else {
                Text(
                    "Belum ada daftar piutang",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Ditandai Selesai", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (selesai.isEmpty()) {
                Text(
                    "Belum ada hutang yang ditandai",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    items(selesai) { item ->
                        Column {
                            Text(
                                "Selesai pada ${item.tanggalSelesai ?: getCurrentDate()}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            PiutangCard(
                                piutang = item,
                                onMarkDone = {},
                                onEdit = {},
                                onDelete = {},
                                isDone = true
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PiutangCard(
    piutang: Piutang,
    onMarkDone: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isDone: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFFD3E1DB))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(piutang.nama, fontWeight = FontWeight.Bold)
                Text(piutang.tanggalTenggat, color = Color.Black, fontSize = 13.sp)
            }

            Text(piutang.jumlah, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))

            if (!isDone) {
                IconButton(
                    onClick = onMarkDone,
                    modifier = Modifier.background(Color(0xFF81A696), RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Done, contentDescription = "Tandai selesai", tint = Color.White)
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.background(Color(0xFFECEC71), RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Black)
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.background(Color(0xFFC45A66), RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.White)
                }
            }
        }
    }
}
