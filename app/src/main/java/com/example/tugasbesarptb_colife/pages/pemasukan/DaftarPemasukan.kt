package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.viewmodel.PemasukanViewModel

object NavRoutes {
    const val DAFTAR_PEMASUKAN = "daftarpemasukan"
    const val TAMBAH_PEMASUKAN = "tambahpemasukan"
    const val TAMBAH_KATEGORI = "tambahkategori"
    const val EDIT_PEMASUKAN = "editpemasukan" // Rute baru untuk edit
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPemasukanScreen(navController: NavController) {

    val pemasukanViewModel: PemasukanViewModel = viewModel()
    val allPemasukan by pemasukanViewModel.allPemasukan.observeAsState(initial = emptyList())

    val currentRoute = navController.currentBackStackEntry?.destination?.route
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
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (allPemasukan.isEmpty()) {
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
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(allPemasukan) { pemasukan ->
                        PemasukanItem(
                            pemasukan = pemasukan,
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("pemasukanId", pemasukan.id)
                                navController.navigate(NavRoutes.EDIT_PEMASUKAN)
                            },
                            onDelete = { pemasukanViewModel.delete(pemasukan) }
                        )
                    }
                }
            }

            // --- Bagian FAB yang Diperbarui ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
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

                FloatingActionButton(
                    onClick = { isExpanded = !isExpanded },
                    containerColor = hijau30,
                    contentColor = Color.White,
                    shape = CircleShape,
                ) {
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

@Composable
fun PemasukanItem(pemasukan: Pemasukan, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            pemasukan.fotoUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Gambar Pemasukan",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pemasukan.sumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pemasukan.tanggal,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Rp ${pemasukan.jumlah}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = hijau30
                    )
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                    }
                }
            }
        }
    }
}


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
