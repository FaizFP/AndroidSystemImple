package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project") // ✅ lowercase juga
data class ProjectEntitity(
    @PrimaryKey val id: String,
    val name: String,
    val model: String,
    val description: String
)
