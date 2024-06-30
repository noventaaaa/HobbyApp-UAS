package com.example.hobbyapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsEntity)

    @Update
    suspend fun update(user: NewsEntity)

    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsEntity>

    @Query("SELECT * FROM news where id = :id")
    suspend fun getNewsById(id: Int): NewsEntity?

    @Delete
    suspend fun delete(user: NewsEntity)


}
