package com.example.tasananew.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LingkunganDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: LingkunganEntity)

    @Query("SELECT * FROM lingkungan WHERE projectName = :projectName")
    fun getByProjectName(projectName: String): LiveData<List<LingkunganEntity>>

    @Query("SELECT * FROM lingkungan")
    fun getAll(): LiveData<List<LingkunganEntity>>
}
