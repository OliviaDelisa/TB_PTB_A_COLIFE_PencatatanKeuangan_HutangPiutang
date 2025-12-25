package com.example.tugasbesarptb_colife.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tugasbesarptb_colife.SessionManager
import com.example.tugasbesarptb_colife.model.UserLoginRequest
import com.example.tugasbesarptb_colife.network.ApiClient
import com.example.tugasbesarptb_colife.ui.theme.hijau30
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

@Composable
fun Login(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Selamat Datang",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Login",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Masukkan Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Masukkan Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Filled.VisibilityOff
                    else Icons.Filled.Visibility

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Isi semua field!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                scope.launch {
                    try {
                        val response = ApiClient.instance.loginUser(
                            UserLoginRequest(email, password)
                        )

                        isLoading = false

                        if (response.isSuccessful && response.body()?.success == true) {
                            val body = response.body()!!

                            // Simpan session userId
                            sessionManager.saveLogin(body.userId)

                            // Ambil token FCM dan kirim ke backend
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val token = task.result
                                    Log.d("FCM Token", "Token: $token")
                                    scope.launch {
                                        sendTokenToBackend(body.userId, token)
                                    }
                                } else {
                                    Log.e("FCM Token", "Gagal ambil token")
                                }
                            }

                            Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()

                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }

                        } else {
                            Toast.makeText(
                                context,
                                response.body()?.message ?: "Login gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {
                        isLoading = false
                        Toast.makeText(context, "Kesalahan jaringan", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN_ERROR", e.toString())
                    }
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = hijau30),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tidak memiliki akun? ", fontSize = 14.sp)
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Sign up", color = hijau30, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

suspend fun sendTokenToBackend(userId: Int, token: String) {
    try {
        val url = "http://10.0.2.2:3000/api/fcm/save-fcm-token"
        // ganti sesuai IP server
        val json = """{ "userId": "$userId", "fcmToken": "$token" }"""
        val client = OkHttpClient()
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            json
        )
        val request = Request.Builder().url(url).post(body).build()
        val response = client.newCall(request).execute()
        Log.d("FCM Token", "Response: ${response.body?.string()}")
    } catch (e: Exception) {
        Log.e("FCM Token", "Error: ${e.message}")
    }
}
