package com.example.tasananew.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete


@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntitity)

    @Query("SELECT * FROM project")
    suspend fun getAllProjects(): List<ProjectEntitity>

    @Update
    fun update(project: ProjectEntitity)

    @Delete
    fun delete(project: ProjectEntitity)
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
