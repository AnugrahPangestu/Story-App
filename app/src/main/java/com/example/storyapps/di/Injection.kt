package com.example.storyapps.di

import android.content.Context
import com.example.storyapps.data.AuthRepository
import com.example.storyapps.data.StoryRepository
import com.example.storyapps.database.StoryDatabase
import com.example.storyapps.helper.UserPreferences
import com.example.storyapps.service.ApiConfig
import com.example.storyapps.ui.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {

        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreferences.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getToken().first()
        }
        val apiService = ApiConfig.getApiService(token)
        return StoryRepository(database, apiService)

    }

    fun provideAuthRepository(context: Context): AuthRepository{
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository(apiService, pref)
    }
}