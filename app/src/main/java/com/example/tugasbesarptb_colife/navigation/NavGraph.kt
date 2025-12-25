package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.tugasbesarptb_colife.pages.*
import com.example.tugasbesarptb_colife.pages.hutang.EditHutangScreen
import com.example.tugasbesarptb_colife.pages.hutang.HistoryHutangScreen
import com.example.tugasbesarptb_colife.pages.hutang.HutangScreen
import com.example.tugasbesarptb_colife.pages.hutang.StrukHutangScreen
import com.example.tugasbesarptb_colife.pages.hutang.TambahHutangScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.DaftarPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.EditPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.SummaryPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.TambahPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pengeluaran.DaftarPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pengeluaran.TambahPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.piutang.BuktiPembayaranScreen
import com.example.tugasbesarptb_colife.pages.piutang.DaftarPiutang
import com.example.tugasbesarptb_colife.pages.piutang.EditPiutangScreen
import com.example.tugasbesarptb_colife.pages.piutang.TambahPiutang
import com.example.tugasbesarptb_colife.pages.pemasukan.*
import com.example.tugasbesarptb_colife.pages.pengeluaran.*

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
        composable("notification") { NotifikasiPage(navController) }


        // Pemasukan
        composable("daftarpemasukan") { DaftarPemasukanScreen(navController) }
        composable("tambahpemasukan") { TambahPemasukanScreen(navController) }
        composable("editpemasukan") { EditPemasukanScreen(navController) }
        composable("tambahkategori") { TambahKategoriPengeluaranScreen(navController) }
        composable("summarypengeluaran") { SummaryPengeluaranScreen(navController) }

        // Pengeluaran & Kategori
        composable("daftarpengeluaran") { DaftarPengeluaranScreen(navController) } // BARU
        composable("tambahpengeluaran") { TambahPengeluaranScreen(navController) } // BARU
        composable("editpengeluaran") { EditPengeluaranScreen(navController) }
    }
}

