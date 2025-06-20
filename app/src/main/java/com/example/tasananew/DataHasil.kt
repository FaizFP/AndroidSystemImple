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

    var selectedLingkunganFilter by remember { mutableStateOf("Semua") }
    val lingkunganFilterOptions = listOf("Semua", "OS", "CPU", "RAM", "DB")

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text("Data Lingkungan", fontSize = 20.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                LingkunganFilterMenu(
                    options = lingkunganFilterOptions,
                    selectedOption = selectedLingkunganFilter,
                    onOptionSelected = { selectedLingkunganFilter = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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

                    if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "OS")
                        Text("OS", color = Color.White, modifier = Modifier.weight(1f))

                    if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "CPU")
                        Text("CPU", color = Color.White, modifier = Modifier.weight(1f))

                    if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "RAM")
                        Text("RAM", color = Color.White, modifier = Modifier.weight(1f))

                    if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "DB")
                        Text("DB", color = Color.White, modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.width(60.dp))
                }

                Divider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(lingkunganList) { item: LingkunganEntity ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.projectName, color = Color.White, modifier = Modifier.weight(1f))

                            if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "OS")
                                Text(item.os, color = Color.White, modifier = Modifier.weight(1f))

                            if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "CPU")
                                Text(item.cpu, color = Color.White, modifier = Modifier.weight(1f))

                            if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "RAM")
                                Text(item.ram, color = Color.White, modifier = Modifier.weight(1f))

                            if (selectedLingkunganFilter == "Semua" || selectedLingkunganFilter == "DB")
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
                        Divider(color = Color.White.copy(alpha = 0.2f))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { context.startActivity(Intent(context, DataLingkunganActivity::class.java)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ADD DATALINGKUNGAN", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(20.dp))

            // Box klik ke ListActivity
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
                Image(
                    painter = painterResource(id = R.drawable.kertas),
                    contentDescription = "Menu Kertas",
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun LingkunganFilterMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
