package com.echoist.linkedout.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("auth/check/email")
    suspend fun emailDuplicateConfirm(
        @Body email: EmailRequest

    ): Response<Unit>

    data class EmailRequest(val email : String,val id : Int? = null)
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
