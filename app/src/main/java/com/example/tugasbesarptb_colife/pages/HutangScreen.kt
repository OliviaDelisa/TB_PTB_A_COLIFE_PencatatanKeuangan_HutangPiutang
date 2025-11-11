package com.example.tugasbesarptb_colife.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

@Composable
fun HutangScreen(navController: NavHostController) {
    TugasBesarPTB_COLIFETheme {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // ✅ Tombol History (atas kanan)
            IconButton(
                onClick = {
                    navController.navigate("historyhutang")
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            // ✅ Judul
            Text(
                text = "List Hutang",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            // ✅ Text tengah
            Text(
                text = "Belum ada hutang",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )

            // ✅ FAB kiri → Struk Hutang
            FloatingActionButton(
                onClick = {
                    navController.navigate("strukhutang")
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 30.dp, bottom = 30.dp),
                containerColor = Color(0xFF4A9C90),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    contentDescription = "Upload Struk",
                    tint = Color.White
                )
            }

            // ✅ FAB kanan → Tambah Hutang
            FloatingActionButton(
                onClick = {
                    navController.navigate("tambahhutang")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 30.dp),
                containerColor = Color(0xFF4A9C90),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Hutang",
                    tint = Color.White
                )
            }
        }
    }
}
