package com.example.hobbyapp.model

import com.google.gson.annotations.SerializedName

data class News (
    var id:Int,
    var title:String,
    @SerializedName("image")
    var imgUrl:String,
    var preview:String?,
    var content:String?,
    var author:String,
    var category:List<String>?,
)

data class User (
    var id:Int,
    var username:String,
    var password:String?,
    var firstname:String,
    var lastname:String,
)