package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "catatan",
    foreignKeys = [ForeignKey(
        entity = ProjectEntitity::class,
        parentColumns = ["name"],  // mengarah ke primary key di ProjectEntitity
        childColumns = ["projectName"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["projectName"])]
)
data class CatatanEntitiy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectName: String,  // foreign key mengarah ke ProjectEntitity.name
    val suggest: String,
    val category: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val namaPemangkuKepentingan: String,
    val namaPeran: String
)