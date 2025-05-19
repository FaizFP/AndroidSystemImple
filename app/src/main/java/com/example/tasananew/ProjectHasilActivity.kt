package com.example.tasananew


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color

class ProjectHasilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF2F3E2F) // Warna latar belakang sama seperti di MainActivity
            ) {
                ProjectHasilScreen()
            }
        }
    }
}
