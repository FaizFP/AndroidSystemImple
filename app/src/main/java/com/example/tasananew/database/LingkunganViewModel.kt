package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class LingkunganViewModel(application: Application) : AndroidViewModel(application) {
    val lingkunganList: LiveData<List<LingkunganEntity>> =
        AppDatabase.getDatabase(application).lingkunganDao().getAll()

    fun updateLingkungan(data: LingkunganEntity) {
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(getApplication()).lingkunganDao()
            dao.update(data)
        }
    }

    fun deleteLingkungan(data: LingkunganEntity) {
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(getApplication()).lingkunganDao()
            dao.delete(data)
        }
    }

}
