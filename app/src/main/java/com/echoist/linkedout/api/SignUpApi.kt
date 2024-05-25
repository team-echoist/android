package com.echoist.linkedout.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject

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

class SignUpApiClient @Inject constructor()  {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val api: SignUpApi = Retrofit.Builder()
        .baseUrl("https://www.linkedoutapp.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SignUpApi::class.java)
}