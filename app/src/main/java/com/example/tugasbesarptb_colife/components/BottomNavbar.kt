package com.example.tugasbesarptb_colife.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.ui.theme.hijau30

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String?) {
    @Composable
    fun NavLabel(text: String) {
        Text(
            text = text,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { NavLabel("Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = hijau30,
                selectedTextColor = hijau30,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Pemasukan") },
            label = { NavLabel("Pemasukan") },
            selected = currentRoute == "pemasukan",
            onClick = { navController.navigate("pemasukan") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = hijau30,
                selectedTextColor = hijau30,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Pengeluaran") },
            label = { NavLabel("Pengeluaran") },
            selected = currentRoute == "pengeluaran",
            onClick = { navController.navigate("pengeluaran") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = hijau30,
                selectedTextColor = hijau30,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Hutang") },
            label = { NavLabel("Hutang") },
            selected = currentRoute == "hutang",
            onClick = { navController.navigate("hutang") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = hijau30,
                selectedTextColor = hijau30,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
            label = { NavLabel("Profil") },
            selected = currentRoute == "profil",
            onClick = { navController.navigate("profilscreen") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = hijau30,
                selectedTextColor = hijau30,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
