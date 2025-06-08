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
import com.example.tasananew.database.ProjectEntitity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProjectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: ""
        val initialModel = intent.getStringExtra("model") ?: ""
        val initialDescription = intent.getStringExtra("description") ?: ""

        setContent {
            var model by remember { mutableStateOf(initialModel) }
            var description by remember { mutableStateOf(initialDescription) }

            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()) {

                Text("Edit Proyek", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = name,
                    onValueChange = {},
                    label = { Text("Nama (tidak bisa diubah)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = AppDatabase.getDatabase(applicationContext).projectDao()
                        dao.update(ProjectEntitity(name = name, model = model, description = description))
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Proyek berhasil diperbarui", Toast.LENGTH_SHORT).show()
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
