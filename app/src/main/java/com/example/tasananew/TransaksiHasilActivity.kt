package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color

class TransaksiHasilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Memberi background warna hijau tua ke seluruh screen
            Surface(color = Color(0xFF2F3E2F)) {
                TransaksiHasilScreen()
            }
        }
    }
}
