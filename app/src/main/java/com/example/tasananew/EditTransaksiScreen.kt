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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.tasananew.database.TransaksiEntity
import com.example.tasananew.database.TransaksiViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditTransaksiScreen(
    transaksi: TransaksiEntity,
    viewModel: TransaksiViewModel,
    onSaveComplete: () -> Unit
) {
    val context = LocalContext.current
    var inputData by remember { mutableStateOf(transaksi.inputData) }
    var tempFileUri by remember { mutableStateOf<Uri?>(null) }
    var photoFilePath by remember { mutableStateOf(transaksi.photoFileName) }
    val photoUriState = remember { mutableStateOf<Uri?>(null) }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            tempFileUri = it
            Toast.makeText(context, "File baru dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) photoUriState.value?.let { tempFileUri = it }
    }

    val requestCamera = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val uri = createUniqueImageUri(context)
            photoUriState.value = uri
            takePicture.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Edit Transaksi: ${transaksi.projectName}", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = inputData,
            onValueChange = { inputData = it },
            label = { Text("Deskripsi Data") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { filePicker.launch("*/*") }, Modifier.fillMaxWidth()) {
            Text("Pilih File Baru")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { requestCamera.launch(Manifest.permission.CAMERA) }, Modifier.fillMaxWidth()) {
            Text("Ambil Foto Baru")
        }

        Spacer(Modifier.height(16.dp))

        // Preview gambar/file
        val displayUri = tempFileUri ?: Uri.fromFile(File(photoFilePath))
        displayUri?.let { uri ->
            val bmp = context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
            bmp?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (tempFileUri != null) {
                    val fileName = getDisplayFileNameFromUri(context, tempFileUri!!)
                    if (saveUriFileToInternalStorage(context, tempFileUri!!, fileName)) {
                        photoFilePath = File(context.filesDir, fileName).absolutePath
                    } else {
                        Toast.makeText(context, "Gagal simpan file", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                }
                viewModel.updateTransaksi(transaksi.copy(inputData = inputData, photoFileName = photoFilePath))
                Toast.makeText(context, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                onSaveComplete()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

// --- Helper Functions ---

fun createUniqueImageUri(context: Context): Uri {
    val cv = ContentValues().apply {
        put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        )
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/TasAnaApp")
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        ?: throw RuntimeException("Gagal buat URI foto")
}

fun getDisplayFileNameFromUri(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, null, null, null, null)?.use { c ->
        val idx = c.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
        if (c.moveToFirst() && idx != -1) return c.getString(idx)
    }
    return uri.lastPathSegment ?: "file"
}

fun saveUriFileToInternalStorage(context: Context, uri: Uri, fileName: String): Boolean {
    return try {
        context.contentResolver.openInputStream(uri).use { inp ->
            File(context.filesDir, fileName).outputStream().use { outp ->
                inp?.copyTo(outp)
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
