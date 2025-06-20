package com.example.tasananew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.example.tasananew.database.AktivitasEntity
import com.example.tasananew.database.AktivitasViewModel
import com.example.tasananew.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InputAktivitasActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil data projectName dari Intent
        val projectName = intent.getStringExtra("projectName") ?: ""

        // Inisialisasi ViewModel sesuai kebutuhanmu
        val viewModel = AktivitasViewModel(application) // Ubah sesuai constructor ViewModelmu

        setContent {
            InputAktivitasScreen(
                viewModel = viewModel,
                initialProjectName = projectName
            )
        }
    }

    @Composable
    fun InputAktivitasScreen(
        viewModel: AktivitasViewModel,
        initialProjectName: String = ""
    ) {
        val context = LocalContext.current
        val activity = context as? Activity
        val coroutineScope = rememberCoroutineScope()

        val selectedProjectForm = remember { mutableStateOf(initialProjectName) }
        val projectOptionsForm = remember { mutableStateListOf<String>() }

        var modelTypeForm by remember { mutableStateOf("") }
        var algorithmUsedForm by remember { mutableStateOf("") }
        var hyperparametersForm by remember { mutableStateOf("") }

        // Ambil daftar project dari database sekali saat Compose dimulai
        LaunchedEffect(Unit) {
            val db = AppDatabase.getDatabase(context)
            val projects = withContext(Dispatchers.IO) {
                db.projectDao().getAllProjects()
            }
            projectOptionsForm.clear()
            projectOptionsForm.addAll(projects.map { it.name })

            if (initialProjectName.isEmpty() || !projectOptionsForm.contains(initialProjectName)) {
                selectedProjectForm.value = projectOptionsForm.firstOrNull() ?: ""
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
                    ProjectDropdownMenuBox(selected = selectedProjectForm, options = projectOptionsForm)

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = modelTypeForm,
                        onValueChange = { modelTypeForm = it },
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
                        value = algorithmUsedForm,
                        onValueChange = { algorithmUsedForm = it },
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
                        value = hyperparametersForm,
                        onValueChange = { hyperparametersForm = it },
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
                            coroutineScope.launch {
                                val aktivitas = AktivitasEntity(
                                    projectName = selectedProjectForm.value,
                                    modelType = modelTypeForm,
                                    algorithmUsed = algorithmUsedForm,
                                    hyperparameters = hyperparametersForm
                                )
                                viewModel.insertAktivitas(aktivitas)
                                Toast.makeText(context, "Aktivitas disimpan", Toast.LENGTH_SHORT).show()

                                // Pindah ke ListActivity setelah simpan
                                context.startActivity(Intent(context, ListActivity::class.java))

                                // Tutup activity ini
                                activity?.finish()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = modelTypeForm.isNotEmpty() && algorithmUsedForm.isNotEmpty() && hyperparametersForm.isNotEmpty(),
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

    @Composable
    fun ProjectDropdownMenuBox(selected: MutableState<String>, options: List<String>) {
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedTextField(
                value = selected.value,
                onValueChange = { },
                readOnly = true,
                label = { Text("Project") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
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
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selected.value = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
