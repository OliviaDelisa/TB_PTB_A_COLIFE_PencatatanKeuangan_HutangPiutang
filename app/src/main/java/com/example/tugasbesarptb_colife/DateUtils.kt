package com.example.tugasbesarptb_colife

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}
