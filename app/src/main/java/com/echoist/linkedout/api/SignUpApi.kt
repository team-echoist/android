package com.echoist.linkedout.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpApi {

    @GET("auth/check")
    suspend fun emailDuplicateConfirm(
        @Query("email") userEmail: String
    ): Response<Unit>

    @POST("auth/verify")
    suspend fun emailVerify(
        @Body userAccount : UserAccount
    ): Response<Unit>

    data class UserAccount(
        val email: String,
        val password: String,
        val birtDate: String = "",
        val gender : String = "",
        val oauthInfo : String = ""
    )

    @POST("auth/login")
    suspend fun login(
        @Body userAccount : UserAccount
    ): Response<LoginResponse>

    data class LoginResponse(
        val success: Boolean,
        val timestamp: String,
        val path: String
    )
}
