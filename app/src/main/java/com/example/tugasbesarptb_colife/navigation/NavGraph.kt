package com.example.tugasbesarptb_colife.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.pages.Daftar
import com.example.tugasbesarptb_colife.pages.LandingPage
import com.example.tugasbesarptb_colife.pages.Login
import com.example.tugasbesarptb_colife.pages.pengeluaran.DaftarPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pengeluaran.Pengeluaran
import com.example.tugasbesarptb_colife.pages.pengeluaran.TambahPengeluaranScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val pengeluaranList = remember { mutableStateListOf<Pengeluaran>() }

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        composable("daftarpengeluaran") { 
            DaftarPengeluaranScreen(
                navController = navController, 
                pengeluaranList = pengeluaranList,
                onDeletePengeluaran = { pengeluaran ->
                    pengeluaranList.remove(pengeluaran)
                }
            ) 
        }
        composable("tambahpengeluaran") { 
            TambahPengeluaranScreen(navController) { pengeluaran ->
                pengeluaranList.add(pengeluaran)
            }
        }
    }
}