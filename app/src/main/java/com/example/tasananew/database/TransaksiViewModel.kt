package com.example.tasananew.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel untuk mengelola data transaksi
// Menggunakan AndroidViewModel agar bisa mengakses context (Application)
class TransaksiViewModel(application: Application) : AndroidViewModel(application) {

    // Mengakses DAO untuk transaksi
    private val transaksiDao = AppDatabase.getDatabase(application).transaksiDao()

    // (Jika digunakan) Mengakses DAO untuk project, misalnya untuk validasi project
    private val projectDao = AppDatabase.getDatabase(application).projectDao()

    // Menyediakan semua transaksi sebagai LiveData, otomatis update saat ada perubahan di database
    val transaksiList: LiveData<List<TransaksiEntity>> = transaksiDao.getAllTransaksi().asLiveData()

    // Menambahkan transaksi baru ke database
    fun insertTransaksi(transaksi: TransaksiEntity) {
        viewModelScope.launch {
            transaksiDao.insertTransaksi(transaksi)
            // Tidak perlu manual update UI karena LiveData sudah observe perubahan
        }
    }

    // Menghapus satu transaksi
    fun deleteTransaksi(transaksi: TransaksiEntity) {
        viewModelScope.launch {
            transaksiDao.deleteTransaksi(transaksi)
            // LiveData akan otomatis mendeteksi dan memperbarui tampilan UI
        }
    }

    // Memperbarui data transaksi yang ada
    fun updateTransaksi(transaksi: TransaksiEntity) {
        viewModelScope.launch {
            transaksiDao.update(transaksi)
        }
    }

    // Menghapus semua transaksi berdasarkan nama proyek tertentu
    fun deleteTransaksiByProject(projectName: String) {
        viewModelScope.launch {
            transaksiDao.deleteTransaksiByProject(projectName)
        }
    }

    // Mengambil daftar transaksi berdasarkan nama proyek
    fun getTransaksiByProjectName(projectName: String): LiveData<List<TransaksiEntity>> {
        return transaksiDao.getTransaksiByProjectName(projectName).asLiveData()
    }
}
    
