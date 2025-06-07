package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.LingkunganEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults

class DataLingkunganActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataLingkunganScreen { lingkungan ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.lingkunganDao().insert(lingkungan)
                }
            }
        }
    }
}

@Composable
fun DataLingkunganScreen(onSave: (LingkunganEntity) -> Unit) {
    val context = LocalContext.current

    var selectedProject by remember { mutableStateOf("") }
    var os by remember { mutableStateOf("") }
    var cpu by remember { mutableStateOf("") }
    var ram by remember { mutableStateOf("") }
    var database by remember { mutableStateOf("") }

    val projectOptions = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val projects = withContext(Dispatchers.IO) {
            db.projectDao().getAllProjects()
        }
        projectOptions.clear()
        projectOptions.addAll(projects.map { it.name })

        if (projectOptions.isNotEmpty()) {
            selectedProject = projectOptions[0]
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF333D2E), RoundedCornerShape(16.dp))
                .padding(24.dp)
                .width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Data Lingkungan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nama Project", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DropdownMenuBox(
                selected = selectedProject,
                options = projectOptions,
                onSelected = { selectedProject = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField("Operating System", os) { os = it }
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField("CPU (Core)", cpu) { cpu = it }
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField("RAM", ram) { ram = it }
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField("Database", database) { database = it }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(
                        LingkunganEntity(
                            projectName = selectedProject,
                            os = os,
                            cpu = cpu,
                            ram = ram,
                            database = database
                        )
                    )
                    // Reset form
                    selectedProject = projectOptions.firstOrNull().orEmpty()
                    os = ""
                    cpu = ""
                    ram = ""
                    database = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedProject.isNotBlank() && os.isNotBlank() && cpu.isNotBlank() && ram.isNotBlank() && database.isNotBlank()
            ) {
                Text("SAVE", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(5.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Text(text = if (selected.isNotEmpty()) selected else "Pilih", color = Color.White)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, color = Color.White)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                containerColor = Color.DarkGray
            )
        )
    }
}
