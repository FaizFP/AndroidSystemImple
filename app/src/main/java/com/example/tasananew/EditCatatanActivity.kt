package com.example.tasananew

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.CatatanEntitiy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCatatanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Terima data dari intent, termasuk id primary key untuk update
        val id = intent.getIntExtra("id", 0)
        val projectName = intent.getStringExtra("projectName") ?: ""
        var suggest = intent.getStringExtra("suggest") ?: ""
        var category = intent.getStringExtra("category") ?: ""
        var status = intent.getStringExtra("status") ?: ""
        var startDate = intent.getStringExtra("startDate") ?: ""
        var endDate = intent.getStringExtra("endDate") ?: ""
        var namaPemangkuKepentingan = intent.getStringExtra("namaPemangkuKepentingan") ?: ""
        var namaPeran = intent.getStringExtra("namaPeran") ?: ""

        setContent {
            var suggestState by remember { mutableStateOf(suggest) }
            var categoryState by remember { mutableStateOf(category) }
            var statusState by remember { mutableStateOf(status) }
            var startDateState by remember { mutableStateOf(startDate) }
            var endDateState by remember { mutableStateOf(endDate) }
            var namaPemangkuKepentinganState by remember { mutableStateOf(namaPemangkuKepentingan) }
            var namaPeranState by remember { mutableStateOf(namaPeran) }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text("Edit Catatan", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = projectName,
                    onValueChange = {},
                    label = { Text("Proyek (tidak bisa diubah)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                OutlinedTextField(
                    value = suggestState,
                    onValueChange = { suggestState = it },
                    label = { Text("Saran") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = categoryState,
                    onValueChange = { categoryState = it },
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = statusState,
                    onValueChange = { statusState = it },
                    label = { Text("Status") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = startDateState,
                    onValueChange = { startDateState = it },
                    label = { Text("Mulai") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = endDateState,
                    onValueChange = { endDateState = it },
                    label = { Text("Selesai") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = namaPemangkuKepentinganState,
                    onValueChange = { namaPemangkuKepentinganState = it },
                    label = { Text("Pemangku Kepentingan") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = namaPeranState,
                    onValueChange = { namaPeranState = it },
                    label = { Text("Peran") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = AppDatabase.getDatabase(applicationContext).catatanDao()

                        // Buat objek CatatanEntitiy dengan id untuk update
                        val catatan = CatatanEntitiy(
                            id = id,
                            projectName = projectName,
                            suggest = suggestState,
                            category = categoryState,
                            status = statusState,
                            startDate = startDateState,
                            endDate = endDateState,
                            namaPemangkuKepentingan = namaPemangkuKepentinganState,
                            namaPeran = namaPeranState
                        )

                        dao.updateCatatan(catatan)

                        runOnUiThread {
                            Toast.makeText(applicationContext, "Catatan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }) {
                    Text("Simpan")
                }
            }
        }
    }
}
