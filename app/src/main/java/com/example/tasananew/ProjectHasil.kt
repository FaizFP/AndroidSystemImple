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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasananew.database.ProjectEntitity
import com.example.tasananew.database.ProjectViewModel
import com.example.tasananew.database.ProjectViewModelFactory

@Composable
fun ProjectHasilScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = ProjectViewModelFactory(application)
    val viewModel: ProjectViewModel = viewModel(factory = factory)

    val projectList by viewModel.projects.observeAsState(emptyList())
    var projectSelectedFilter by remember { mutableStateOf("Semua") }
    val projectFilterOptions = listOf("Semua", "Model", "Deskripsi")
    var projectToDelete by remember { mutableStateOf<ProjectEntitity?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Daftar Proyek", fontSize = 20.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                ProjectFilterDropdownMenu(
                    options = projectFilterOptions,
                    selectedOption = projectSelectedFilter,
                    onOptionSelected = { projectSelectedFilter = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (projectList.isEmpty()) {
                Text("Belum ada data proyek.", color = Color.White)
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nama", color = Color.White, modifier = Modifier.weight(1f))

                    if (projectSelectedFilter == "Semua" || projectSelectedFilter == "Model")
                        Text("Model", color = Color.White, modifier = Modifier.weight(1f))

                    if (projectSelectedFilter == "Semua" || projectSelectedFilter == "Deskripsi")
                        Text("Deskripsi", color = Color.White, modifier = Modifier.weight(2f))

                    Spacer(modifier = Modifier.width(120.dp))
                }

                Divider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(projectList) { project: ProjectEntitity ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(project.name, color = Color.White, modifier = Modifier.weight(1f))

                                if (projectSelectedFilter == "Semua" || projectSelectedFilter == "Model")
                                    Text(project.model, color = Color.White, modifier = Modifier.weight(1f))

                                if (projectSelectedFilter == "Semua" || projectSelectedFilter == "Deskripsi")
                                    Text(project.description, color = Color.White, modifier = Modifier.weight(2f))
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text("Edit", color = Color.Cyan, modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        val intent = Intent(context, EditProjectActivity::class.java)
                                        intent.putExtra("name", project.name)
                                        intent.putExtra("model", project.model)
                                        intent.putExtra("description", project.description)
                                        context.startActivity(intent)
                                    })

                                Text("Delete", color = Color.Red, modifier = Modifier.clickable {
                                    projectToDelete = project
                                })
                            }

                            Divider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.fetchProjectAndActivityFromApi() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("FETCH DATA API", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { context.startActivity(Intent(context, ProjectActivity::class.java)) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ADD", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // GANTI ICON KERTAS dengan TULISAN MENU
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
                Text(
                    text = "MENU",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // AlertDialog Konfirmasi Hapus
        projectToDelete?.let { project ->
            AlertDialog(
                onDismissRequest = { projectToDelete = null },
                title = {
                    Text("Konfirmasi Hapus", color = Color.White, fontSize = 20.sp)
                },
                text = {
                    Text("Apakah kamu yakin ingin menghapus data ini?", color = Color.White, fontSize = 16.sp)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteProject(project)
                            projectToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text("YA", fontSize = 16.sp)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { projectToDelete = null },
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

@Composable
fun ProjectFilterDropdownMenu(
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
