package com.echoist.linkedout.api

import com.echoist.linkedout.data.BasicResponse
import com.echoist.linkedout.data.RelatedEssayResponse
import com.echoist.linkedout.data.StoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryApi {

    @GET("api/stories")
    suspend fun readMyStories(
        
    ): Response<StoryResponse>

    @GET("api/essays/stories/{userId}")
    suspend fun readUserStories(
        @Path("userId") userId: Int
    ): Response<StoryResponse>

    data class StoryData(val name : String, val essayIds : List<Int>? = null)
    @POST("api/stories")
    suspend fun createStory(
        @Body storyData: StoryData
    ): Response<BasicResponse>


    @PUT("api/stories/{storyId}")
    suspend fun modifyStory(
        @Path("storyId") storyId: Int,
        @Body storyData: StoryData
    ): Response<BasicResponse>


    @DELETE("api/stories/{storyId}")
    suspend fun deleteStory(
        @Path("storyId") storyId: Int
    ): Response<BasicResponse>

    @GET("api/stories/related")
    suspend fun readStoryEssayList(
        @Query("storyId") storyId: Int? = null,
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 20,

    ): Response<RelatedEssayResponse>

    @PUT("api/stories/{storyId}/essays/{essayId}/")
    suspend fun modifyEssayInStory(
        @Path("essayId") essayId: Int,
        @Path("storyId") storyId: Int
    ): Response<BasicResponse>

    @DELETE("api/stories/essays/{essayId}")
    suspend fun deleteEssayInStory(
        @Path("essayId") essayId: Int
    ): Response<BasicResponse>

}