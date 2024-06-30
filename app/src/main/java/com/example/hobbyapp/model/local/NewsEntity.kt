package com.example.hobbyapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hobbyapp.global.Converters


@Entity(tableName = "news")
@TypeConverters(Converters::class)
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var title:String,
    var imgUrl:String,
    var preview:String?,
    var content:String?,
    var author:String,
    var createdAt: String? = "",
    var category: List<String>?
)
