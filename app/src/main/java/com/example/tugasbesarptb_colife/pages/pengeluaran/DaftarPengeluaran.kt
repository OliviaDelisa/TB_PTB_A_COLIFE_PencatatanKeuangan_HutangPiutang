package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranWithKategori
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.util.formatRupiah
import com.example.tugasbesarptb_colife.viewmodel.PengeluaranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPengeluaranScreen(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val pengeluaranViewModel: PengeluaranViewModel = viewModel()
    val pengeluaranList by pengeluaranViewModel.allPengeluaran.observeAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf<PengeluaranWithKategori?>(null) }

    Scaffold(
        topBar = {   TopAppBar(
            title = {
                Text(
                    text = "Pengeluaran",
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
        ) },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("tambahpengeluaran") },
                containerColor = hijau30,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, "Tambah Pengeluaran")
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = { navController.navigate("summarypengeluaran") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = hijau30)
            ) {
                Text("Summary", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
            }

            if (pengeluaranList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada Pengeluaran", color = hijau30, fontSize = 18.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(pengeluaranList) { pengeluaran ->
                        PengeluaranCard(
                            pengeluaran = pengeluaran,
                            onDelete = { showDeleteDialog = pengeluaran },
                            onEdit = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("pengeluaranId", pengeluaran.id)
                                navController.navigate("editpengeluaran")
                            }
                        )
                    }
                }
            }
        }

        showDeleteDialog?.let { pengeluaranToDelete ->
            DeleteConfirmationDialog(
                onConfirm = {
                    val pengeluaranEntity = Pengeluaran(id = pengeluaranToDelete.id, nama = pengeluaranToDelete.nama, jumlah = pengeluaranToDelete.jumlah, tanggal = pengeluaranToDelete.tanggal, kategoriId = pengeluaranToDelete.kategoriId)
                    pengeluaranViewModel.delete(pengeluaranEntity)
                    showDeleteDialog = null
                },
                onDismiss = { showDeleteDialog = null }
            )
        }
    }
}

@Composable
fun PengeluaranCard(pengeluaran: PengeluaranWithKategori, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(pengeluaran.warnaKategori).copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(pengeluaran.nama, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(pengeluaran.namaKategori, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(formatRupiah(pengeluaran.jumlah), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(pengeluaran.tanggal, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    IconButton(onClick = onEdit, modifier=Modifier.size(24.dp)) { Icon(Icons.Default.Edit, "Edit") }
                    IconButton(onClick = onDelete, modifier=Modifier.size(24.dp)) { Icon(Icons.Default.Delete, "Delete", tint = Color.Red) }
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Apakah anda yakin akan menghapus daftar ini?", fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = onDismiss, shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = hijau30), border = BorderStroke(1.dp, hijau30), modifier = Modifier.weight(1f)) { Text("Batalkan") }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onConfirm, shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White), modifier = Modifier.weight(1f)) { Text("Hapus") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DaftarPengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        // Preview tidak bisa menampilkan data dari ViewModel, ini hanya untuk UI statis
        DaftarPengeluaranScreen(navController = rememberNavController())
    }
}
