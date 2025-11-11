package com.example.tugasbesarptb_colife.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions // <-- IMPORT DITAMBAHKAN
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahHutangScreen(navController: NavHostController) {
    TugasBesarPTB_COLIFETheme {

        var namaPeminjam by remember { mutableStateOf("") }
        // Mengubah state tanggal menjadi Long (milidetik) agar kompatibel dengan DatePickerState
        var tanggalTagihan by remember { mutableStateOf<Long?>(null) }
        var jumlahPinjaman by remember { mutableStateOf("") }

        var showDatePicker by remember { mutableStateOf(false) }

        // Membuat DatePickerState untuk mengontrol DatePicker
        val datePickerState = rememberDatePickerState()

        // Formatter untuk menampilkan tanggal di TextField
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                // ✅ Judul
                Text(
                    text = "Tambahkan Hutang",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // ✅ Nama Peminjam
                Text(
                    text = "Nama Peminjam",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = namaPeminjam,
                    onValueChange = { namaPeminjam = it },
                    placeholder = {
                        Text(
                            "Masukkan nama peminjam",
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A9C90),
                        unfocusedBorderColor = Color(0xFF4A9C90),
                        cursorColor = Color(0xFF4A9C90)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ✅ Tanggal Tagihan
                Text(
                    text = "Tanggal Tagihan",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    // Menampilkan tanggal yang sudah diformat jika tidak null
                    value = if (tanggalTagihan != null) dateFormatter.format(Date(tanggalTagihan!!)) else "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Masukkan tanggal tagihan pinjaman",
                            fontSize = 12.sp
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendar"
                            )
                        }
                    },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A9C90),
                        unfocusedBorderColor = Color(0xFF4A9C90),
                        cursorColor = Color(0xFF4A9C90)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = jumlahPinjaman,
                    onValueChange = { newValue ->

                        // Hapus semua karakter non-digit
                        val digitsOnly = newValue.filter { it.isDigit() }

                        // Hindari crash jika kosong
                        if (digitsOnly.isEmpty()) {
                            jumlahPinjaman = ""
                            return@OutlinedTextField
                        }

                        // Format ribuan (1234567 -> 1.234.567)
                        val formatted = digitsOnly.reversed()
                            .chunked(3)
                            .joinToString(".")
                            .reversed()

                        jumlahPinjaman = formatted
                    },
                    placeholder = {
                        Text(
                            "Masukkan nominal pinjaman",
                            fontSize = 12.sp
                        )
                    },
                    trailingIcon = {
                        Text(
                            text = "Rp",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A9C90),
                        unfocusedBorderColor = Color(0xFF4A9C90),
                        cursorColor = Color(0xFF4A9C90)
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number // ✅ Hanya angka
                    )
                )
            }


                // ✅ Tombol Selesai
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(20.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A9C90)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Selesai",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }

            // ✅ Date Picker Popup
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Ambil tanggal yang dipilih dari state
                                tanggalTagihan = datePickerState.selectedDateMillis
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Batal")
                        }
                    }
                ) {
                    // Gunakan DatePicker dengan state yang sudah dibuat
                    DatePicker(state = datePickerState)
                }
            }
        }
    }

