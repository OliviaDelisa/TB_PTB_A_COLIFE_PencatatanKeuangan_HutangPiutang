package com.example.tugasbesarptb_colife.pages

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.model.HutangItem
import com.example.tugasbesarptb_colife.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun HutangScreen(navController: NavHostController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var hutangList by remember { mutableStateOf<List<HutangItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // =====================================================
    //  REFRESH jika ada perubahan dari Edit / Mark Done
    // =====================================================
    val refresh = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Boolean>("refresh", false)
        ?.collectAsState()

    LaunchedEffect(refresh?.value) {
        if (refresh?.value == true) {
            loadHutangData(
                onSuccess = { hutangList = it },
                onError = { error = it },
                setLoading = { loading = it }
            )

            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", false)
        }
    }

    // =====================================================
    //  LOAD DATA PERTAMA KALI
    // =====================================================
    LaunchedEffect(true) {
        loadHutangData(
            onSuccess = { hutangList = it },
            onError = { error = it },
            setLoading = { loading = it }
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController, "hutang") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("tambahhutang") },
                containerColor = Color(0xFF4A9C90),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { inner ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {

            // ================= TITLE =================
            Text(
                "List Hutang",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            // ================= HISTORY BUTTON =================
            IconButton(
                onClick = { navController.navigate("historyhutang") },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = "History")
            }

            // ================= LOADING =================
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // ================= ERROR =================
            else if (error != null) {
                Text(error ?: "", modifier = Modifier.align(Alignment.Center))
            }

            // ================= EMPTY =================
            else if (hutangList.isEmpty()) {
                Text(
                    "Belum ada hutang",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // ================= LIST =================
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp)
                ) {

                    items(hutangList) { item ->

                        HutangCard(
                            item = item,

                            // =====================================================
                            //  ✔ SUCCESS (mark as done)
                            // =====================================================
                            onMarkDone = {
                                scope.launch {
                                    try {
                                        val res = ApiClient.instance.selesaiHutang(item.id)

                                        if (res.isSuccessful && res.body()?.success == true) {

                                            // A. HAPUS dari list utama
                                            hutangList = hutangList.filter { it.id != item.id }

                                            // B. KIRIM ke history
                                            navController.currentBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("pushHistory", item)

                                            Toast.makeText(context, "Hutang selesai!", Toast.LENGTH_SHORT).show()

                                        } else {
                                            Toast.makeText(context, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                                        }

                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },

                            // =====================================================
                            //  ✏ EDIT
                            // =====================================================
                            onEdit = {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("editData", item)

                                navController.navigate("edithutang")
                            },

                            // =====================================================
                            //  🗑 DELETE
                            // =====================================================
                            onDelete = {
                                scope.launch {
                                    try {
                                        val res = ApiClient.instance.deleteHutang(item.id)
                                        if (res.isSuccessful && res.body()?.success == true) {
                                            hutangList = hutangList.filter { it.id != item.id }
                                        } else {
                                            error = "Gagal menghapus data"
                                        }
                                    } catch (e: Exception) {
                                        error = "Tidak dapat terhubung ke server"
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// =====================================================
//  FUNCTION: load data
// =====================================================
suspend fun loadHutangData(
    onSuccess: (List<HutangItem>) -> Unit,
    onError: (String) -> Unit,
    setLoading: (Boolean) -> Unit
) {
    setLoading(true)

    try {
        val res = ApiClient.instance.getHutang()
        if (res.isSuccessful && res.body()?.success == true) {
            onSuccess(res.body()?.data ?: emptyList())
        } else {
            onError("Gagal memuat data")
        }
    } catch (e: Exception) {
        onError("Tidak dapat terhubung ke server")
    }

    setLoading(false)
}
