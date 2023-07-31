package com.example.storyapps.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapps.service.response.Story

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("SELECT * FROM story WHERE id = :id")
    fun getDetailStory(id: String): LiveData<Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Story>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}