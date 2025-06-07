package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LingkunganViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LingkunganViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LingkunganViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
