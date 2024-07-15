package com.echoist.linkedout.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SocialSignUpApi {
    @POST("api/auth/google/mobile")
    suspend fun requestGoogleLogin(
        @Body userAccount : UserAccount
    ): Response<Unit>

    @POST("api/auth/kakao/mobile")
    suspend fun requestKakaoLogin(
        @Body userAccount : UserAccount
    ): Response<Unit>

    @POST("api/auth/naver/mobile")
    suspend fun requestNaverLogin(
        @Body userAccount : UserAccount
    ): Response<Unit>

    data class UserAccount(
        val token : String,
        val platformId : String
    )
}