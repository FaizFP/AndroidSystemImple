package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class LingkunganViewModel(application: Application) : AndroidViewModel(application) {
    val lingkunganList: LiveData<List<LingkunganEntity>> =
        AppDatabase.getDatabase(application).lingkunganDao().getAll()
}
