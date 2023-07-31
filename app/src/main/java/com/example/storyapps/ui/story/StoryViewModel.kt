package com.example.storyapps.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.helper.UserPreferences
import kotlinx.coroutines.launch

class StoryViewModel(private val pref: UserPreferences) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            pref.setToken("")
        }
    }
}