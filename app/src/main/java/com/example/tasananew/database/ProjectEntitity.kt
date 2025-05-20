package com.example.tasananew.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class ProjectEntitity(
    @PrimaryKey val name: String,  // name jadi primary key
    val model: String,
    val description: String
)