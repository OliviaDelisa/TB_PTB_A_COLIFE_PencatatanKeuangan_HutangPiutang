package com.example.tugasbesarptb_colife.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TanggalPicker(
    buka: Boolean,
    saatTutup: () -> Unit,
    saatDipilih: (String) -> Unit
) {
    val status = rememberDatePickerState()

    if (buka) {
        DatePickerDialog(
            onDismissRequest = { saatTutup() },
            confirmButton = {
                TextButton(onClick = {
                    val waktu = status.selectedDateMillis
                    if (waktu != null) {
                        val tanggal = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
                            .format(Date(waktu))
                        saatDipilih(tanggal)
                    }
                    saatTutup()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { saatTutup() }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = status)
        }
    }
}
