package com.example.tugasbesarptb_colife.pages.hutang

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class StrukHutangViewModel : ViewModel() {
    val strukList = mutableStateListOf<Uri>()

    fun add(uri: Uri) {
        strukList.add(uri)
    }

    fun delete(uri: Uri) {
        strukList.remove(uri)
    }
}