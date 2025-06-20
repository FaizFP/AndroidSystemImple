package com.example.tasananew

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tasananew.database.AppDatabase
import com.example.tasananew.database.StatusCount

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF6F765C)
            ) {
                SystemImplementationScreen()
            }
        }
    }
}

@Composable
fun SystemImplementationScreen() {
    val context = LocalContext.current
    val viewModel = remember { MainGraphicViewModel(context.applicationContext as Application) }
    val statusCounts by viewModel.statusCounts.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "System Implementation",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1F2C1F), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Aplikasi ini dirancang untuk mencatat, memantau, dan mengelola proses implementasi serta pemeliharaan perangkat lunak...",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Grafik Status",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        StatusBarChartHorizontal(statusCounts)

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
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
                        .size(100.dp)
                        .padding(end = 20.dp)
                        .clickable {
                            context.startActivity(Intent(context, ListActivity::class.java))
                        }
                )
            }
        }
    }
}

@Composable
fun StatusBarChartHorizontal(statusCounts: List<StatusCount>) {
    val colors = listOf(Color(0xFFD0BFFF), Color(0xFFB0A8B9), Color(0xFFA084DC)) // Warna ungu variasi

    if (statusCounts.isEmpty()) {
        Text("Belum ada data status.", color = Color.White)
        return
    }

    val maxCount = statusCounts.maxOf { it.count }.coerceAtLeast(1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            statusCounts.forEachIndexed { index, item ->
                val scaledHeight = ((item.count.toFloat() / maxCount) * 80).dp
                val animatedHeight by animateDpAsState(targetValue = scaledHeight, label = "bar_height")

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .height(140.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = item.count.toString(),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(animatedHeight)
                            .background(colors[index % colors.size], RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.status,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

class MainGraphicViewModel(application: Application) : AndroidViewModel(application) {
    private val catatanDao = AppDatabase.getDatabase(application).catatanDao()
    val statusCounts: LiveData<List<StatusCount>> = catatanDao.getStatusCounts().asLiveData()
}