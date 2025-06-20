package com.example.tasananew

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.LingkunganEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LingkunganInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil nama project dari intent
        val projectNameFromIntent = intent.getStringExtra("projectName") ?: ""

        setContent {
            Surface(color = Color(0xFF6F765C)) {
                LingkunganInputScreen(initialProjectName = projectNameFromIntent) { lingkungan ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            val db = AppDatabase.getDatabase(applicationContext)
                            db.lingkunganDao().insert(lingkungan)
                        }

                        // Setelah tersimpan, pindah ke InputCatatanActivity
                        Toast.makeText(applicationContext, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LingkunganInputActivity, InputCatatanActivity::class.java).apply {
                            putExtra("projectName", lingkungan.projectName)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun LingkunganInputScreen(
    initialProjectName: String,
    onSave: (LingkunganEntity) -> Unit
) {
    val context = LocalContext.current

    var os by remember { mutableStateOf("") }
    var cpu by remember { mutableStateOf("") }
    var ram by remember { mutableStateOf("") }
    var dbSystem by remember { mutableStateOf("") }

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
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Input Data Lingkungan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nama Project", modifier = Modifier.align(Alignment.Start), color = Color.White)
            Text(
                text = initialProjectName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(Color.DarkGray, RoundedCornerShape(5.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            LingkunganTextField("Operating System", os) { os = it }
            Spacer(modifier = Modifier.height(8.dp))

            LingkunganTextField("CPU (Core)", cpu) { cpu = it }
            Spacer(modifier = Modifier.height(8.dp))

            LingkunganTextField("RAM", ram) { ram = it }
            Spacer(modifier = Modifier.height(8.dp))

            LingkunganTextField("Database", dbSystem) { dbSystem = it }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val trimmedOs = os.trim()
                    val trimmedCpu = cpu.trim()
                    val trimmedRam = ram.trim()
                    val trimmedDb = dbSystem.trim()

                    if (trimmedOs.isNotEmpty() && trimmedCpu.isNotEmpty() && trimmedRam.isNotEmpty() && trimmedDb.isNotEmpty()) {
                        onSave(
                            LingkunganEntity(
                                projectName = initialProjectName,
                                os = trimmedOs,
                                cpu = trimmedCpu,
                                ram = trimmedRam,
                                database = trimmedDb
                            )
                        )
                    } else {
                        Toast.makeText(context, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                modifier = Modifier.fillMaxWidth(),
                enabled = os.isNotBlank() && cpu.isNotBlank() && ram.isNotBlank() && dbSystem.isNotBlank()
            ) {
                Text("NEXT", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LingkunganTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.White)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                containerColor = Color.DarkGray
            )
        )
    }
}
