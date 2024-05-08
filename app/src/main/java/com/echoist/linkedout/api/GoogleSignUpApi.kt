package com.echoist.linkedout.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleSignUpApi {
    @POST("auth/google/android")
    suspend fun googleLogin(
        @Body userAccount : UserGoogleAccount
    ): Response<Unit>

    data class UserGoogleAccount(
        val token : String,
        val id : String
    )
}