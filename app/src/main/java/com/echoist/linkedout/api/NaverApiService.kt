package com.echoist.linkedout.api

import com.echoist.linkedout.data.NaverApiUserInfo
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverApiService {
    @GET("v1/nid/me")
    suspend fun getUserInfo(@Header("Authorization") accessToken: String): NaverApiUserInfo
}