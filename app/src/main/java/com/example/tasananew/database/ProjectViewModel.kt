package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val _projects = MutableLiveData<List<ProjectEntitity>>()
    val projects: LiveData<List<ProjectEntitity>> = _projects

    init {
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(application).projectDao()
            _projects.value = dao.getAllProjects()
        }
    }
}
