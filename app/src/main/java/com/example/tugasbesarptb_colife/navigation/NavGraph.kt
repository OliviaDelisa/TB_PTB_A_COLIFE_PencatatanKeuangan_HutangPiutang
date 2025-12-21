package com.example.tugasbesarptb_colife.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tugasbesarptb_colife.CoLifeApplication
import com.example.tugasbesarptb_colife.pages.*
import com.example.tugasbesarptb_colife.pages.pemasukan.*
import com.example.tugasbesarptb_colife.pages.pengeluaran.*
import com.example.tugasbesarptb_colife.pages.pengeluaran.viewmodel.PengeluaranViewModel
import com.example.tugasbesarptb_colife.pages.pengeluaran.viewmodel.PengeluaranViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as CoLifeApplication
    val viewModel: PengeluaranViewModel = viewModel(
        factory = PengeluaranViewModelFactory(application.pengeluaranRepository)
    )

    val pengeluaranList by viewModel.allPengeluaran.collectAsState()

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

        /* ================= PENGELUARAN (DIPERBARUI) ================= */

        // LIST
        composable("daftarpengeluaran") {
            DaftarPengeluaranScreen(
                navController = navController,
                pengeluaranList = pengeluaranList,
                onDeletePengeluaran = { pengeluaran ->
                    viewModel.delete(pengeluaran)
                },
                onEditPengeluaran = { pengeluaran ->
                    navController.navigate("editpengeluaran/${pengeluaran.id}")
                }
            )
        }

        // TAMBAH
        composable("tambahpengeluaran") {
            TambahPengeluaranScreen(
                navController = navController,
                onAddPengeluaran = { pengeluaran ->
                    viewModel.insert(pengeluaran)
                    navController.popBackStack()
                }
            )
        }

        // EDIT (DIPERBARUI)
        composable(
            route = "editpengeluaran/{pengeluaranId}",
            arguments = listOf(
                navArgument("pengeluaranId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("pengeluaranId") ?: -1

            // ✅ Panggil EditPengeluaranScreen dengan id dan viewModel
            EditPengeluaranScreen(
                navController = navController,
                pengeluaranId = id,
                viewModel = viewModel
            )
        }
    }
}