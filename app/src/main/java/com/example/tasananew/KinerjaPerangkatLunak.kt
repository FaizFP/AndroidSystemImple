package com.example.tasananew

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasananew.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Data class untuk metric
data class MetricItem(val title: String, val value: String, val icon: ImageVector)

@Composable
fun KinerjaPerangkatLunakScreen() {
    val context = LocalContext.current

    // Dropdown project
    val selectedProject = remember { mutableStateOf("") }
    val projectOptions = remember { mutableStateListOf<String>() }

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

    // Daftar metric
    val metrics = listOf(
        MetricItem("Respon Time", "10 Ms", Icons.Filled.AccessTime),
        MetricItem("Uptime", "90 %", Icons.Filled.SignalCellular4Bar),
        MetricItem("Usage", "2 %", Icons.Filled.Storage),
        MetricItem("Error Rate", "2.2 %", Icons.Filled.Warning)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF2F3E2F)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Kinerja Perangkat Lunak", fontSize = 20.sp, color = Color.White)

            Spacer(modifier = Modifier.height(12.dp))

            // Dropdown project
            DropdownSelectorMutable(
                selected = selectedProject,
                options = projectOptions
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tampilkan card metric
            metrics.forEach {
                MetricCard(metric = it)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu bawah (box klik icon kertas)
            CustomKinerjaClickableBox()
            Spacer(modifier = Modifier.height(20.dp))
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
                backgroundColor = Color.White,
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
        backgroundColor = Color(0xFFE0E0E0),
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

@Composable
fun CustomKinerjaClickableBox() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                context.startActivity(Intent(context, ListActivity::class.java))
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.kertas),
            contentDescription = "Menu Kertas",
            modifier = Modifier.size(50.dp)
        )
    }
}
