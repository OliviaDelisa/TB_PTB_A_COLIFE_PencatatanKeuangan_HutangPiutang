package com.example.tugasbesarptb_colife.pages.pemasukan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.viewmodel.PemasukanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPemasukanScreen(navController: NavController) { // Parameter pemasukanId dihapus

    val pemasukanViewModel: PemasukanViewModel = viewModel()

    // Ambil ID dari SavedStateHandle
    val pemasukanId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("pemasukanId")

    // State untuk data yang akan di-edit
    var sumberPemasukan by remember { mutableStateOf("") }
    var tanggalPemasukan by remember { mutableStateOf("") }
    var jumlahPemasukan by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Jika ID ditemukan, ambil data dari database
    if (pemasukanId != null) {
        val pemasukanState by pemasukanViewModel.getPemasukanById(pemasukanId).observeAsState()

        // Update state saat data dari database diterima
        LaunchedEffect(pemasukanState) {
            pemasukanState?.let {
                sumberPemasukan = it.sumber
                tanggalPemasukan = it.tanggal
                jumlahPemasukan = it.jumlah
            }
        }
    }

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Pemasukan", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            FormInput(
                label = "Sumber Pemasukan",
                value = sumberPemasukan,
                onValueChange = { sumberPemasukan = it },
                placeholder = "Masukkan sumber pemasukan"
            )

            FormInput(
                label = "Tanggal Pemasukan",
                value = tanggalPemasukan,
                onValueChange = {},
                placeholder = "Masukkan tanggal pemasukan",
                readOnly = true,
                trailingIcon = { IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.CalendarToday, "Pilih Tanggal") } }
            )

            FormInput(
                label = "Jumlah Pemasukan",
                value = jumlahPemasukan,
                onValueChange = { jumlahPemasukan = it },
                placeholder = "Masukkan nominal pemasukan",
                keyboardType = KeyboardType.Number,
                trailingIcon = { Text("Rp", color = Color.Gray, modifier = Modifier.padding(end = 8.dp)) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (pemasukanId != null && sumberPemasukan.isNotBlank() && tanggalPemasukan.isNotBlank() && jumlahPemasukan.isNotBlank()) {
                        val updatedPemasukan = Pemasukan(id = pemasukanId, sumber = sumberPemasukan, tanggal = tanggalPemasukan, jumlah = jumlahPemasukan)
                        pemasukanViewModel.update(updatedPemasukan)
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E8378)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.align(Alignment.End).height(48.dp).width(130.dp)
            ) {
                Text("Simpan", fontSize = 16.sp, color = Color.White)
            }
        }
        TanggalPicker(
            buka = showDatePicker,
            saatTutup = { showDatePicker = false },
            saatDipilih = { tanggal -> tanggalPemasukan = tanggal }
        )
    }
}

@Composable
private fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = hijau30,
                focusedBorderColor = hijau30,
                cursorColor = hijau30
            )
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun EditPemasukanScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        EditPemasukanScreen(navController = rememberNavController())
    }
}