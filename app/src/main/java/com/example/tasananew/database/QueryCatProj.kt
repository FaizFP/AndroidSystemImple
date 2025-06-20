package com.example.tasananew.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// === Project DAO ===
@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntitity)

    @Query("SELECT name FROM project")
    suspend fun getAllProjectNames(): List<String>

    @Query("SELECT * FROM project WHERE name = :name LIMIT 1")
    suspend fun getProjectByName(name: String): ProjectEntitity

    @Query("SELECT * FROM project")
    suspend fun getAllProjects(): List<ProjectEntitity>

    @Update
    fun update(project: ProjectEntitity)

    @Delete
    fun delete(project: ProjectEntitity)
}

// === Catatan DAO ===
@Dao
interface CatatanDao {
    @Insert
    suspend fun insertCatatan(catatan: CatatanEntitiy)

    @Query("SELECT * FROM catatan WHERE projectName = :projectName")
    suspend fun getCatatanByProject(projectName: String): List<CatatanEntitiy>

    @Query("SELECT * FROM catatan")
    suspend fun getAllCatatan(): List<CatatanEntitiy>

    @Update
    suspend fun updateCatatan(catatan: CatatanEntitiy)

    @Delete
    suspend fun deleteCatatan(catatan: CatatanEntitiy)

    // Query untuk menghitung jumlah catatan per status
    @Query("SELECT status, COUNT(*) as count FROM catatan GROUP BY status")
    fun getStatusCounts(): Flow<List<StatusCount>>
}
