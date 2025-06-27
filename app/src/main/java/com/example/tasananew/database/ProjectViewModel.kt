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
     * Fetch data dari API nyata dan simpan ke database lokal.
     */
    fun fetchProjectAndActivityFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://arlellll.pythonanywhere.com/api-content/training-models/")
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (!responseBody.isNullOrEmpty()) {
                        val jsonArray = JSONArray(responseBody)

                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)

                            val projectDetail = item.getJSONObject("project_detail")
                            val name = projectDetail.getString("name")
                            val description = projectDetail.getString("description")
                            val model = item.getString("model_name")
                            val modelType = item.getString("model_type")
                            val algorithmUsed = item.getString("algorithm_used")
                            val refiningStrategy = item.optString("refining_strategy", "-")

                            // Simpan ke ProjectEntity
                            val project = ProjectEntitity(
                                name = name,
                                model = model,
                                description = description
                            )
                            projectDao.insertProject(project)

                            // Simpan ke AktivitasEntity
                            val activity = AktivitasEntity(
                                projectName = name,
                                modelType = modelType,
                                algorithmUsed = algorithmUsed,
                                hyperparameters = refiningStrategy
                            )
                            aktivitasDao.insertAktivitas(activity)
                        }

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                getApplication(),
                                "Data berhasil diambil dari API dan disimpan!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        loadProjects()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplication(),
                            "Respon gagal: ${response.code}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
