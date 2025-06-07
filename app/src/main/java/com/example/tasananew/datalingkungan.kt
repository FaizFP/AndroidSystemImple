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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.LingkunganEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    var selectedProject by remember { mutableStateOf("") }
    var os by remember { mutableStateOf("") }
    var cpu by remember { mutableStateOf("") }
    var ram by remember { mutableStateOf("") }
    var database by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C))
            .padding(16.dp)
    ) {
        Text(
            text = "Data Lingkungan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProjectDropdown(selectedProject = selectedProject, onProjectSelected = { selectedProject = it })

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField("Operating System", os) { os = it }
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField("CPU (Core)", cpu) { cpu = it }
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField("RAM", ram) { ram = it }
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField("Database", database) { database = it }
        Spacer(modifier = Modifier.height(24.dp))

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
                // Reset input
                selectedProject = ""
                os = ""
                cpu = ""
                ram = ""
                database = ""
            },
            enabled = selectedProject.isNotBlank() && os.isNotBlank() && cpu.isNotBlank() && ram.isNotBlank() && database.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "SAVE")
        }
    }
}

@Composable
fun ProjectDropdown(selectedProject: String, onProjectSelected: (String) -> Unit) {
    val context = LocalContext.current
    val projectOptions = remember { mutableStateListOf<String>() }
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val db = AppDatabase.getDatabase(context)
            val projects = withContext(Dispatchers.IO) {
                db.projectDao().getAllProjects()
            }
            projectOptions.clear()
            projectOptions.addAll(projects.map { it.name })
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(5.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Text(
            text = if (selectedProject.isNotEmpty()) selectedProject else "Pilih Project",
            color = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            projectOptions.forEach { project ->
                DropdownMenuItem(
                    text = { Text(project, color = Color.White) },
                    onClick = {
                        onProjectSelected(project)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, color = Color.White)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                cursorColor = Color.White
            )
        )
    }
}
