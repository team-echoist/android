package com.echoist.linkedout.api

import com.echoist.linkedout.data.RegisterCode
import com.echoist.linkedout.data.TokensResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpApi {
    @POST("api/auth/check/email")
    suspend fun emailDuplicateConfirm(
        @Body email: EmailRequest
    ): Response<Unit>

    data class EmailRequest(val email: String, val id: Int? = null)

    @POST("api/auth/sign")
    suspend fun emailVerify(
        @Body userAccount: UserAccount
    ): Response<Unit>

    data class UserAccount(
        val email: String,
        val password: String,
        val birtDate: String = "",
        val gender: String = "",
        val oauthInfo: String = ""
    )

    //이메일 재설정
    @POST("api/auth/email/verify")
    suspend fun sendEmailVerificationForChange(
        @Body email: EmailRequest
    ): Response<Unit>

    data class ChangeEmail(val code: String)
    @POST("api/auth/email/change")
    suspend fun postAuthChangeEmail(
        @Body code: ChangeEmail
    ): Response<Unit>

    //비밀번호 재설정 요청
    @POST("api/auth/password/reset")
    suspend fun requestChangePw(
        @Body email: EmailRequest
    ): Response<Unit>

    @POST("api/auth/password/reset-verify")
    suspend fun verifyChangePw(
        @Query("token") token: String
    ): Response<Unit>

    @POST("api/auth/password/reset")
    suspend fun resetPw(
        @Body resetPwRequest: ResetPwRequest
    ): Response<Unit>

    data class ResetPwRequest(val pw: String, val token: String)

    @POST("api/auth/login")
    suspend fun login(
        @Body userAccount: UserAccount
    ): Response<TokensResponse>

    //6자리 코드 인증 후 회원가입 요청
    @POST("api/auth/register")
    suspend fun requestRegister(
        @Body code: RegisterCode
    ): Response<TokensResponse>
}
