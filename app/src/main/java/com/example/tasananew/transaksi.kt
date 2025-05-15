package com.example.tasananew

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TransaksiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransaksiScreen()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransaksiScreen() {
    val context = LocalContext.current

    // Data input dari user
    var inputData by remember { mutableStateOf("") }

    // File yang diambil dari penyimpanan
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    // Gambar yang diambil dari kamera
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher untuk memilih file
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        fileUri = uri
        if (uri != null) {
            Toast.makeText(context, "File dipilih!", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher untuk mengambil foto
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        capturedImage = bitmap
        if (bitmap != null) {
            Toast.makeText(context, "Foto berhasil diambil!", Toast.LENGTH_SHORT).show()
        }
    }

    // Tampilan layar
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C))
            .padding(16.dp)
    ) {
        // Judul
        Text(
            text = "TRANSAKSI",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Kartu form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333D2E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Input teks
                Text(text = "Input Data", color = Color.White)
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

                // Tombol untuk memilih file
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

                // Tombol untuk ambil foto dari kamera
                Button(
                    onClick = { cameraLauncher.launch() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("AMBIL FOTO")
                }

                // Menampilkan gambar jika tersedia
                if (capturedImage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        bitmap = capturedImage!!.asImageBitmap(),
                        contentDescription = "Gambar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol simpan
                Button(
                    onClick = {
                        Toast.makeText(context, "Data disimpan: $inputData", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = inputData.isNotEmpty() && (fileUri != null || capturedImage != null),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("SIMPAN")
                }
            }
        }
    }
}
