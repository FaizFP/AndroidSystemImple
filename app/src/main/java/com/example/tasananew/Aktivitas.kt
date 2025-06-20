package com.example.tasananew

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
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
import com.example.tasananew.database.AktivitasEntity
import com.example.tasananew.database.AktivitasViewModel
import com.example.tasananew.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InputAktivitasScreen(viewModel: AktivitasViewModel) {
    val context = LocalContext.current
    val selectedProject = remember { mutableStateOf("") }
    val projectOptions = remember { mutableStateListOf<String>() }

    var modelType by remember { mutableStateOf("") }
    var algorithmUsed by remember { mutableStateOf("") }
    var hyperparameters by remember { mutableStateOf("") }

    // Ambil nama-nama project dari database saat pertama kali tampil
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val projects = withContext(Dispatchers.IO) {
            db.projectDao().getAllProjects()
        }
        projectOptions.clear()
        projectOptions.addAll(projects.map { it.name })
        if (projectOptions.isNotEmpty()) {
            selectedProject.value = projectOptions[0]
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C))
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("AKTIVITAS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333D2E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nama Project", color = Color.White)
                DropdownMenuBox(selected = selectedProject, options = projectOptions)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = modelType,
                    onValueChange = { modelType = it },
                    label = { Text("Model Type") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = algorithmUsed,
                    onValueChange = { algorithmUsed = it },
                    label = { Text("Algorithm Used") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = hyperparameters,
                    onValueChange = { hyperparameters = it },
                    label = { Text("Hyperparameters") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val aktivitas = AktivitasEntity(
                            projectName = selectedProject.value,
                            modelType = modelType,
                            algorithmUsed = algorithmUsed,
                            hyperparameters = hyperparameters
                        )
                        viewModel.insertAktivitas(aktivitas)
                        Toast.makeText(context, "Aktivitas disimpan", Toast.LENGTH_SHORT).show()

                        // Tutup layar setelah simpan
                        (context as Activity).finish()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = modelType.isNotEmpty() && algorithmUsed.isNotEmpty() && hyperparameters.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("SAVE")
                }
            }
        }
    }
}
