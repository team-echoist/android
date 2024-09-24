package com.echoist.linkedout.data.api

import com.echoist.linkedout.data.dto.NaverUserResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverApiService {
    @GET("v1/nid/me")
    suspend fun readUserInfo(@Header("Authorization") accessToken: String): NaverUserResponse
}