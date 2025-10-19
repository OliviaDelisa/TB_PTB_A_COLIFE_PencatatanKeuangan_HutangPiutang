package com.example.tugasbesarptb_colife.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.R
import com.example.tugasbesarptb_colife.ui.theme.hijau30

@Composable
fun LandingPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.gambarlandingpage),
                contentDescription = "Landing Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "COLIFE", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Organize Your College Life", style = MaterialTheme.typography.bodyMedium)
        }
        Button(
            onClick = { navController.navigate("login") },
            colors = ButtonDefaults.buttonColors(containerColor = hijau30),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Ayo, Jelajahi Sekarang")
        }
    }
}
