package com.example.tugasbesarptb_colife.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

@Composable
fun StrukHutangScreen(navController: NavHostController) {
    TugasBesarPTB_COLIFETheme {

        var showMenu by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Judul
            Text(
                text = "Bukti Struk Hutang",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            //  Text tengah
            Text(
                text = "Belum ada struk hutang",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )

            // Popup menu animasi
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF4A9C90),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(15.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showMenu = false
                                // TODO: aksi ambil foto
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Ambil Foto",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text("Ambil foto", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showMenu = false
                                // TODO: aksi buka penyimpanan
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Buka Penyimpanan",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text("Buka Penyimpanan", color = Color.White)
                    }
                }
            }

            // FAB Tambah (+) — warna tombol 0xFF4A9C90 & icon putih
            FloatingActionButton(
                onClick = {
                    showMenu = !showMenu
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 30.dp),
                containerColor = Color(0xFF4A9C90),
                contentColor = Color.White // default warna isi FAB
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color.White // ikon putih
                )
            }
        }
    }
}