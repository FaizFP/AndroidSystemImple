package com.example.tasananew

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasananew.database.TransaksiEntity
import com.example.tasananew.database.TransaksiViewModel
import com.example.tasananew.database.TransaksiViewModelFactory

class EditTransaksiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("id", -1)
        if (id == -1) finish()

        val projectName = intent.getStringExtra("projectName") ?: ""
        val inputData = intent.getStringExtra("inputData") ?: ""
        val photoFileName = intent.getStringExtra("photoFileName") ?: ""

        val transaksi = TransaksiEntity(id, projectName, inputData, photoFileName)

        setContent {
            val viewModel: TransaksiViewModel = viewModel(factory = TransaksiViewModelFactory(application as Application))
            EditTransaksiScreen(transaksi, viewModel) {
                finish()
            }
        }
    }
}
