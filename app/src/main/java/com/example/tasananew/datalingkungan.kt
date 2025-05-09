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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class DataLingkunganActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataLingkunganScreen()
        }
    }
}

@Composable
fun DataLingkunganScreen() {
    var os by remember { mutableStateOf("") }
    var cpu by remember { mutableStateOf("") }
    var ram by remember { mutableStateOf("") }
    var database by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6F765C)) // Sama seperti background CatatanActivity
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "WELCOME",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = "Data Lingkungan",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333D2E)) // Sama seperti background form Catatan
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField("Operating System", os) { os = it }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField("Cpu(Core)", cpu) { cpu = it }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField("RAM", ram) { ram = it }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField("DATABASE", database) { database = it }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Simpan data */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    ),
                    enabled = os.isNotEmpty() && cpu.isNotEmpty() && ram.isNotEmpty() && database.isNotEmpty()
                ) {
                    Text("SAVE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, color = Color.White)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                cursorColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDataLingkunganScreen() {
    DataLingkunganScreen()
}