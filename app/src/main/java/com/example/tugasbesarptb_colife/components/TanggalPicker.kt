package com.example.tugasbesarptb_colife.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TanggalPicker(
    buka: Boolean,
    tanggalMin: String? = null, // yyyy-MM-dd
    saatTutup: () -> Unit,
    saatDipilih: (String) -> Unit
) {
    val minMillis = tanggalMin?.let {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.parse(it)?.time
    } ?: System.currentTimeMillis()

    val state = rememberDatePickerState(
        initialSelectedDateMillis = minMillis,
        initialDisplayedMonthMillis = minMillis
    )

    if (buka) {
        DatePickerDialog(
            onDismissRequest = { saatTutup() },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = state.selectedDateMillis
                    if (selectedMillis != null && selectedMillis >= minMillis) {
                        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(Date(selectedMillis))
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
            DatePicker(state = state)
        }
    }
}
