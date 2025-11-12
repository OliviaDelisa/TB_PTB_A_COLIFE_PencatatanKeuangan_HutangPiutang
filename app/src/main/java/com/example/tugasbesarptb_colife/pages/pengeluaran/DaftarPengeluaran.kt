package com.example.tugasbesarptb_colife.pages.pengeluaran

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.components.BottomNavBar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.tugasbesarptb_colife.ui.theme.hijau70

data class Pengeluaran(
    val nama: String,
    val tanggal: String,
    val jumlah: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPengeluaranScreen(navController: NavController, pengeluaranList: List<Pengeluaran>) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
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
                    containerColor = Color.White, // Latar belakang TopAppBar
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("tambahpengeluaran") },
                containerColor = hijau30,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.White
    ) { innerPadding ->
        if (pengeluaranList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada Pengeluaran",
                    color = hijau30,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pengeluaranList) { pengeluaran ->
                    PengeluaranCard(pengeluaran)
                }
            }
        }
    }
}

@Composable
fun PengeluaranCard(pengeluaran: Pengeluaran) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = hijau70)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = pengeluaran.nama, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = pengeluaran.jumlah, fontSize = 16.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = pengeluaran.tanggal, fontSize = 12.sp)
                Row {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PengeluaranScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        DaftarPengeluaranScreen(navController = rememberNavController(), pengeluaranList = listOf(
            Pengeluaran("Makanan", "19/10/25", "Rp30.000.000"),
            Pengeluaran("Minuman", "30/10/25", "Rp3.300.000"),
        ))
    }
}