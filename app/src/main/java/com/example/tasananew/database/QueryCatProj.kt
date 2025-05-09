package com.example.tasananew.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntitity)

    @Query("SELECT * FROM project") // ✅ sesuaikan tableName baru
    suspend fun getAllProjects(): List<ProjectEntitity>
}

@Dao
interface CatatanDao {
    @Insert
    suspend fun insertCatatan(catatan: CatatanEntitiy)

    @Query("SELECT * FROM catatan WHERE projectId = :projectId") // ✅ tableName baru
    suspend fun getCatatanByProject(projectId: String): List<CatatanEntitiy>
}