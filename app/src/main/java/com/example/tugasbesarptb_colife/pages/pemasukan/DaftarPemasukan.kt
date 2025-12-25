package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.util.formatRupiah // Import fungsi terpusat
import com.example.tugasbesarptb_colife.viewmodel.PemasukanViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPemasukanScreen(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Pemasukan?>(null) }

    val pemasukanViewModel: PemasukanViewModel = viewModel()
    val pemasukanList by pemasukanViewModel.allPemasukan.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemasukan", fontWeight = FontWeight.Bold, fontSize = 24.sp) },
                actions = {
                    IconButton(onClick = { /* TODO: Notifikasi */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifikasi")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F4F3),
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        containerColor = Color(0xFFF0F4F3)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (pemasukanList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada Pemasukan", color = hijau30, fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(pemasukanList) { pemasukan ->
                        PemasukanItem(
                            pemasukan = pemasukan,
                            onEditClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("pemasukanId", pemasukan.id)
                                navController.navigate("editpemasukan")
                            },
                            onDeleteClick = { showDeleteDialog = pemasukan }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // FAB Column
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
                        OptionButton(text = "Kategori") {
                            navController.navigate("tambahkategori")
                            isExpanded = false
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OptionButton(text = "Pemasukan") {
                            navController.navigate("tambahpemasukan")
                            isExpanded = false
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = { isExpanded = !isExpanded },
                    containerColor = hijau30,
                    contentColor = Color.White,
                    shape = CircleShape,
                ) {
                    val rotation by animateFloatAsState(targetValue = if (isExpanded) 45f else 0f, label = "")
                    Icon(Icons.Default.Add, "Tambah", modifier = Modifier.rotate(rotation))
                }
            }

            // Dialog Konfirmasi Hapus
            showDeleteDialog?.let {
                DeleteConfirmationDialog(
                    pemasukan = it,
                    onConfirm = {
                        pemasukanViewModel.delete(it)
                        showDeleteDialog = null
                    },
                    onDismiss = { showDeleteDialog = null }
                )
            }
        }
    }
}

@Composable
fun PemasukanItem(
    pemasukan: Pemasukan,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDDE8E4))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = pemasukan.sumber, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRupiah(pemasukan.jumlah.toLongOrNull() ?: 0L),
                    color = Color.Black, 
                    fontWeight = FontWeight.SemiBold, 
                    fontSize = 16.sp
                )
            }
            Text(text = pemasukan.tanggal, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Pemasukan", tint = Color.Black)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus Pemasukan", tint = Color.Red)
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(pemasukan: Pemasukan, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Apakah anda yakin akan menghapus daftar ini?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = hijau30),
                        border = BorderStroke(1.dp, hijau30),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batalkan")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Hapus")
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
