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
import com.example.tasananew.database.CatatanEntitiy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatatanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF6F765C)
                ) {
                    FormCatatan { catatanEntity ->
                        // Save ke database Room
                        lifecycleScope.launch(Dispatchers.IO) {
                            val db = AppDatabase.getDatabase(applicationContext)
                            db.catatanDao().insertCatatan(catatanEntity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormCatatan(onSave: (CatatanEntitiy) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val suggest = remember { mutableStateOf("") }
    val selectedProject = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf("investigation") }
    val selectedStatus = remember { mutableStateOf("Problem") }

    val projectOptions = remember { mutableStateListOf<String>() }

    // Ambil nama-nama project dari database
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val projectDao = db.projectDao()

        val projects = withContext(Dispatchers.IO) {
            projectDao.getAllProjects()
        }

        projectOptions.clear()
        projectOptions.addAll(projects.map { it.name })

        if (projectOptions.isNotEmpty()) {
            selectedProject.value = projectOptions[0]
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
            Text("Catatan Pemeliharaan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))


            Text("Nama Project", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DropdownMenuBox(selected = selectedProject, options = projectOptions)

            Spacer(modifier = Modifier.height(8.dp))

            // Textfield Suggest
            OutlinedTextField(
                value = suggest.value,
                onValueChange = { suggest.value = it },
                label = { Text("Suggest", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Category
            Text("Category", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DropdownMenuBox(
                selected = selectedCategory,
                options = listOf("investigation", "comparison", "maintenance")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Status
            Text("Status", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DropdownMenuBox(
                selected = selectedStatus,
                options = listOf("Problem", "Update", "Repair")
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val catatan = CatatanEntitiy(
                        projectName = selectedProject.value,
                        suggest = suggest.value,
                        category = selectedCategory.value,
                        status = selectedStatus.value
                    )
                    onSave(catatan)

                    // Reset form
                    suggest.value = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SAVE", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun DropdownMenuBox(selected: MutableState<String>, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(5.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Text(selected.value.ifEmpty { "Pilih" }, color = Color.White)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        selected.value = option
                        expanded = false
                    }
                )
            }
        }
    }
}
