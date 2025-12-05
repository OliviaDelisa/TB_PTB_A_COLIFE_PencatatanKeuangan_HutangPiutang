package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tugasbesarptb_colife.data.local.AppDatabase

import com.example.tugasbesarptb_colife.pages.*
import com.example.tugasbesarptb_colife.pages.pemasukan.DaftarPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.TambahPemasukanScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, startDestination: String = "landing") {
    NavHost(navController = navController, startDestination = startDestination) {

        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        composable("home") { Home(navController) }
        composable("hutang") { HutangUtama(navController) }
        composable("detailPiutang") { DaftarPiutang(navController) }
        composable("tambahPiutang") { TambahPiutang(navController) }
        composable("uploadBuktiPembayaran") { BuktiPembayaranScreen(navController) }
        composable("editPiutang") { EditPiutangScreen(navController) }
        composable("listhutang") { HutangScreen(navController) }
        composable("historyhutang") { HistoryHutangScreen(navController) }
        composable("strukhutang") { StrukHutangScreen(navController) }
        composable("tambahhutang") { TambahHutangScreen(navController) }
        composable("edithutang") { EditHutangScreen(navController) }
        composable("profilscreen") { ProfilScreen(navController) }
        composable("daftarpemasukan") { DaftarPemasukanScreen(navController) }
        composable("tambahpemasukan") { TambahPemasukanScreen(navController) }

        }
}