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

    var selectedFilter by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Model", "Deskripsi")

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Daftar Proyek", fontSize = 20.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown menu seperti di LingkunganScreen
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                ProjectFilterMenu(
                    options = filterOptions,
                    selectedOption = selectedFilter,
                    onOptionSelected = { selectedFilter = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (projectList.isEmpty()) {
                Text("Belum ada data proyek.", color = Color.White)
            } else {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nama", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Model")
                        Text("Model", color = Color.White, modifier = Modifier.weight(1f))
                    if (selectedFilter == "Semua" || selectedFilter == "Deskripsi")
                        Text("Deskripsi", color = Color.White, modifier = Modifier.weight(2f))
                    Spacer(modifier = Modifier.width(50.dp))
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

                                if (selectedFilter == "Semua" || selectedFilter == "Model")
                                    Text(project.model, color = Color.White, modifier = Modifier.weight(1f))
                                if (selectedFilter == "Semua" || selectedFilter == "Deskripsi")
                                    Text(project.description, color = Color.White, modifier = Modifier.weight(2f))
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
                                            val intent = Intent(context, EditProjectActivity::class.java)
                                            intent.putExtra("name", project.name)
                                            intent.putExtra("model", project.model)
                                            intent.putExtra("description", project.description)
                                            context.startActivity(intent)
                                        }
                                )
                                Text(
                                    "Delete",
                                    color = Color.Red,
                                    modifier = Modifier.clickable {
                                        viewModel.deleteProject(project)
                                    }
                                )
                            }

                            Divider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }

            RoundedNextButton("ADD PROJEK") {
                context.startActivity(Intent(context, ProjectActivity::class.java))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 16.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kertas),
                        contentDescription = "Menu Kertas",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                context.startActivity(Intent(context, ListActivity::class.java))
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectFilterMenu(
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

@Composable
fun RoundedNextButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onClick() }
            .background(Color.LightGray, shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
        )
    }
}
