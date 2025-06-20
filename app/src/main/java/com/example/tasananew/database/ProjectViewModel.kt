package com.example.tasananew.database

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val projectDao = AppDatabase.getDatabase(application).projectDao()
    private val aktivitasDao = AppDatabase.getDatabase(application).aktivitasDao()

    private val _projects = MutableLiveData<List<ProjectEntitity>>()
    val projects: LiveData<List<ProjectEntitity>> = _projects

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = projectDao.getAllProjects()
            _projects.postValue(data)
        }
    }

    fun updateProject(project: ProjectEntitity) {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao.update(project)
            loadProjects()
        }
    }

    fun deleteProject(project: ProjectEntitity) {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao.delete(project)
            loadProjects()
        }
    }

    /**
     * Mengambil data dari API (simulasi) dan menyimpannya sebagai AktivitasEntity.
     * Kode ini sudah diperbaiki untuk mengatasi error foreign key.
     */
    fun fetchProjectAndActivityFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Contoh hardcoded JSON, kamu bisa ganti dengan request API asli jika sudah tersedia
                val jsonResponse = """
                [
                    {
                        "project_detail": {
                            "name": "web manajemen proyek"
                        },
                        "model_name": "RandomForestClassifier_v1.0",
                        "model_type": "classification",
                        "algorithm_used": "Random Forest",
                        "refining_strategy": "Hyperparameter tuning menggunakan GridSearchCV pada n_estimators dan max_depth."
                    }
                ]
            """.trimIndent()

                val jsonArray = JSONArray(jsonResponse)
                if (jsonArray.length() > 0) {
                    val projectData = jsonArray.getJSONObject(0)
                    val projectDetail = projectData.getJSONObject("project_detail")
                    val name = projectDetail.getString("name")
                    val model = projectData.getString("model_name")
                    val description = "Membuat aplikasi Manajemen Proyek berbasis django untuk melengkapi tugas semester 4"

                    // 1. Simpan ke ProjectEntitity
                    val project = ProjectEntitity(
                        name = name,
                        model = model,
                        description = description // Deskripsi baru ditambahkan di sini
                    )
                    projectDao.insertProject(project)

                    // 2. Simpan ke AktivitasEntity
                    val activity = AktivitasEntity(
                        projectName = name,
                        modelType = projectData.getString("model_type"),
                        algorithmUsed = projectData.getString("algorithm_used"),
                        hyperparameters = projectData.getString("refining_strategy")
                    )
                    aktivitasDao.insertAktivitas(activity)

                    // 3. Tampilkan toast sukses di UI
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplication(),
                            "Data proyek & aktivitas berhasil diambil dari API!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // 4. Refresh daftar proyek
                    loadProjects()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        "Gagal mengambil data dari API: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
