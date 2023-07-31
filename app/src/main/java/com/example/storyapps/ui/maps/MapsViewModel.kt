package com.example.storyapps.ui.maps

import androidx.lifecycle.ViewModel
import com.example.storyapps.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStoriesWithLocation() = storyRepository.getAllStoriesWithLocation()
}