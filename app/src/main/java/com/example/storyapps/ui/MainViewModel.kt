package com.example.storyapps.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapps.helper.UserPreferences

class MainViewModel (private val pref: UserPreferences) : ViewModel() {
    fun getUserToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}