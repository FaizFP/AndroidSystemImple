package com.example.tasananew.database
import androidx.room.Dao
import androidx.room.*

@Dao
interface AktivitasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAktivitas(aktivitas: AktivitasEntity)

    @Query("SELECT * FROM aktivitas WHERE projectName = :projectName")
    suspend fun getAktivitasByProject(projectName: String): List<AktivitasEntity>

    @Query("SELECT * FROM aktivitas")
    suspend fun getAllAktivitas(): List<AktivitasEntity>

    @Update
    suspend fun updateAktivitas(aktivitas: AktivitasEntity)

    @Delete
    suspend fun deleteAktivitas(aktivitas: AktivitasEntity)
}
