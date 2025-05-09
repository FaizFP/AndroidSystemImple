package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CatatanListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Mengambil data yang dikirim melalui Intent
                    val catatanList = intent.getStringArrayListExtra("DATA_LIST") ?: arrayListOf()

                    // Menampilkan CatatanListScreen dengan data yang diambil
                    CatatanListScreen(catatanList)
                }
            }
        }
    }
}

@Composable
fun CatatanListScreen(catatanList: List<String>) {
    // Menampilkan daftar catatan
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(catatanList) { catatan ->
            // Menampilkan setiap catatan dalam bentuk card atau text
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = catatan, fontSize = 16.sp)
                }
            }
        }
    }
}
