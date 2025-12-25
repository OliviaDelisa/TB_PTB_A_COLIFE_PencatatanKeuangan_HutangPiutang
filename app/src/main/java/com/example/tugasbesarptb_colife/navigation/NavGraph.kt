package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tugasbesarptb_colife.pages.* // Import all pages
import com.example.tugasbesarptb_colife.pages.pemasukan.*
import com.example.tugasbesarptb_colife.pages.pengeluaran.*

@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "landing") {
        // Landing & Auth
        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        
        // Main
        composable("home") { Home(navController) }
        composable("profilscreen") { ProfilScreen(navController) }

        // Hutang & Piutang
        composable("hutang") { HutangUtama(navController) }
        composable("listhutang") { HutangScreen(navController) }
        composable("tambahhutang") { TambahHutangScreen(navController) }
        composable("edithutang") { EditHutangScreen(navController) }
        composable("historyhutang") { HistoryHutangScreen(navController) }
        composable("strukhutang") { StrukHutangScreen(navController) }
        composable("detailPiutang") { DaftarPiutang(navController) }
        composable("tambahPiutang") { TambahPiutang(navController) }
        composable("editPiutang") { EditPiutangScreen(navController) }
        composable("uploadBuktiPembayaran") { BuktiPembayaranScreen(navController) }

        // Pemasukan
        composable("daftarpemasukan") { DaftarPemasukanScreen(navController) }
        composable("tambahpemasukan") { TambahPemasukanScreen(navController) }
        composable("editpemasukan") { EditPemasukanScreen(navController) }
        composable("tambahkategori") { TambahKategoriPengeluaranScreen(navController) }
        composable("summarypengeluaran") { SummaryPengeluaranScreen(navController) }

        // Pengeluaran & Kategori
        composable("daftarpengeluaran") { DaftarPengeluaranScreen(navController) } // BARU
        composable("tambahpengeluaran") { TambahPengeluaranScreen(navController) } // BARU
        composable("editpengeluaran") { EditPengeluaranScreen(navController) }     // BARU

    }
}
