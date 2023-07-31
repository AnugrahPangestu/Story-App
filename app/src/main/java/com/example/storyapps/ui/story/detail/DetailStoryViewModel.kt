package com.example.storyapps.ui.story.detail

import androidx.lifecycle.ViewModel
import com.example.storyapps.data.StoryRepository

class DetailStoryViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun getDetailStory(id: String) = storyRepository.getDetailStory(id)
}