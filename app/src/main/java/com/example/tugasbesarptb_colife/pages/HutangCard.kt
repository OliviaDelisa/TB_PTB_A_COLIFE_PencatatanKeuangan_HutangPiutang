package com.example.tugasbesarptb_colife.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tugasbesarptb_colife.model.HutangItem

@Composable
fun HutangCard(
    item: HutangItem,
    onMarkDone: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDoneDialog by remember { mutableStateOf(false) }

    // ===================== POPUP MARK DONE =====================
    if (showDoneDialog) {
        AlertDialog(
            onDismissRequest = { showDoneDialog = false },
            title = { Text("Hutang Selesai") },
            text = { Text("Apakah kamu yakin hutang ini sudah lunas?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDoneDialog = false
                        onMarkDone()
                    }
                ) {
                    Text("Ya", color = Color(0xFF4CAF50))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDoneDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // ===================== POPUP DELETE =====================
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Hutang") },
            text = { Text("Apakah kamu yakin ingin menghapus hutang ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // ===================== KARTU =====================
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3E3E3))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // ===================== KIRI (Nama + tanggal) =====================
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nama, style = MaterialTheme.typography.bodyLarge)
                Text(text = item.tanggal ?: "-", color = Color.Gray)
            }

            // ===================== TENGAH (Jumlah) =====================
            Text(
                text = "Rp${item.jumlah}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            // ===================== KANAN (Aksi) =====================
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                // ✔ TANDAI SELESAI
                IconButton(onClick = { showDoneDialog = true }) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = "Selesai",
                        tint = Color(0xFF4CAF50)
                    )
                }

                // ✏ EDIT
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFFFFC107)
                    )
                }

                // 🗑 DELETE
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE53935)
                    )
                }
            }
        }
    }
}
