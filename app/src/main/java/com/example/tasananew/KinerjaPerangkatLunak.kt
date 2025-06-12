package com.example.tasananew

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.tasananew.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Data class untuk item metric
data class MetricItem(val title: String, val value: String, val icon: ImageVector)

@Composable
fun KinerjaPerangkatLunakScreen() {
    val context = LocalContext.current

    // State untuk pilihan dropdown dan list opsi project
    val selectedProject = remember { mutableStateOf("") }
    val projectOptions = remember { mutableStateListOf<String>() }

    // Load project list dari Room database saat screen muncul
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

    val metrics = listOf(
        MetricItem("Respon Time", "10 Ms", Icons.Filled.AccessTime),
        MetricItem("Uptime", "90 %", Icons.Filled.SignalCellular4Bar),
        MetricItem("Usage", "2 %", Icons.Filled.Storage),
        MetricItem("Error Rate", "2.2 %", Icons.Filled.Warning)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF2F3E2F) // Dark green background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dropdown pilih project dari database dengan background putih
            DropdownSelectorMutable(
                selected = selectedProject,
                options = projectOptions
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Metric cards
            metrics.forEach {
                MetricCard(metric = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun DropdownSelectorMutable(selected: MutableState<String>, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.White, // Ubah jadi putih
                contentColor = Color.Black
            )
        ) {
            Text(text = selected.value, color = Color.Black)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selected.value = option
                        expanded = false
                    }
                ) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun MetricCard(metric: MetricItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        backgroundColor = Color(0xFFE0E0E0), // Light gray
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = metric.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = metric.title,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(text = metric.value, color = Color.Black)
        }
    }
}
