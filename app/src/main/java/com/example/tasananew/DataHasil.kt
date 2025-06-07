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
import com.example.tasananew.database.LingkunganEntity
import com.example.tasananew.database.LingkunganViewModel
import com.example.tasananew.database.LingkunganViewModelFactory

@Composable
fun DataLingkunganHasilScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = LingkunganViewModelFactory(application)
    val viewModel: LingkunganViewModel = viewModel(factory = factory)

    val lingkunganList by viewModel.lingkunganList.observeAsState(emptyList())

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Data Lingkungan", fontSize = 20.sp, color = Color.White)

            if (lingkunganList.isEmpty()) {
                Text("Belum ada data lingkungan.", color = Color.White)
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Proyek", color = Color.White, modifier = Modifier.weight(1f))
                    Text("OS", color = Color.White, modifier = Modifier.weight(1f))
                    Text("CPU", color = Color.White, modifier = Modifier.weight(1f))
                    Text("RAM", color = Color.White, modifier = Modifier.weight(1f))
                    Text("DB", color = Color.White, modifier = Modifier.weight(1f))
                    Text("Aksi", color = Color.White, modifier = Modifier.weight(1f))
                }

                HorizontalDivider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(lingkunganList) { item: LingkunganEntity ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.projectName, color = Color.White, modifier = Modifier.weight(1f))
                            Text(item.os, color = Color.White, modifier = Modifier.weight(1f))
                            Text(item.cpu, color = Color.White, modifier = Modifier.weight(1f))
                            Text(item.ram, color = Color.White, modifier = Modifier.weight(1f))
                            Text(item.database, color = Color.White, modifier = Modifier.weight(1f))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Edit",
                                    color = Color.Cyan,
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .clickable {
                                            val intent = Intent(context, EditLingkunganActivity::class.java)
                                            intent.putExtra("id", item.id)
                                            context.startActivity(intent)
                                        }
                                )
                                Text(
                                    "Delete",
                                    color = Color.Red,
                                    modifier = Modifier.clickable {
                                        viewModel.deleteLingkungan(item)
                                    }
                                )
                            }
                        }
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    }
                }
            }

            // Tombol tambah data
            RoundedNextButton("ADD DATA") {
                context.startActivity(Intent(context, DataLingkunganActivity::class.java))
            }

            // Menu bar bawah
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
