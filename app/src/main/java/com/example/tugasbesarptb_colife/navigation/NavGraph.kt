package com.example.tugasbesarptb_colife.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tugasbesarptb_colife.pages.Daftar
import com.example.tugasbesarptb_colife.pages.LandingPage
import com.example.tugasbesarptb_colife.pages.Login
import com.example.tugasbesarptb_colife.pages.pengeluaran.DaftarPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pengeluaran.EditPengeluaranScreen
import com.example.tugasbesarptb_colife.pages.pengeluaran.Pengeluaran
import com.example.tugasbesarptb_colife.pages.pengeluaran.TambahPengeluaranScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    // Data dummy untuk testing
    val pengeluaranList = remember {
        mutableStateListOf(
            Pengeluaran("Kopi", "18 Oktober 2025", "15.000"),
            Pengeluaran("Nasi Goreng", "17 Oktober 2025", "25.000")
        )
    }

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingPage(navController) }

        // Definisi halaman Login, yang akan dipanggil dari NavHost
        composable("login") {
            Login(
                navController = navController,
                // Tambahkan lambda untuk menangani navigasi setelah login berhasil
                onLoginSuccess = {
                    navController.navigate("daftarpengeluaran") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("signup") { Daftar(navController) }
        composable("daftarpengeluaran") {
            DaftarPengeluaranScreen(
                navController = navController,
                pengeluaranList = pengeluaranList,
                onDeletePengeluaran = { pengeluaran ->
                    pengeluaranList.remove(pengeluaran)
                },
                onEditPengeluaran = { pengeluaran ->
                    val index = pengeluaranList.indexOf(pengeluaran)
                    if (index != -1) {
                        navController.navigate("editpengeluaran/$index")
                    }
                }
            )
        }

        // --- BLOK INI SUDAH BENAR ---
        composable("tambahpengeluaran") {
            TambahPengeluaranScreen(navController) { pengeluaran ->
                pengeluaranList.add(0, pengeluaran)
                navController.popBackStack() // Kembali ke layar sebelumnya
            }
        }

        // Route untuk halaman edit
        composable(
            route = "editpengeluaran/{pengeluaranIndex}",
            arguments = listOf(navArgument("pengeluaranIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("pengeluaranIndex") ?: -1
            if (index != -1 && index < pengeluaranList.size) {
                val pengeluaranToEdit = pengeluaranList[index]
                EditPengeluaranScreen(
                    navController = navController,
                    pengeluaran = pengeluaranToEdit,
                    onSave = { updatedPengeluaran ->
                        pengeluaranList[index] = updatedPengeluaran
                        navController.popBackStack()
                    },
                    onDelete = {
                        pengeluaranList.removeAt(index)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
