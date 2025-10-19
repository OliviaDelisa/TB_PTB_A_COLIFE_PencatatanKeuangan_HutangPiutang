package com.example.tugasbesarptb_colife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tugasbesarptb_colife.navigation.NavGraph
import com.example.tugasbesarptb_colife.ui.theme.TugasBesarPTB_COLIFETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugasBesarPTB_COLIFETheme {
                NavGraph()
            }
        }
    }
}
