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
import com.example.tasananew.database.AktivitasViewModel
import com.example.tasananew.database.AktivitasViewModelFactory

@Composable
fun AktivitasHasilScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = AktivitasViewModelFactory(application)
    val viewModel: AktivitasViewModel = viewModel(factory = factory)

    val aktivitasList by viewModel.aktivitasList.observeAsState(emptyList())

    var selectedFilter by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Model", "Algoritma", "Hyperparam")
    var dropdownExpanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text("Daftar Aktivitas", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown filter
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

            if (aktivitasList.isEmpty()) {
                Text("Belum ada data aktivitas.", color = Color.White)
            } else {
                // Header Kolom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Project", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Model")
                        Text("Model", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Algoritma")
                        Text("Algoritma", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Hyperparam")
                        Text("Hyperparam", color = Color.White, modifier = Modifier.weight(1f))
                }
                Divider(color = Color.White)

                // Isi Tabel
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(aktivitasList) { aktivitas ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(aktivitas.projectName, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Model")
                                    Text(aktivitas.modelType, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Algoritma")
                                    Text(aktivitas.algorithmUsed, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Hyperparam")
                                    Text(aktivitas.hyperparameters, color = Color.White, modifier = Modifier.weight(1f))
                            }

                            // Tombol Edit dan Delete
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
                                            val intent = Intent(context, EditAktivitasActivity::class.java).apply {
                                                putExtra("id", aktivitas.id)
                                                putExtra("projectName", aktivitas.projectName)
                                                putExtra("modelType", aktivitas.modelType)
                                                putExtra("algorithmUsed", aktivitas.algorithmUsed)
                                                putExtra("hyperparameters", aktivitas.hyperparameters)
                                            }
                                            context.startActivity(intent)
                                        }
                                )
                                Text(
                                    "Delete",
                                    color = Color.Red,
                                    modifier = Modifier.clickable {
                                        viewModel.deleteAktivitas(aktivitas)
                                    }
                                )
                            }
                            Divider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Tambah Data
            Button(
                onClick = {
                    context.startActivity(Intent(context, AktivitasActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ADD AKTIVITAS", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bagian menu bawah hanya menampilkan ikon di tengah dengan background abu-abu dan rounded corner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kertas),
                    contentDescription = "Menu Kertas",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            // Ubah sesuai activity tujuan
                            context.startActivity(Intent(context, ListActivity::class.java))
                        }
                )
            }
        }
    }
}
