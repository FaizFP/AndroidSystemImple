package com.example.tasananew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                onItemClick = { selectedItem ->
                    when (selectedItem) {
                        "PROJECT" -> startActivity(Intent(this, ProjectHasilActivity::class.java))
                        "DATA LINGKUNGAN" -> startActivity(Intent(this, DataLingkunganListActivity::class.java))
                        "CATATAN PEMELIHARAAN" -> startActivity(Intent(this, CatatanHasilActivity::class.java))
                        "DATA TRANSAKSI INPUT DAN OUTPUT" -> startActivity(Intent(this, TransaksiHasilActivity::class.java))
                        "KINERJA PERANGKAT LUNAK" -> startActivity(Intent(this, kinerjaPerangkatLunakActivity::class.java))
                        "AKTIVITAS PERANGKAT LUNAK" -> startActivity(Intent(this, AktivitasHasilActivity::class.java))
                        "DOWNLOAD" -> startActivity(Intent(this, GeneratePdfActivity::class.java))
                    }
                },
                context = this
            )
        }
    }
}

@Composable
fun MainScreen(
    onItemClick: (String) -> Unit,
    context: Context? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C))
            .padding(16.dp)
    ) {
        Text(
            text = "System Implementation",
            fontSize = 24.sp,
            color = Color(0xFFD9D9D9),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        DataBox(title = "PROJECT", onClick = { onItemClick("PROJECT") })

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val dataList = listOf(
                "KINERJA PERANGKAT LUNAK",
                "AKTIVITAS PERANGKAT LUNAK",
                "CATATAN PEMELIHARAAN",
                "DATA LINGKUNGAN",
                "DATA TRANSAKSI INPUT DAN OUTPUT",
                "DOWNLOAD"
            )
            items(dataList) { item ->
                DataBox(title = item, onClick = { onItemClick(item) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (context != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(12.dp))
                    .clickable {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("DASHBOARD", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun DataBox(title: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9)),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(8.dp)
            .height(50.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(onItemClick = {})
}
