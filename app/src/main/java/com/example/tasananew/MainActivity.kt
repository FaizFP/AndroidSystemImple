package com.example.systemimplementation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF2F3E2F) // Latar belakang utama
            ) {
                SystemImplementationScreen()
            }
        }
    }
}

@Composable
fun SystemImplementationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "System Implementation",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1F2C1F), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Aplikasi ini dirancang untuk mencatat, memantau, dan mengelola proses implementasi serta pemeliharaan perangkat lunak. Fitur utamanya mencakup pencatatan aktivitas implementasi, data transaksi model (input dan output), informasi lingkungan, serta catatan dari pemelihara. Sistem ini juga memantau kinerja perangkat lunak untuk memastikan operasional yang optimal dan mendukung perbaikan berkelanjutan, menjaga kualitas dan keandalan sistem dalam jangka panjang.",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}
