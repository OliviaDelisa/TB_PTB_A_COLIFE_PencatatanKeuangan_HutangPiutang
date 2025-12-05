package com.example.tugasbesarptb_colife.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.model.HutangItem
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

@Composable
fun HistoryHutangScreen(navController: NavHostController) {

    TugasBesarPTB_COLIFETheme {

        // =========================================================
        //  LIST HISTORY (disimpan agar tidak hilang)
        // =========================================================
        var historyList by remember { mutableStateOf<List<HutangItem>>(emptyList()) }

        // =========================================================
        //  AMBIL DATA YANG DIPUSH DARI HutangScreen (pushHistory)
        //  setiap kali ada data baru → tambah ke historyList
        // =========================================================
        val pushedItem = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<HutangItem?>("pushHistory", null)
            ?.collectAsState()

        LaunchedEffect(pushedItem?.value) {
            pushedItem?.value?.let { newItem ->
                historyList = historyList + newItem

                // reset agar tidak menambah lagi
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("pushHistory", null)
            }
        }

        // =========================================================
        //  UI START
        // =========================================================
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // ------------------ TITLE ------------------
            Text(
                text = "History Hutang",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            // ------------------ EMPTY STATE ------------------
            if (historyList.isEmpty()) {
                Text(
                    text = "Belum ada history hutang",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // ------------------ LIST HISTORY ------------------
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp)
                ) {
                    items(historyList) { item ->
                        HistoryCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HutangItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.nama, style = MaterialTheme.typography.bodyLarge)
            Text("Tanggal: ${item.tanggal}", color = Color.Gray)
            Text("Jumlah: Rp${item.jumlah}", fontWeight = FontWeight.Bold)
        }
    }
}
