package com.example.hobbyapp.viewmodel

import android.app.Application
import android.support.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hobbyapp.model.local.UserDao


class UserViewModelFactory(
    private val application: Application,
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    @NonNull
    override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}