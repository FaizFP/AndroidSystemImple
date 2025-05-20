package com.example.tasananew.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntitity)

    @Query("SELECT * FROM project")
    suspend fun getAllProjects(): List<ProjectEntitity>
}

@Dao
interface CatatanDao {
    @Insert
    suspend fun insertCatatan(catatan: CatatanEntitiy)

    @Query("SELECT * FROM catatan WHERE projectName = :projectName") // Ganti projectId jadi projectName
    suspend fun getCatatanByProject(projectName: String): List<CatatanEntitiy>

    @Query("SELECT * FROM catatan")
    suspend fun getAllCatatan(): List<CatatanEntitiy>
}
