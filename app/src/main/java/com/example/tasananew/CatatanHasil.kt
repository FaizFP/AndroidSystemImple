package com.example.tasananew

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasananew.database.CatatanEntitiy
import com.example.tasananew.database.CatatanViewModel
import com.example.tasananew.database.CatatanViewModelFactory

@Composable
fun CatatanHasilScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = CatatanViewModelFactory(application)
    val viewModel: CatatanViewModel = viewModel(factory = factory)

    val catatanList by viewModel.projects.observeAsState(emptyList())

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Daftar Catatan", fontSize = 20.sp, color = Color.White)

            if (catatanList.isEmpty()) {
                Text("Belum ada data catatan.", color = Color.White)
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Proyek", color = Color.White, modifier = Modifier.weight(1f))
                    Text("Saran", color = Color.White, modifier = Modifier.weight(1f))
                    Text("Kategori", color = Color.White, modifier = Modifier.weight(1f))
                    Text("Status", color = Color.White, modifier = Modifier.weight(1f))
                }

                HorizontalDivider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(catatanList) { catatan: CatatanEntitiy ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = catatan.projectName, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = catatan.suggest, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = catatan.category, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = catatan.status, color = Color.White, modifier = Modifier.weight(1f))
                        }
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    }
                }
            }

            // Tombol untuk menambah catatan baru
            RoundedNextButton("ADD CATATAN") {
                context.startActivity(Intent(context, CatatanActivity::class.java))
            }

            // Menu bar di bawah
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 16.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kertas),
                        contentDescription = "Menu Kertas",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                context.startActivity(Intent(context, ListActivity::class.java))
                            }
                    )
                }
            }
        }
    }
}