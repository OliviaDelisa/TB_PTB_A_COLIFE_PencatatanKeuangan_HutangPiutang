package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tugasbesarptb_colife.pages.BuktiPembayaranScreen
import com.example.tugasbesarptb_colife.pages.LandingPage
import com.example.tugasbesarptb_colife.pages.Login
import com.example.tugasbesarptb_colife.pages.Daftar
import com.example.tugasbesarptb_colife.pages.DaftarPiutang
import com.example.tugasbesarptb_colife.pages.EditHutangScreen
import com.example.tugasbesarptb_colife.pages.EditPiutangScreen
import com.example.tugasbesarptb_colife.pages.HistoryHutangScreen
import com.example.tugasbesarptb_colife.pages.Home
import com.example.tugasbesarptb_colife.pages.HutangScreen
import com.example.tugasbesarptb_colife.pages.HutangUtama
import com.example.tugasbesarptb_colife.pages.ProfilScreen
import com.example.tugasbesarptb_colife.pages.StrukHutangScreen
import com.example.tugasbesarptb_colife.pages.TambahHutangScreen
import com.example.tugasbesarptb_colife.pages.TambahPiutang
import com.example.tugasbesarptb_colife.pages.pemasukan.DaftarPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.TambahPemasukanScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        composable("home") { Home(navController) }
        composable("hutang") { HutangUtama(navController) }
        composable("detailPiutang") {DaftarPiutang(navController) }
        composable("tambahPiutang") {TambahPiutang(navController) }
        composable("uploadBuktiPembayaran") {BuktiPembayaranScreen(navController) }
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

