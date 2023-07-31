package com.example.storyapps

import com.example.storyapps.service.response.Story

object DataDummy {
    fun generateDummyStory(): List<Story> {
        val storyList: MutableList<Story> = arrayListOf()
        for (i in 0..10) {
            val story = Story(
                i.toString(),
                "https://picsum.photos/id/$i/400",
                "2023-05-24T04:04:22.061Z",
                "user$i",
                "descr$i"
            )
            storyList.add(story)
        }
        return storyList
    }
}