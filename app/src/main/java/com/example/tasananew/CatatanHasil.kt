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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    var selectedFilter by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Saran", "Kategori", "Status", "Mulai", "Selesai", "Pemangku", "Peran")
    var dropdownExpanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Daftar Catatan", fontSize = 20.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown filter sederhana
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(
                    onClick = { dropdownExpanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(selectedFilter)
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedFilter = option
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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

                    if (selectedFilter == "Semua" || selectedFilter == "Saran")
                        Text("Saran", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Kategori")
                        Text("Kategori", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Status")
                        Text("Status", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Mulai")
                        Text("Mulai", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Selesai")
                        Text("Selesai", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Pemangku")
                        Text("Pemangku", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Peran")
                        Text("Peran", color = Color.White, modifier = Modifier.weight(1f))
                }

                Divider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(catatanList) { catatan: CatatanEntitiy ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(catatan.projectName, color = Color.White, modifier = Modifier.weight(1f))

                                if (selectedFilter == "Semua" || selectedFilter == "Saran")
                                    Text(catatan.suggest, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Kategori")
                                    Text(catatan.category, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Status")
                                    Text(catatan.status, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Mulai")
                                    Text(catatan.startDate, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Selesai")
                                    Text(catatan.endDate, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Pemangku")
                                    Text(catatan.namaPemangkuKepentingan, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Peran")
                                    Text(catatan.namaPeran, color = Color.White, modifier = Modifier.weight(1f))
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    "Edit",
                                    color = Color.Cyan,
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .clickable {
                                            val intent = Intent(context, EditCatatanActivity::class.java).apply {
                                                putExtra("projectName", catatan.projectName)
                                                putExtra("suggest", catatan.suggest)
                                                putExtra("category", catatan.category)
                                                putExtra("status", catatan.status)
                                                putExtra("startDate", catatan.startDate)
                                                putExtra("endDate", catatan.endDate)
                                                putExtra("namaPemangkuKepentingan", catatan.namaPemangkuKepentingan)
                                                putExtra("namaPeran", catatan.namaPeran)
                                            }
                                            context.startActivity(intent)
                                        }
                                )
                                Text(
                                    "Delete",
                                    color = Color.Red,
                                    modifier = Modifier.clickable {
                                        viewModel.deleteCatatan(catatan)
                                    }
                                )
                            }

                            Divider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, CatatanActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ADD CATATAN", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kertas),
                        contentDescription = "Menu Kertas",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                context.startActivity(Intent(context, ListActivity::class.java))
                            }
                    )
                    Text("Menu Lain", color = Color.White)
                }
            }
        }
    }
}
