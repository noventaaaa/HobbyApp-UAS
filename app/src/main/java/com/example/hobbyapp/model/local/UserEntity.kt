package com.example.hobbyapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var username:String,
    var password:String?,
    var firstname:String,
    var lastname:String,
)
