package com.echoist.linkedout.api

import com.echoist.linkedout.data.TokensResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SocialSignUpApi {
    @POST("api/auth/google/mobile")
    suspend fun requestGoogleLogin(
        @Body userAccount : UserAccount
    ): Response<TokensResponse>

    @POST("api/auth/kakao/mobile")
    suspend fun requestKakaoLogin(
        @Body userAccount : UserAccount
    ): Response<TokensResponse>

    @POST("api/auth/naver/mobile")
    suspend fun requestNaverLogin(
        @Body userAccount : UserAccount
    ): Response<TokensResponse>

    @POST("api/auth/apple/mobile")
    suspend fun requestAppleLogin(
        @Body userAccount : UserAccount
    ): Response<TokensResponse>

    data class UserAccount(
        val token : String
    )


}