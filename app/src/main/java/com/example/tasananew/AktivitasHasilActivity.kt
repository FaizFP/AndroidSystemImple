package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color

class AktivitasHasilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = Color(0xFF2F3E2F)) {
                // Tidak perlu kirim viewModel, karena dibuat langsung di dalam AktivitasHasilScreen
                AktivitasHasilScreen()
            }
        }
    }
}
