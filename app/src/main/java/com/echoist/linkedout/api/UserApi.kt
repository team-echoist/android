package com.echoist.linkedout.api

import com.echoist.linkedout.data.BadgeDetailResponse
import com.echoist.linkedout.data.BadgeLevelUpResponse
import com.echoist.linkedout.data.BadgeSimpleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("api/users/{userId}")
    suspend fun readUserInfo(
        @Header("Authorization") accessToken: String,
        @Path("essayId") essayId: Int = 0
    ): Response<BadgeSimpleResponse>
    @GET("api/users/badges")
    suspend fun readBadgeList(
        @Header("Authorization") accessToken: String
    ): Response<BadgeSimpleResponse>

    @GET("api/users/badges/detail")
    suspend fun readBadgeWithTagsList(
        @Header("Authorization") accessToken: String
    ): Response<BadgeDetailResponse>

    @POST("api/users/badges/level")
    suspend fun requestBadgeLevelUp(
        @Header("Authorization") accessToken: String,
        @Query("badgeId") badgeId: Int,
    ): Response<BadgeLevelUpResponse>

}