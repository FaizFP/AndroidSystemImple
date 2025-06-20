package com.example.tasananew.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class LaporanViewModel(
    private val projectDao: ProjectDao,
    private val lingkunganDao: LingkunganDao,
    private val catatanDao: CatatanDao,
    private val transaksiDao: TransaksiDao,
    private val aktivitasDao: AktivitasDao
) : ViewModel() {

    // Fungsi untuk generate PDF berdasarkan nama project
    fun generatePdfFromNamaProject(context: Context, namaProject: String) {
        viewModelScope.launch {
            try {
                val project = projectDao.getProjectByName(namaProject)
                    ?: throw Exception("Project tidak ditemukan")

                val lingkungan = lingkunganDao.getLingkunganByProject(namaProject)
                    ?: throw Exception("Lingkungan tidak ditemukan")

                val catatan = catatanDao.getCatatanByProject(namaProject).firstOrNull()
                    ?: throw Exception("Catatan tidak ditemukan")

                val transaksi = transaksiDao.getTransaksiByProject(namaProject)
                    ?: throw Exception("Transaksi tidak ditemukan")

                val aktivitas = aktivitasDao.getAktivitasByProject(namaProject).firstOrNull()
                    ?: throw Exception("Aktivitas tidak ditemukan")

                generatePdfLaporan(
                    context = context,
                    namaProject = project.name,
                    model = project.model,
                    deskripsi = project.description,
                    os = lingkungan.os,
                    cpu = lingkungan.cpu,
                    ram = lingkungan.ram,
                    database = lingkungan.database,
                    rencana = catatan.suggest,
                    kategori = catatan.category,
                    status = catatan.status,
                    mulai = catatan.startDate,
                    selesai = catatan.endDate,
                    stakeholder = "${catatan.namaPemangkuKepentingan} - ${catatan.namaPeran}",
                    deskripsiTransaksi = transaksi.inputData,
                    namaFile = transaksi.photoFileName,
                    modelType = aktivitas.modelType,
                    algorithm = aktivitas.algorithmUsed,
                    hyperparam = aktivitas.hyperparameters
                )

            } catch (e: Exception) {
                Log.e("LaporanViewModel", "Gagal ambil data: ${e.message}")
            }
        }
    }

    // Fungsi untuk ambil semua nama project agar bisa ditampilkan di dropdown
    suspend fun getAllProjectNames(): List<String> {
        return withContext(Dispatchers.IO) {
            projectDao.getAllProjectNames()
        }
    }
}
