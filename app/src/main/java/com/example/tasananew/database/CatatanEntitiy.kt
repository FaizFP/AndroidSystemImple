package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "catatan", // ✅ lowercase, lebih aman
    foreignKeys = [ForeignKey(
        entity = ProjectEntitity::class,
        parentColumns = ["id"],
        childColumns = ["projectId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["projectId"])]
)
data class CatatanEntitiy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: String,
    val suggest: String,
    val category: String,
    val status: String
)
