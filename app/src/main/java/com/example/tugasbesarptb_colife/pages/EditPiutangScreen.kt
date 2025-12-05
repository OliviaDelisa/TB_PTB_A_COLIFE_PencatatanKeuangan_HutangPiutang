package com.example.tugasbesarptb_colife.pages

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.SessionManager
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.components.TopBar
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModel
import com.example.tugasbesarptb_colife.viewmodel.PiutangViewModelFactory
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun EditPiutangScreen(navController: NavController) {
    val context = LocalContext.current
    val userId = SessionManager(context).getUserId().toInt()

    val repository = remember {
        PiutangRepository(
            piutangDao = AppDatabase.getInstance(context).piutangDao(),
            apiService = ApiClient.instance,
            userId = userId
        )
    }

    val viewModel: PiutangViewModel = viewModel(
        factory = remember {
            PiutangViewModelFactory(repository)
        }
    )
    val piutangToEdit = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Parcelable>("piutangToEdit") as? Piutang

    var nama by remember { mutableStateOf(piutangToEdit?.nama ?: "") }
    var tanggalTagihan by remember { mutableStateOf(piutangToEdit?.tanggalTenggat ?: "") }
    var jumlahText by remember { mutableStateOf(piutangToEdit?.jumlah?.toString() ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar(navController = navController as NavHostController) },
        bottomBar = { BottomNavBar(navController, currentRoute = "hutang") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Edit Daftar Piutang",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Peminjam") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tanggalTagihan,
                onValueChange = {},
                label = { Text("Tanggal Tagihan") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jumlahText,
                onValueChange = { if (it.all(Char::isDigit)) jumlahText = it },
                label = { Text("Jumlah Pinjaman") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = { Text("Rp", color = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (
                        piutangToEdit != null &&
                        piutangToEdit.userId == userId &&
                        nama.isNotBlank() &&
                        tanggalTagihan.isNotBlank() &&
                        jumlahText.isNotBlank()
                    ) {
                        val updatedPiutang = piutangToEdit.copy(
                            nama = nama,
                            jumlah = jumlahText.toInt(),
                            tanggalTenggat = tanggalTagihan
                        )

                        scope.launch {
                            viewModel.updatePiutang(updatedPiutang)
                            viewModel.syncPendingPiutang()
                            navController.popBackStack()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E8378)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .align(Alignment.End)
                    .height(48.dp)
                    .width(130.dp)
            ) {
                Text("Simpan", color = Color.White)
            }
        }

        // Date picker di luar Column
        TanggalPicker(
            buka = showDatePicker,
            saatTutup = { showDatePicker = false },
            saatDipilih = { tanggal -> tanggalTagihan = tanggal }
        )
    }
}