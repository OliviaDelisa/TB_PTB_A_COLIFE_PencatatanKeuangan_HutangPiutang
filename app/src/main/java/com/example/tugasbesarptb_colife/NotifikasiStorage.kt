package com.example.tugasbesarptb_colife

object NotifikasiStorage {
    private val notifications = mutableListOf<String>()

    fun addNotification(message: String) {
        notifications.add(0, message) 
    }

    fun getNotifications(): List<String> = notifications
}
