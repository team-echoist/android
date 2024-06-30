package com.echoist.linkedout.api

import SignUpApiImpl
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SignUpApi {

    @POST("api/auth/check/email")
    suspend fun emailDuplicateConfirm(
        @Body email: EmailRequest

    ): Response<Unit>

    data class EmailRequest(val email : String,val id : Int? = null)
    @POST("api/auth/verify")
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

    //이메일 재설정
    @POST("api/auth/verify/email")
    suspend fun sendEmailVerificationForChange(
        @Header("Authorization") accessToken: String,
        @Body email : EmailRequest
    ): Response<Unit>

    //비밀번호 재설정 요청
    @POST("api/auth/password/reset-req")
    suspend fun requestChangePw(
        @Header("Authorization") accessToken: String,
        @Body email : EmailRequest
    ): Response<Unit>

    @POST("api/auth/password/reset-verify")
    suspend fun verifyChangePw(
        @Header("Authorization") accessToken: String,
        @Query("token") token : String
    ): Response<Unit>

    @POST("api/auth/password/reset")
    suspend fun resetPw(
        @Header("Authorization") accessToken: String,
        @Body resetPwRequest : ResetPwRequest
    ): Response<Unit>

    data class ResetPwRequest(val pw : String,val token : String)


    @POST("api/auth/login")
    suspend fun login(
        @Body userAccount : UserAccount
    ): Response<LoginResponse>

    data class LoginResponse(
        val success: Boolean,
        val timestamp: String,
        val path: String
    )
    @POST("api/support/devices/register")
    suspend fun requestRegisterDevice(
        @Header("Authorization") accessToken: String,
        @Body registerDeviceRequest: SignUpApiImpl.RegisterDeviceRequest
    ): Response<Unit>


    @GET("api/support/settings/{deviceId}")
    suspend fun getUserNotification(
        @Header("Authorization") accessToken: String,
        @Path("deviceId") deviceId: String,
    ): Response<NotificationResponse>
    @POST("api/support/settings/{deviceId}")
    suspend fun updateUserNotification(
        @Header("Authorization") accessToken: String,
        @Path("deviceId") deviceId: String,
        @Body requestSettings: NotificationSettings
    ): Response<Unit>

    data class NotificationSettings(
        val viewed: Boolean,
        val report : Boolean,
        val timeAllowed : Boolean,
        val remindTime : String? = null
    )

    @JsonClass(generateAdapter = true)
    data class NotificationResponse(
        val data: NotificationSettings,
        val path: String?,
        val success: Boolean,
        val timestamp: String?,
        val statusCode : Int?
    )


}
