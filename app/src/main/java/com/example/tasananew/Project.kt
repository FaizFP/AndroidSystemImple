package com.example.tasananew

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF6F765C), RoundedCornerShape(16.dp))
                .padding(24.dp)
                .fillMaxWidth(),
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
                    val nama = namaProject.value.trim()
                    val model = modelUsed.value.trim()
                    val deskripsiText = deskripsi.value.trim()

                    if (nama.isNotEmpty() && model.isNotEmpty() && deskripsiText.isNotEmpty()) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                val db = AppDatabase.getDatabase(context)
                                db.projectDao().insertProject(
                                    ProjectEntitity(
                                        name = nama,
                                        model = model,
                                        description = deskripsiText
                                    )
                                )
                            }

                            Toast.makeText(context, "Project berhasil disimpan!", Toast.LENGTH_SHORT).show()

                            // Pindah ke halaman berikutnya
                            val intent = Intent(context, LingkunganInputActivity::class.java).apply {
                                putExtra("projectName", nama)
                            }
                            context.startActivity(intent)
                        }
                    } else {
                        Toast.makeText(context, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth(),
                enabled = namaProject.value.isNotBlank() &&
                        modelUsed.value.isNotBlank() &&
                        deskripsi.value.isNotBlank()
            ) {
                Text("NEXT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        // Menu bawah
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
                            context.startActivity(
                                Intent(context, ListActivity::class.java)
                            )
                        }
                )
            }
        }
    }
}
