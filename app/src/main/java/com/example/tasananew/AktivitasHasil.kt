package com.example.tasananew

import android.app.Application
import android.content.Intent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
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

    var aktivitasToDelete by remember { mutableStateOf<com.example.tasananew.database.AktivitasEntity?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Daftar Aktivitas", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(
                    onClick = { dropdownExpanded = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
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
                                        aktivitasToDelete = aktivitas
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
                    context.startActivity(Intent(context, AktivitasActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ADD", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ Menu bawah dengan teks "MENU"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(12.dp))
                    .clickable {
                        context.startActivity(Intent(context, ListActivity::class.java))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("MENU", color = Color.White, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ✅ Dialog Konfirmasi Hapus
        aktivitasToDelete?.let { aktivitas ->
            AlertDialog(
                onDismissRequest = { aktivitasToDelete = null },
                title = {
                    Text("Konfirmasi Hapus", color = Color.White, fontSize = 20.sp)
                },
                text = {
                    Text("Apakah kamu yakin ingin menghapus data ini?", color = Color.White, fontSize = 16.sp)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteAktivitas(aktivitas)
                            aktivitasToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text("YA", fontSize = 16.sp)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { aktivitasToDelete = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text("BATAL", fontSize = 16.sp)
                    }
                },
                containerColor = Color(0xFF333D2E),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
