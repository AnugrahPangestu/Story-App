package com.example.storyapps.ui.story.addstory

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.data.StoryRepository
import com.example.storyapps.service.AddStory
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _file = MutableLiveData<File>()
    val file: LiveData<File> = _file

    private val _location = MutableLiveData<Location>()
    val location : LiveData<Location> = _location

    fun addStory(item: AddStory) = storyRepository.addStory(item)

    fun addStoryLocation(item: AddStory) = storyRepository.addStoryWithLocation(item)

    fun setFile(file: File) {
        _file.value = file
    }

    fun setLocation(location: Location){
        _location.value = location
    }
}