package com.example.tugasbesarptb_colife.pages.hutang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.model.HutangItem
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

@Composable
fun HistoryHutangScreen(navController: NavHostController) {

    var historyList by remember { mutableStateOf<List<HutangItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.instance.getHistoryHutang()
            if (response.isSuccessful) {
                historyList = response.body()?.data ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    TugasBesarPTB_COLIFETheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Text(
                text = "History Hutang",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                historyList.isEmpty() -> {
                    Text(
                        text = "Belum ada history hutang",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
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
}

@Composable
fun HistoryCard(item: HutangItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.nama, fontWeight = FontWeight.Bold)
            Text("Tanggal: ${item.tanggal}", color = Color.Gray)
            Text("Jumlah: Rp${item.jumlah}")
        }
    }
}