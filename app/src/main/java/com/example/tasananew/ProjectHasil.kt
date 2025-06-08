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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Daftar Proyek", fontSize = 20.sp, color = Color.White)

            if (projectList.isEmpty()) {
                Text("Belum ada data proyek.", color = Color.White)
            } else {
                // Header Tabel
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nama", color = Color.White, modifier = Modifier.weight(1f))
                    Text("Model", color = Color.White, modifier = Modifier.weight(1f))
                    Text("Deskripsi", color = Color.White, modifier = Modifier.weight(2f))
                    Spacer(modifier = Modifier.width(50.dp))
                }

                HorizontalDivider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(projectList) { project: ProjectEntitity ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = project.name, color = Color.White, modifier = Modifier.weight(1f))
                                Text(text = project.model, color = Color.White, modifier = Modifier.weight(1f))
                                Text(text = project.description, color = Color.White, modifier = Modifier.weight(2f))
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

                            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }

            RoundedNextButton("ADD") {
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
