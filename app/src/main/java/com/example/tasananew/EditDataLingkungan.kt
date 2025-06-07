package com.example.tasananew

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.LingkunganEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditLingkunganActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("id", -1)
        val dao = AppDatabase.getDatabase(application).lingkunganDao()

        setContent {
            var projectName by remember { mutableStateOf("") }
            var os by remember { mutableStateOf("") }
            var cpu by remember { mutableStateOf("") }
            var ram by remember { mutableStateOf("") }
            var database by remember { mutableStateOf("") }

            val context = LocalContext.current

            // Ambil data awal dari database
            LaunchedEffect(id) {
                dao.getAll().observeForever { list ->
                    list.find { it.id == id }?.let { data ->
                        projectName = data.projectName
                        os = data.os
                        cpu = data.cpu
                        ram = data.ram
                        database = data.database
                    }
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
                    Text(
                        text = "Edit Data Lingkungan",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CustomTextField("Project Name", projectName) { projectName = it }
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField("OS", os) { os = it }
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField("CPU", cpu) { cpu = it }
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField("RAM", ram) { ram = it }
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField("Database", database) { database = it }
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val updated = LingkunganEntity(id, projectName, os, cpu, ram, database)
                                dao.update(updated)
                                launch(Dispatchers.Main) {
                                    Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = projectName.isNotBlank() && os.isNotBlank() && cpu.isNotBlank() && ram.isNotBlank() && database.isNotBlank()
                    ) {
                        Text(text = "Simpan Perubahan", color = Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFieldEdit(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
