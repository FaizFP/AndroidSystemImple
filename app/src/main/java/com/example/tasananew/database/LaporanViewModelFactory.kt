package com.example.tasananew.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LaporanViewModelFactory(
    private val projectDao: ProjectDao,
    private val lingkunganDao: LingkunganDao,
    private val catatanDao: CatatanDao,
    private val transaksiDao: TransaksiDao,
    private val aktivitasDao: AktivitasDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaporanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LaporanViewModel(
                projectDao,
                lingkunganDao,
                catatanDao,
                transaksiDao,
                aktivitasDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
