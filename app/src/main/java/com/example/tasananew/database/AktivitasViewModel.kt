package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AktivitasViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: AktivitasDao =
        AppDatabase.getDatabase(application).aktivitasDao()

    private val _aktivitasList = MutableLiveData<List<AktivitasEntity>>()
    val aktivitasList: LiveData<List<AktivitasEntity>> get() = _aktivitasList

    init {
        loadAktivitas()
    }

    private fun loadAktivitas() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dao.getAllAktivitas()
            _aktivitasList.postValue(result)
        }
    }

    fun insertAktivitas(aktivitas: AktivitasEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertAktivitas(aktivitas)
            loadAktivitas() // refresh
        }
    }

    fun updateAktivitas(aktivitas: AktivitasEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateAktivitas(aktivitas)
            loadAktivitas()
        }
    }

    fun deleteAktivitas(aktivitas: AktivitasEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAktivitas(aktivitas)
            loadAktivitas()
        }
    }

    suspend fun getAktivitasByProject(projectName: String): List<AktivitasEntity> {
        return withContext(Dispatchers.IO) {
            dao.getAktivitasByProject(projectName)
        }
    }
}
