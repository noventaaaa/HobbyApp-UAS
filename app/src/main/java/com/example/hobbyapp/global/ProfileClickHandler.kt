package com.example.hobbyapp.global

import android.view.View
import com.example.hobbyapp.model.User

interface ProfileClickHandler {
    fun onProfileClick(view: View, user: User)
}