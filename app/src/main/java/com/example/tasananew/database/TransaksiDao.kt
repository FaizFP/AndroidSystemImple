package com.example.tasananew.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {

    // Menambahkan transaksi baru ke database.
    // Jika ada data dengan primary key yang sama, maka akan ditimpa (REPLACE).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: TransaksiEntity)

    // Menghapus satu data transaksi dari database.
    @Delete
    suspend fun deleteTransaksi(transaksi: TransaksiEntity)

    // Mengupdate data transaksi yang sudah ada di database.
    @Update
    suspend fun update(transaksi: TransaksiEntity)

    // Mengambil seluruh data transaksi dari tabel 'transaksi'.
    // Hasilnya berupa Flow untuk mendukung pemantauan data secara realtime.
    @Query("SELECT * FROM transaksi")
    fun getAllTransaksi(): Flow<List<TransaksiEntity>>

    // Mengambil data transaksi berdasarkan nama project tertentu.
    // Hasilnya juga berupa Flow agar bisa digunakan untuk pemantauan realtime.
    @Query("SELECT * FROM transaksi WHERE projectName = :projectName")
    fun getTransaksiByProjectName(projectName: String): Flow<List<TransaksiEntity>>

    @Query("SELECT * FROM transaksi WHERE projectName = :projectName LIMIT 1")
    suspend fun getTransaksiByProject(projectName: String): TransaksiEntity?


    // Menghapus semua data transaksi yang terkait dengan nama project tertentu.
    @Query("DELETE FROM transaksi WHERE projectName = :projectName")
    suspend fun deleteTransaksiByProject(projectName: String)
}
