package com.example.storyapps.service

import com.example.storyapps.service.response.GetStoryResponse
import com.example.storyapps.service.response.LoginResponse
import com.example.storyapps.service.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getAllStories(@Query("location") location: Int): GetStoryResponse

    @GET("stories")
    suspend fun getStoriesWithPaging(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : GetStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part, @Part("description") description: RequestBody
    ): RegisterResponse

    @Multipart
    @POST("stories")
    suspend fun addStoryWithLocation(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): RegisterResponse

    //@GET("stories/{id}")
    //fun getDetailStory(@Path("id") id: String): Call<GetDetailStoryResponse>
}