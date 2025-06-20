package com.example.tasananew

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasananew.database.AktivitasEntity
import com.example.tasananew.database.AktivitasViewModel
import com.example.tasananew.database.AktivitasViewModelFactory

class EditAktivitasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("id", -1)
        if (id == -1) finish()

        val projectName = intent.getStringExtra("projectName") ?: ""
        val modelType = intent.getStringExtra("modelType") ?: ""
        val algorithmUsed = intent.getStringExtra("algorithmUsed") ?: ""
        val hyperparameters = intent.getStringExtra("hyperparameters") ?: ""

        val aktivitas = AktivitasEntity(
            id = id,
            projectName = projectName,
            modelType = modelType,
            algorithmUsed = algorithmUsed,
            hyperparameters = hyperparameters
        )

        setContent {
            val viewModel: AktivitasViewModel = viewModel(factory = AktivitasViewModelFactory(application as Application))
            EditAktivitasScreen(aktivitas, viewModel) {
                finish()
            }
        }
    }
}
