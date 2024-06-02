package com.echoist.linkedout.api

import com.echoist.linkedout.data.BadgeDetailResponse
import com.echoist.linkedout.data.BadgeSimpleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("api/users/badges")
    suspend fun readBadgeList(
        @Header("Authorization") accessToken: String
    ): Response<BadgeSimpleResponse>

    @GET("api/users/badges/detail")
    suspend fun readBadgeWithTagsList(
        @Header("Authorization") accessToken: String
    ): Response<BadgeDetailResponse>
}