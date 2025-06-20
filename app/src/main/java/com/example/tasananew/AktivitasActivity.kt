package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.tasananew.database.AktivitasViewModel
import com.example.tasananew.database.AktivitasViewModelFactory

class AktivitasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil nama project dari intent jika tersedia
        val projectName = intent.getStringExtra("PROJECT_NAME") ?: "DefaultProject"

        // Buat ViewModel menggunakan Factory
        val viewModel = ViewModelProvider(
            this,
            AktivitasViewModelFactory(application)
        )[AktivitasViewModel::class.java]

        // Tampilkan layar input aktivitas
        setContent {
            InputAktivitasScreen(viewModel = viewModel)
        }
    }
}
