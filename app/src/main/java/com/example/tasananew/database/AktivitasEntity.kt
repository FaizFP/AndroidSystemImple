package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "aktivitas",
    foreignKeys = [ForeignKey(
        entity = ProjectEntitity::class,
        parentColumns = ["name"],
        childColumns = ["projectName"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["projectName"])]
)
data class AktivitasEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectName: String, // relasi ke tabel project
    val modelType: String,
    val algorithmUsed: String,
    val hyperparameters: String
)
