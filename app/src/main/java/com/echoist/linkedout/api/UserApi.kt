package com.echoist.linkedout.api

import com.echoist.linkedout.data.BadgeDetailResponse
import com.echoist.linkedout.data.BadgeSimpleResponse
import com.echoist.linkedout.data.BasicResponse
import com.echoist.linkedout.data.UserEssayStatsResponse
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.data.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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
    ): Response<BasicResponse>

    @PUT("api/users")
    suspend fun readMyInfo(
        @Header("Authorization") accessToken: String
    ): UserResponse

    @GET("api/users/{userId}")
    suspend fun readMyInfoDetail(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId : Int
    ): UserEssayStatsResponse

    @PUT("api/users")
    suspend fun userUpdate(
        @Header("Authorization") accessToken: String,
        @Body userInfo: UserInfo
    ): Response<UserResponse>




}