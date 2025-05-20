package com.example.tasananew.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CatatanEntitiy::class, ProjectEntitity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catatanDao(): CatatanDao
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasana_database"
                )
                    .fallbackToDestructiveMigration()  // Tambahkan ini agar migrasi otomatis dengan reset database
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

