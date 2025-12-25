package com.example.tugasbesarptb_colife

import android.content.Context



class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLogin(userId: Int) {
        prefs.edit().apply {
            putInt("user_id", userId)
            putBoolean("is_login", true) // flag login
            apply()
        }
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", 0)
    }

    fun isLogin(): Boolean {
        return prefs.getBoolean("is_login", false)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}



