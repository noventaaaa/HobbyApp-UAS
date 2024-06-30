package com.example.hobbyapp.viewmodel

import android.app.Application
import android.support.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hobbyapp.model.local.NewsDao


class NewsViewModelFactory(
    private val application: Application,
    private val newsDao: NewsDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    @NonNull
    override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(application, newsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}