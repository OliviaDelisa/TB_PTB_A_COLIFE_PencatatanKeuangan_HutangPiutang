package com.example.tugasbesarptb_colife.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.ui.theme.hijau30

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String?) {
    BottomAppBar(
        containerColor = Color(0xFFF0F3F2),
        contentColor = Color.Gray,
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets(0.dp))
    ) {
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            onClick = { /* TODO: Navigasi ke Home */ },
            isSelected = currentRoute == "home"
        )
        BottomNavItem(
            label = "Pemasukan",
            icon = Icons.Default.AccountBalanceWallet,
            onClick = { navController.navigate("daftarpemasukan") },
            isSelected = currentRoute == "daftarpemasukan" || currentRoute == "tambahpemasukan"
        )
        BottomNavItem(
            label = "Pengeluaran",
            icon = Icons.Default.ShoppingCart,
            onClick = { /* TODO: Navigasi ke Pengeluaran */ },
            isSelected = currentRoute == "pengeluaran"
        )
        BottomNavItem(
            label = "Hutang",
            icon = Icons.Default.Receipt,
            onClick = { /* TODO: Navigasi ke Hutang */ },
            isSelected = currentRoute == "hutang"
        )
        BottomNavItem(
            label = "Profil",
            icon = Icons.Default.Person,
            onClick = { /* TODO: Navigasi ke Profil */ },
            isSelected = currentRoute == "profil"
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
