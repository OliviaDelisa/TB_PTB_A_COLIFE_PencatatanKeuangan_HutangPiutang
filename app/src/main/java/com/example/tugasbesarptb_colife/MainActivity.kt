package com.example.tugasbesarptb_colife

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.navigation.NavGraph
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var session: SessionManager
    private var userId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ===== WAJIB ANDROID 13+ =====
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // ===== FORCE TOKEN BIKIN =====
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "FORCE TOKEN: $token")
        }

        // ===== SESSION =====
        session = SessionManager(this)
        userId = session.getUserId()

        // ===== SYNC =====
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

        // ===== COMPOSE =====
        setContent {
            val navController = rememberNavController()
            val open = intent.getStringExtra("open") ?: ""

            LaunchedEffect(open) {
                if (open == "notification") {
                    navController.navigate("notification")
                }
            }

            NavGraph(
                navController,
                startDestination = if (session.isLogin()) "home" else "login"
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
