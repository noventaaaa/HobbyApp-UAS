package com.example.hobbyapp.global

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hobbyapp.model.local.NewsDao
import com.example.hobbyapp.model.local.NewsEntity
import com.example.hobbyapp.model.local.UserDao
import com.example.hobbyapp.model.local.UserEntity

@Database(entities = [NewsEntity::class, UserEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun userDao(): UserDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE news ADD COLUMN category TEXT")
            }
        }
    }
}