package com.example.hobbyapp.global

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hobby"
                ).addMigrations(
                    AppDatabase.MIGRATION_1_2,
                ).build()
            }
        }
        return instance!!
    }
}