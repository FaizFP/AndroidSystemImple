package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lingkungan",
    foreignKeys = [ForeignKey(
        entity = ProjectEntitity::class,
        parentColumns = ["name"],  // mengacu ke PK di ProjectEntitity
        childColumns = ["projectName"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["projectName"])]
)
data class LingkunganEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectName: String,  // foreign key
    val os: String,
    val cpu: String,
    val ram: String,
    val database: String
)
