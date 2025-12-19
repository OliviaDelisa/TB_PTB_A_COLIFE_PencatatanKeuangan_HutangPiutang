package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tugasbesarptb_colife.pages.*
import com.example.tugasbesarptb_colife.pages.pemasukan.*
import com.example.tugasbesarptb_colife.pages.pengeluaran.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {

    /* ================= STATE PENGELUARAN ================= */
    val pengeluaranList = remember {
        mutableStateListOf(
            Pengeluaran("Kopi", "18 Oktober 2025", "15.000"),
            Pengeluaran("Nasi Goreng", "17 Oktober 2025", "25.000")
        )
    }

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {

        /* ================= EXISTING ROUTES (TIDAK DIUBAH) ================= */
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

        /* ================= PENGELUARAN (DITAMBAHKAN) ================= */

        // LIST
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

        // TAMBAH
        composable("tambahpengeluaran") {
            TambahPengeluaranScreen(
                navController = navController,
                onAddPengeluaran = { pengeluaran ->
                    pengeluaranList.add(0, pengeluaran)
                    navController.navigate("daftarpengeluaran") {
                        popUpTo("tambahpengeluaran") {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // EDIT (PAKAI INDEX)
        composable(
            route = "editpengeluaran/{pengeluaranIndex}",
            arguments = listOf(
                navArgument("pengeluaranIndex") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val index =
                backStackEntry.arguments?.getInt("pengeluaranIndex") ?: -1

            if (index != -1 && index < pengeluaranList.size) {
                EditPengeluaranScreen(
                    navController = navController,
                    pengeluaran = pengeluaranList[index],
                    onSave = { updated ->
                        pengeluaranList[index] = updated
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
