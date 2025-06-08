package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).projectDao()

    private val _projects = MutableLiveData<List<ProjectEntitity>>()
    val projects: LiveData<List<ProjectEntitity>> = _projects

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = dao.getAllProjects()
            _projects.postValue(data)
        }
    }

    fun updateProject(project: ProjectEntitity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(project)
            loadProjects()
        }
    }

    fun deleteProject(project: ProjectEntitity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(project)
            loadProjects()
        }
    }
}
