package com.example.tasananew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.ProjectEntitity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF6F765C)
                ) {
                    FormProject()
                }
            }
        }
    }
}

@Composable
fun FormProject() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val namaProject = remember { mutableStateOf("") }
    val modelUsed = remember { mutableStateOf("") }
    val deskripsi = remember { mutableStateOf("") }

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
            Text(
                "PROJECT",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = namaProject.value,
                onValueChange = { namaProject.value = it },
                label = { Text("Nama Project", color = Color.White) },
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

            OutlinedTextField(
                value = modelUsed.value,
                onValueChange = { modelUsed.value = it },
                label = { Text("Model yang digunakan", color = Color.White) },
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

            OutlinedTextField(
                value = deskripsi.value,
                onValueChange = { deskripsi.value = it },
                label = { Text("Deskripsi Project", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        val db = AppDatabase.getDatabase(context)
                        val projectDao = db.projectDao()

                        val project = ProjectEntitity(
                            name = namaProject.value,  // sesuai PK di entity
                            model = modelUsed.value,
                            description = deskripsi.value
                        )

                        withContext(Dispatchers.IO) {
                            projectDao.insertProject(project)
                        }

                        // Kosongkan form setelah save
                        namaProject.value = ""
                        modelUsed.value = ""
                        deskripsi.value = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SAVE", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProjectActivity() {
    FormProject()
}
