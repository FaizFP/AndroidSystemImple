package com.example.tasananew

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UpdateProjectScreen() {
    var selectedProject by remember { mutableStateOf("") }
    val projectOptions = listOf("Project A", "Project B", "Project C")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Handle back navigation */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Update Project", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dropdown (Pilih Project)
        Text(text = "Pilih Project", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true }) {
                Text(text = if (selectedProject.isEmpty()) "Pick an option" else selectedProject)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                projectOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedProject = option
                        expanded = false
                    }, text = { Text(option) })
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Update buttons
        UpdateItem("CATATAN PEMELIHARAAN")
        UpdateItem("DATA LINGKUNGAN")
        UpdateItem("DATA TRANSAKSI INPUT DAN OUTPUT")
    }
}

@Composable
fun UpdateItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp)
        Button(onClick = { /* TODO: Handle Update */ }) {
            Text("Update")
        }
    }
}
