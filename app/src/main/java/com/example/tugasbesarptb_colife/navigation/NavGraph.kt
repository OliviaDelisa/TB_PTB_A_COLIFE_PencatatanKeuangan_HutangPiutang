package com.example.tugasbesarptb_colife.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.pages.Daftar
import com.example.tugasbesarptb_colife.pages.LandingPage
import com.example.tugasbesarptb_colife.pages.Login
import com.example.tugasbesarptb_colife.pages.pengeluaran.DaftarPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pengeluaran.TambahPengeluaranScreen
import com.example.tugasbesarptb_colife.ui.viewmodel.PengeluaranViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val pengeluaranViewModel: PengeluaranViewModel = viewModel()
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        composable("daftarpengeluaran") { DaftarPengeluaranScreen(navController, pengeluaranViewModel) }
        composable("tambahpengeluaran") { TambahPengeluaranScreen(navController, pengeluaranViewModel) }
    }
}
