package com.example.tasananew

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.CatatanEntitiy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class InputCatatanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val projectNameFromIntent = intent.getStringExtra("projectName") ?: ""

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF6F765C)
                ) {
                    FormInputCatatan(
                        projectName = projectNameFromIntent,
                        onSave = { catatanEntity ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                val db = AppDatabase.getDatabase(applicationContext)
                                db.catatanDao().insertCatatan(catatanEntity)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Data berhasil disimpan",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@InputCatatanActivity, InputTransaksiActivity::class.java)
                                    intent.putExtra("projectName", catatanEntity.projectName)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FormInputCatatan(
    projectName: String,
    onSave: (CatatanEntitiy) -> Unit
) {
    val inputSuggest = remember { mutableStateOf("") }
    val inputCategory = remember { mutableStateOf("investigation") }
    val inputStatus = remember { mutableStateOf("ONGOING") }
    val dateStart = remember { mutableStateOf("") }
    val dateEnd = remember { mutableStateOf("") }
    val inputPemangku = remember { mutableStateOf("") }
    val inputPeran = remember { mutableStateOf("") }

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
            Text("Catatan Pemeliharaan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nama Project", modifier = Modifier.align(Alignment.Start), color = Color.White)
            Text(
                text = projectName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(Color.DarkGray, RoundedCornerShape(5.dp))
                    .padding(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = inputSuggest.value,
                onValueChange = { inputSuggest.value = it },
                label = { Text("Saran", color = Color.White) },
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

            Text("Kategori", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DropdownMenuBoxInput(selected = inputCategory, options = listOf("investigation", "comparison", "maintenance"))
            Spacer(modifier = Modifier.height(8.dp))

            Text("Status", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DropdownMenuBoxInput(selected = inputStatus, options = listOf("ONGOING", "PENDING", "DONE"))
            Spacer(modifier = Modifier.height(8.dp))

            Text("Tanggal Mulai", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DatePickerFieldInput("Pilih Tanggal Mulai", selectedDate = dateStart)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Tanggal Selesai", modifier = Modifier.align(Alignment.Start), color = Color.White)
            DatePickerFieldInput("Pilih Tanggal Selesai", selectedDate = dateEnd)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = inputPemangku.value,
                onValueChange = { inputPemangku.value = it },
                label = { Text("Nama Pemangku Kepentingan", color = Color.White) },
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
                value = inputPeran.value,
                onValueChange = { inputPeran.value = it },
                label = { Text("Nama Peran", color = Color.White) },
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
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val catatan = CatatanEntitiy(
                        projectName = projectName,
                        suggest = inputSuggest.value,
                        category = inputCategory.value,
                        status = inputStatus.value,
                        startDate = dateStart.value,
                        endDate = dateEnd.value,
                        namaPemangkuKepentingan = inputPemangku.value,
                        namaPeran = inputPeran.value
                    )
                    onSave(catatan)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("NEXT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DropdownMenuBoxInput(selected: MutableState<String>, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(5.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Text(selected.value.ifEmpty { "Pilih" }, color = Color.White)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        selected.value = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerFieldInput(label: String, selectedDate: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(context, { _, y, m, d ->
            selectedDate.value = "$d/${m + 1}/$y"
        }, year, month, day)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(5.dp))
            .clickable { datePickerDialog.show() }
            .padding(12.dp)
    ) {
        Text(
            if (selectedDate.value.isEmpty()) label else selectedDate.value,
            color = Color.White
        )
    }
}
