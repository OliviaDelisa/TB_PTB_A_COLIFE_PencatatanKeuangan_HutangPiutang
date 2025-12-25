package com.example.tugasbesarptb_colife.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Long): String {
    val localeID = Locale("in", "ID")
    val formatter = NumberFormat.getCurrencyInstance(localeID)
    return formatter.format(amount).replace(",00", "").replace("Rp ", "Rp")
}
