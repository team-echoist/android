package com.echoist.linkedout.api

import com.echoist.linkedout.data.BasicResponse
import com.echoist.linkedout.data.StoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StoryApi {

    @GET("api/essays/stories")
    suspend fun readMyStories(
        @Header("Authorization") accessToken: String,
    ): Response<StoryResponse>

    @GET("api/essays/stories/{userId}")
    suspend fun readUserStories(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int
    ): Response<StoryResponse>

    data class StoryData(val name : String, val essayIds : List<Int>? = null)
    @POST("api/essays/stories")
    suspend fun createStory(
        @Header("Authorization") accessToken: String,
        @Body storyData: StoryData
    ): Response<BasicResponse>


    @PUT("api/essays/stories/{storyId}")
    suspend fun modifyStory(
        @Header("Authorization") accessToken: String,
        @Path("storyId") storyId: Int,
        @Body storyData: StoryData
    ): Response<BasicResponse>


    @DELETE("api/essays/stories/{storyId}")
    suspend fun deleteStory(
        @Header("Authorization") accessToken: String,
        @Path("storyId") storyId: Int
    ): Response<BasicResponse>

}