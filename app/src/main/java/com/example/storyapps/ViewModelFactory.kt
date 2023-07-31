package com.example.storyapps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.di.Injection
import com.example.storyapps.helper.UserPreferences
import com.example.storyapps.ui.MainViewModel
import com.example.storyapps.ui.login.LoginViewModel
import com.example.storyapps.ui.maps.MapsViewModel
import com.example.storyapps.ui.register.RegisterViewModel
import com.example.storyapps.ui.story.HomeStoryViewModel
import com.example.storyapps.ui.story.StoryViewModel
import com.example.storyapps.ui.story.addstory.AddStoryViewModel
import com.example.storyapps.ui.story.detail.DetailStoryViewModel

class ViewModelFactory(
    private val pref: UserPreferences? = null,
    private val context: Context? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (context != null) {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(Injection.provideAuthRepository(context)) as T
            } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(Injection.provideAuthRepository(context)) as T
            } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
                return DetailStoryViewModel(Injection.provideStoryRepository(context)) as T
            } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
                return AddStoryViewModel(Injection.provideStoryRepository(context)) as T
            } else if (modelClass.isAssignableFrom(HomeStoryViewModel::class.java)) {
                return HomeStoryViewModel(Injection.provideStoryRepository(context)) as T
            } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
                return MapsViewModel(Injection.provideStoryRepository(context)) as T
            }
        }
        if (pref != null) {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref) as T
            } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
                return StoryViewModel(pref) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}