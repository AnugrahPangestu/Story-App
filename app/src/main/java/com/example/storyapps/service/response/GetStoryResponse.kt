package com.example.storyapps.service.response

import com.google.gson.annotations.SerializedName

data class GetStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<Story>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)