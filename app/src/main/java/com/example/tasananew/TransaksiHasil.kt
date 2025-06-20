package com.example.tasananew

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasananew.database.TransaksiViewModel
import com.example.tasananew.database.TransaksiViewModelFactory
import com.example.tasananew.database.TransaksiEntity
import java.io.File

@Composable
fun TransaksiHasilScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as Application
    val viewModel: TransaksiViewModel = viewModel(factory = TransaksiViewModelFactory(app))
    val transaksiList by viewModel.transaksiList.observeAsState(emptyList())

    var selectedFilter by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Deskripsi Data", "Foto/File")
    var transaksiToDelete by remember { mutableStateOf<TransaksiEntity?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF2F3E2F)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Daftar Transaksi", fontSize = 20.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                FilterDropdownMenu(
                    options = filterOptions,
                    selectedOption = selectedFilter,
                    onOptionSelected = { selectedFilter = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (transaksiList.isEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text("Belum ada data transaksi.", color = Color.White)
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("ID", Modifier.weight(0.5f), color = Color.White)
                    Text("Project", Modifier.weight(1f), color = Color.White)

                    if (selectedFilter == "Semua" || selectedFilter == "Deskripsi Data") {
                        Text("Deskripsi Data", Modifier.weight(1f), color = Color.White)
                    }

                    if (selectedFilter == "Semua" || selectedFilter == "Foto/File") {
                        Text("Foto/File", Modifier.weight(1f), color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(55.dp))
                }
                Divider(color = Color.White)

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(transaksiList) { transaksi ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(transaksi.id.toString(), Modifier.weight(0.5f), color = Color.White)
                                Text(transaksi.projectName, Modifier.weight(1f), color = Color.White)

                                if (selectedFilter == "Semua" || selectedFilter == "Deskripsi Data") {
                                    Text(transaksi.inputData, Modifier.weight(1f), color = Color.White)
                                }

                                if (selectedFilter == "Semua" || selectedFilter == "Foto/File") {
                                    Text(File(transaksi.photoFileName).name, Modifier.weight(1f), color = Color.White)
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text("Edit", color = Color.Cyan, modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        Intent(context, EditTransaksiActivity::class.java).apply {
                                            putExtra("id", transaksi.id)
                                            putExtra("projectName", transaksi.projectName)
                                            putExtra("inputData", transaksi.inputData)
                                            putExtra("photoFileName", transaksi.photoFileName)
                                            context.startActivity(this)
                                        }
                                    })

                                Text("Delete", color = Color.Red, modifier = Modifier.clickable {
                                    transaksiToDelete = transaksi
                                })
                            }
                            Divider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { context.startActivity(Intent(context, TransaksiActivity::class.java)) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ADD", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(20.dp))
            CustomClickableBox()
            Spacer(Modifier.height(20.dp))
        }

        // AlertDialog konfirmasi hapus
        transaksiToDelete?.let { transaksi ->
            AlertDialog(
                onDismissRequest = { transaksiToDelete = null },
                title = { Text("Konfirmasi Hapus", color = Color.White, fontSize = 20.sp) },
                text = { Text("Apakah kamu yakin ingin menghapus data ini?", color = Color.White, fontSize = 16.sp) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTransaksi(transaksi)
                            transaksiToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text("YA", fontSize = 16.sp)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { transaksiToDelete = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text("BATAL", fontSize = 16.sp)
                    }
                },
                containerColor = Color(0xFF333D2E),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun FilterDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CustomClickableBox() {
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

        Text(
            text = "MENU",
            color = Color.White,
            fontSize = 20.sp
        )
    }
}
