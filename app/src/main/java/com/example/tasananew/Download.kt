package com.example.tasananew

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasananew.database.LaporanViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GeneratePdfScreen(viewModel: LaporanViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }
    var selectedProjectName by remember { mutableStateOf("") }
    var projectList by remember { mutableStateOf(listOf<String>()) }

    // Ambil semua nama project
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            projectList = viewModel.getAllProjectNames()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C)) // background utama seperti TransaksiScreen
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "DOWNLOAD LAPORAN",
                fontSize = 24.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333D2E)) // warna kartu seperti TransaksiScreen
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Pilih Project", color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray, RoundedCornerShape(4.dp))
                        .clickable { expanded = true }
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (selectedProjectName.isEmpty()) "-- Pilih Project --" else selectedProjectName,
                        color = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    projectList.forEach { project ->
                        DropdownMenuItem(
                            text = { Text(project) },
                            onClick = {
                                selectedProjectName = project
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (selectedProjectName.isNotBlank()) {
                            viewModel.generatePdfFromNamaProject(context, selectedProjectName)
                        } else {
                            Toast.makeText(context, "Silakan pilih project terlebih dahulu", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("DOWNLOAD PDF")
                }
            }
        }
    }
}
