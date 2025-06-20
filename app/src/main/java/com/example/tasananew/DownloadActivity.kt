package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.lifecycle.ViewModelProvider
import com.example.tasananew.database.*

class GeneratePdfActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi database dan DAO
        val database = AppDatabase.getDatabase(applicationContext)
        val projectDao = database.projectDao()
        val lingkunganDao = database.lingkunganDao()
        val catatanDao = database.catatanDao()
        val transaksiDao = database.transaksiDao()
        val aktivitasDao = database.aktivitasDao()

        // Inisialisasi ViewModel
        val laporanViewModel = ViewModelProvider(
            this,
            LaporanViewModelFactory(projectDao, lingkunganDao, catatanDao, transaksiDao, aktivitasDao)
        )[LaporanViewModel::class.java]

        setContent {
            // Tanpa tema khusus
            Surface(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                GeneratePdfScreen(laporanViewModel)
            }
        }
    }
}
