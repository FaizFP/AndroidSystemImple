package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.tasananew.database.TransaksiViewModel
import com.example.tasananew.database.TransaksiViewModelFactory

class TransaksiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengambil nama project dari intent extras
        // Digunakan untuk menampilkan atau mengelompokkan transaksi berdasarkan project
        val projectName = intent.getStringExtra("PROJECT_NAME") ?: "DefaultProject"

        // Membuat instance ViewModel menggunakan ViewModelFactory
        // Karena TransaksiViewModel membutuhkan context (Application)
        val viewModel = ViewModelProvider(
            this,
            TransaksiViewModelFactory(application)
        )[TransaksiViewModel::class.java]

        // Menampilkan UI TransaksiScreen dan mengoper ViewModel ke dalamnya
        setContent {
            TransaksiScreen(viewModel = viewModel)
        }
    }
}
