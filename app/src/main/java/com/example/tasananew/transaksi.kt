package com.example.tasananew

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.TransaksiEntity
import com.example.tasananew.database.TransaksiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransaksiScreen(viewModel: TransaksiViewModel) {
    val context = LocalContext.current
    var inputData by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var tempFileUri by remember { mutableStateOf<Uri?>(null) }
    val photoUri = remember { mutableStateOf<Uri?>(null) }

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

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            tempFileUri = it
            Toast.makeText(context, "File dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempFileUri = photoUri.value
            Toast.makeText(context, "Foto berhasil diambil", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Gagal mengambil foto", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            photoUri.value = uri
            takePictureLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C))
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("TRANSAKSI", fontSize = 24.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333D2E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pilih Project", color = Color.White)
                DropdownMenuBox(selected = selectedProject, options = projectOptions)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Deskripsi Data", color = Color.White)
                OutlinedTextField(
                    value = inputData,
                    onValueChange = { inputData = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { filePickerLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("UPLOAD DOKUMEN")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("AMBIL FOTO")
                }

                tempFileUri?.let { uri ->
                    Spacer(modifier = Modifier.height(16.dp))
                    if (uri.toString().contains("image")) {
                        val inputStream = context.contentResolver.openInputStream(uri)
                        inputStream?.let {
                            val bitmap = BitmapFactory.decodeStream(it)
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Preview Foto",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                        }
                    } else {
                        val fileName = getFileNameFromUri(context, uri)
                        Text(
                            text = "File dipilih: $fileName",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        selectedFileUri = tempFileUri
                        selectedFileUri?.let { uri ->
                            val fileName = getFileNameFromUri(context, uri)
                            val success = saveFileToInternalStorage(context, uri, fileName)
                            if (success) {
                                val savedPath = File(context.filesDir, fileName).absolutePath
                                val transaksi = TransaksiEntity(
                                    projectName = selectedProject.value,
                                    inputData = inputData,
                                    photoFileName = savedPath // Simpan path file internal storage
                                )
                                viewModel.insertTransaksi(transaksi)
                                Toast.makeText(
                                    context,
                                    "Data disimpan: ${selectedProject.value}\nFile disalin ke: $savedPath",
                                    Toast.LENGTH_SHORT
                                ).show()
                                inputData = ""
                                tempFileUri = null
                            } else {
                                Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = inputData.isNotEmpty() && tempFileUri != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("SAVE")
                }
            }
        }
    }
}

// Fungsi simpan file ke internal storage dengan handling error
fun saveFileToInternalStorage(context: Context, uri: Uri, fileName: String): Boolean {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.filesDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Membuat URI untuk menyimpan foto kamera
fun createImageUri(context: Context): Uri {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        )
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/TasAnaApp")
    }
    return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: throw RuntimeException("Gagal membuat URI foto")
}

// Ambil nama file dari URI
fun getFileNameFromUri(context: Context, uri: Uri): String {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            return it.getString(nameIndex)
        }
    }
    return uri.path?.substringAfterLast('/') ?: "unknown"
}
