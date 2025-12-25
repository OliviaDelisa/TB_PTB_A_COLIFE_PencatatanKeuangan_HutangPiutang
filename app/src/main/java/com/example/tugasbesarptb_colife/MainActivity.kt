package com.example.tugasbesarptb_colife

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.navigation.NavGraph
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val userId = 1 

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val piutangRepository = PiutangRepository(
            piutangDao = AppDatabase.getInstance(applicationContext).piutangDao(),
            apiService = ApiClient.instance,
            userId = userId
        )
        lifecycleScope.launch {
            try {
                piutangRepository.syncPending()
            } catch (e: Exception) {
                e.printStackTrace() 
            }
        }


        setContent {
            val navController = rememberNavController()
            NavGraph(navController)
        }
    }

    override fun onResume() {
        super.onResume()

        val piutangRepository = PiutangRepository(
            piutangDao = AppDatabase.getInstance(applicationContext).piutangDao(),
            apiService = ApiClient.instance,
            userId = userId
        )

        lifecycleScope.launch {
            try {
                piutangRepository.syncPending()
            } catch (e: Exception) {
                e.printStackTrace() // tangani error agar tidak crash
            }
        }
    }
}
