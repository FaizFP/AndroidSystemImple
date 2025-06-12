package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factory untuk membuat instance dari TransaksiViewModel
// Digunakan ketika ViewModel membutuhkan parameter (dalam hal ini Application)
class TransaksiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    // Method ini dipanggil oleh sistem saat ViewModel pertama kali dibuat
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Memastikan bahwa class yang diminta adalah TransaksiViewModel
        if (modelClass.isAssignableFrom(TransaksiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransaksiViewModel(application) as T
        }
        // Jika bukan, lempar exception
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
