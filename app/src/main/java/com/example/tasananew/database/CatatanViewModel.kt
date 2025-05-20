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

    init {
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(application).catatanDao()
            _catatanList.value = dao.getAllCatatan()
        }
    }
}
