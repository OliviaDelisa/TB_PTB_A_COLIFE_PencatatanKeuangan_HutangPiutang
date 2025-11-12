package com.example.tugasbesarptb_colife.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.components.BottomNavBar
import com.example.tugasbesarptb_colife.components.TopBar
import com.example.tugasbesarptb_colife.components.TanggalPicker
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavController) {

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
    val calendar = Calendar.getInstance()
    var tanggalDipilih by remember { mutableStateOf(formatter.format(calendar.time)) }

    var showTanggalPicker by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
    val date = sdf.parse(tanggalDipilih)
    val bulanSekarang = SimpleDateFormat("MMMM", Locale("id", "ID")).format(date)
    val tahunSekarang = SimpleDateFormat("yyyy", Locale("id", "ID")).format(date)

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(navController, currentRoute = "home") }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tahunSekarang,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1.2f)
                )

                Text(
                    text = "Pengeluaran",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
                Text(
                    text = "Pemasukan",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
                Text(
                    text = "Saldo",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(bulanSekarang, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = { showTanggalPicker = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Pilih tanggal")
                    }
                }

                Text(
                    text = "80.000",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
                Text(
                    text = "200.000",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
                Text(
                    text = "120.000",
                    fontWeight = FontWeight.Bold,
                    color = hijau30,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tanggalDipilih, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text("Aktivitas Anda", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            ActivityCard(
                title = "Pemasukan",
                value = "200.000",
                color = hijau30,
                icon = Icons.Default.ArrowDownward
            )
            ActivityCard(
                title = "Pengeluaran",
                value = "-80.000",
                color = Color.Red,
                icon = Icons.Default.ArrowUpward
            )
            ActivityCard(
                title = "Hutang",
                value = "10.000",
                color = Color.Gray,
                icon = Icons.Default.WarningAmber
            )
            ActivityCard(
                title = "Piutang",
                value = "50.000",
                color = Color(0xFF2196F3),
                icon = Icons.Default.AttachMoney
            )

            if (showTanggalPicker) {
                TanggalPicker(
                    buka = true,
                    saatTutup = { false.also { showTanggalPicker = false } },
                    saatDipilih = { hasilTanggal ->
                        tanggalDipilih = hasilTanggal
                        showTanggalPicker = false
                    }
                )
            }
        }
    }
}

@Composable
fun ActivityCard(title: String, value: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F5EF))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = color)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.SemiBold)
            }
            Text(value, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
