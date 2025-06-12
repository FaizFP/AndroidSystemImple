package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectName: String,
    val inputData: String = "",
    val photoFileName: String

)
