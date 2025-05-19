package com.example.tasananew

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasananew.database.ProjectViewModel
import com.example.tasananew.database.ProjectViewModelFactory
import androidx.compose.material3.Surface


@Composable
fun ProjectHasilScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = ProjectViewModelFactory(application)
    val viewModel: ProjectViewModel = viewModel(factory = factory)

    val projectList by viewModel.projects.observeAsState(emptyList())

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Daftar Proyek", color = Color.White)

            if (projectList.isEmpty()) {
                Text("Belum ada data proyek.", color = Color.White)
            } else {
                LazyColumn {
                    items(projectList) { project ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Nama: ${project.name}", color = Color.White)
                            Text("Model: ${project.model}", color = Color.White)
                            Text("Deskripsi: ${project.description}", color = Color.White)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
