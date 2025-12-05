package com.example.tugasbesarptb_colife.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.pages.LandingPage
import com.example.tugasbesarptb_colife.pages.Login
import com.example.tugasbesarptb_colife.pages.Daftar
import com.example.tugasbesarptb_colife.pages.pemasukan.DaftarPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.TambahPemasukanScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.TambahKategoriPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pemasukan.SummaryPengeluaranScreen



@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        composable("daftarpemasukan") { DaftarPemasukanScreen(navController) }
        composable("tambahpemasukan") { TambahPemasukanScreen(navController) }
        composable("tambahkategori") { TambahKategoriPengeluaranScreen(navController) }
        composable("summarypengeluaran") { SummaryPengeluaranScreen(navController) }

    }
}
