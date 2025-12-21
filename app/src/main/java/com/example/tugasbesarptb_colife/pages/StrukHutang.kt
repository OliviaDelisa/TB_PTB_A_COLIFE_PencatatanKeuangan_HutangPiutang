package com.example.tugasbesarptb_colife.pages

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import java.io.File

@Composable
fun StrukHutangScreen(
    navController: NavHostController
) {
    // ================= PERUBAHAN UTAMA (WAJIB) =================
    val backStackEntry by navController.currentBackStackEntryAsState()
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry("hutang")
    }
    val viewModel: StrukHutangViewModel = viewModel(parentEntry)
    // ===========================================================

    TugasBesarPTB_COLIFETheme {

        val context = LocalContext.current
        var showMenu by remember { mutableStateOf(false) }

        // DELETE STATE
        var showDeleteDialog by remember { mutableStateOf(false) }
        var selectedUri by remember { mutableStateOf<Uri?>(null) }

        // CAMERA URI
        var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

        // ===== GALLERY =====
        val galleryLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let { viewModel.add(it) }
            }

        // ===== CAMERA =====
        val cameraLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture()
            ) { success ->
                if (success) {
                    cameraImageUri?.let { viewModel.add(it) }
                }
            }

        val cameraPermissionLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    val file = File.createTempFile("struk_", ".jpg", context.cacheDir)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    cameraImageUri = uri
                    cameraLauncher.launch(uri)
                }
            }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // ================= JUDUL =================
            Text(
                text = "Bukti Struk Hutang",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            // ================= KONTEN =================
            if (viewModel.strukList.isEmpty()) {
                Text(
                    text = "Belum ada struk hutang",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.strukList) { uri ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                )

                                // ===== DELETE BUTTON (TIDAK DIUBAH) =====
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectedUri = uri
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ================= POPUP MENU =================
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color(0xFF4A9C90), RoundedCornerShape(20.dp))
                        .padding(15.dp)
                ) {

                    // ===== AMBIL FOTO =====
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showMenu = false
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PhotoCamera, null, tint = Color.White)
                        Spacer(Modifier.width(15.dp))
                        Text("Ambil foto", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ===== BUKA PENYIMPANAN =====
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showMenu = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Folder, null, tint = Color.White)
                        Spacer(Modifier.width(15.dp))
                        Text("Buka Penyimpanan", color = Color.White)
                    }
                }
            }

            // ================= FAB =================
            FloatingActionButton(
                onClick = { showMenu = !showMenu },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(30.dp),
                containerColor = Color(0xFF4A9C90)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }

            // ================= DIALOG DELETE =================
            if (showDeleteDialog && selectedUri != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        selectedUri = null
                    },
                    title = { Text("Hapus Struk") },
                    text = { Text("Apakah yakin ingin menghapus struk ini?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.delete(selectedUri!!)
                                selectedUri = null
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Hapus", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                selectedUri = null
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}