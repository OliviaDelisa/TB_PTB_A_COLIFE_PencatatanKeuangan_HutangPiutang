package com.example.tugasbesarptb_colife.pages.pemasukan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPemasukanScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pemasukan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Aksi notifikasi */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // Latar belakang TopAppBar
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Aksi tambah pemasukan */ },
                containerColor = hijau30,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada Pemasukan",
                color = hijau30,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    BottomAppBar(
        containerColor = Color(0xFFF0F3F2), // Warna abu-abu muda seperti di gambar
        contentColor = Color.Gray,
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets(0.dp))
    ) {

        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            onClick = { /* navController.navigate("home") */ }
        )
        BottomNavItem(
            label = "Pemasukan",
            icon = Icons.Default.AccountBalanceWallet, // Contoh ikon untuk pemasukan
            onClick = { navController.navigate("pemasukan") },
            isSelected = true // Tandai sebagai halaman aktif
        )
        BottomNavItem(
            label = "Pengeluaran",
            icon = Icons.Default.ShoppingCart,
            onClick = { /* navController.navigate("pengeluaran") */ }
        )
        BottomNavItem(
            label = "Hutang",
            icon = Icons.Default.Receipt, // Contoh ikon untuk hutang
            onClick = { /* navController.navigate("hutang") */ }
        )
        BottomNavItem(
            label = "Profil",
            icon = Icons.Default.Person,
            onClick = { /* navController.navigate("profil") */ }
        )


    }
}

@Composable
fun RowScope.BottomNavItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    val contentColor = if (isSelected) hijau30 else Color.Gray

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor
        )
        Text(
            text = label,
            color = contentColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PemasukanScreenPreview() {
    TugasBesarPTB_COLIFETheme {
        DaftarPemasukanScreen(navController = rememberNavController())
    }
}
