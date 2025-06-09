package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CatatanViewModel(application: Application) : AndroidViewModel(application) {
    private val _catatanList = MutableLiveData<List<CatatanEntitiy>>()
    val projects: LiveData<List<CatatanEntitiy>> = _catatanList

    private val dao = AppDatabase.getDatabase(application).catatanDao()

    init {
        loadAllCatatan()
    }

    private fun loadAllCatatan() {
        viewModelScope.launch {
            _catatanList.value = dao.getAllCatatan()
        }
    }

    fun insertCatatan(catatan: CatatanEntitiy) {
        viewModelScope.launch {
            dao.insertCatatan(catatan)
            loadAllCatatan()
        }
    }

    fun updateCatatan(catatan: CatatanEntitiy) {
        viewModelScope.launch {
            dao.updateCatatan(catatan)
            loadAllCatatan()
        }
    }

    fun deleteCatatan(catatan: CatatanEntitiy) {
        viewModelScope.launch {
            dao.deleteCatatan(catatan)
            loadAllCatatan()
        }
    }

    fun getCatatanByProject(projectName: String) {
        viewModelScope.launch {
            _catatanList.value = dao.getCatatanByProject(projectName)
        }
    }
}
