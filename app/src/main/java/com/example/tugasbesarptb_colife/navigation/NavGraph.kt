package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.pages.LandingPage
import com.example.tugasbesarptb_colife.pages.Login
import com.example.tugasbesarptb_colife.pages.Daftar
import com.example.tugasbesarptb_colife.pages.EditHutangScreen
import com.example.tugasbesarptb_colife.pages.HistoryHutangScreen
import com.example.tugasbesarptb_colife.pages.HutangScreen
import com.example.tugasbesarptb_colife.pages.StrukHutangScreen
import com.example.tugasbesarptb_colife.pages.TambahHutangScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingPage(navController) }
        composable("login") { Login(navController) }
        composable("signup") { Daftar(navController) }
        composable("listhutang") { HutangScreen(navController) }
        composable("historyhutang") { HistoryHutangScreen(navController) }
        composable("strukhutang") { StrukHutangScreen(navController) }
        composable("tambahhutang") { TambahHutangScreen(navController) }
        composable("edithutang") { EditHutangScreen(navController) }


    }
}
