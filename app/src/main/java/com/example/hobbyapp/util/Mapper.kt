package com.example.hobbyapp.util

import com.example.hobbyapp.model.News
import com.example.hobbyapp.model.User
import com.example.hobbyapp.model.local.NewsEntity
import com.example.hobbyapp.model.local.UserEntity

object Mapper {
    fun NewsEntity.mapper(): News {
        return News(
            id = this.id,
            title = this.title,
            imgUrl = this.imgUrl,
            preview = this.preview,
            content = this.content,
            author = this.author,
            category = this.category
        )
    }

    fun News.mapperRequest(): NewsEntity {
        return NewsEntity(
            id = this.id,
            title = this.title,
            imgUrl = this.imgUrl,
            preview = this.preview,
            content = this.content,
            author = this.author,
            category = this.category
//            createdAt = this.createdAt.toString()
        )
    }

    fun User.mapperRequest(): UserEntity {
        return UserEntity(
            id = this.id,
            username = this.username,
            password = this.password,
            firstname = this.firstname,
            lastname = this.lastname,
        )
    }

    fun UserEntity?.mapperUser(): User {
        return User(
            id = this?.id ?: 0,
            username = this?.username ?: "",
            password = this?.password ?: "",
            firstname = this?.firstname ?: "",
            lastname = this?.lastname ?: "",
        )
    }
    fun List<NewsEntity>.listMapper(): List<News> {
        return this.map { it.mapper() }
    }

}