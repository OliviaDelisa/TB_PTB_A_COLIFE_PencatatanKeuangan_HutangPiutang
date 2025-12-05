package com.example.tugasbesarptb_colife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository

class PiutangViewModelFactory(private val repository: PiutangRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PiutangViewModel::class.java)) {
            return PiutangViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
