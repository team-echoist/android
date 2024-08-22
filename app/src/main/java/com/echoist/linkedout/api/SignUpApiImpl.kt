package com.echoist.linkedout.api
import android.content.ContentValues.TAG
import android.util.Log
import com.echoist.linkedout.data.RegisterCode
import com.echoist.linkedout.data.TokensResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class SignUpApiImpl : SignUpApi {

    data class RegisterDeviceRequest(val uid : String, val fcmToken : String)


    override suspend fun emailDuplicateConfirm(email: SignUpApi.EmailRequest): Response<Unit> {
        // Implement emailDuplicateConfirm API call
        return TODO()
    }

    override suspend fun emailVerify(userAccount: SignUpApi.UserAccount): Response<Unit> {
        // Implement emailVerify API call
        return TODO()
    }

    override suspend fun sendEmailVerificationForChange(
        accessToken: String,
        refreshToken: String,
        email: SignUpApi.EmailRequest
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun requestChangePw(
        accessToken: String,
        refreshToken: String,
        email: SignUpApi.EmailRequest
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyChangePw(
        accessToken: String,
        refreshToken: String,
        token: String
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPw(
        accessToken: String,
        refreshToken: String,
        resetPwRequest: SignUpApi.ResetPwRequest
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun login(userAccount: SignUpApi.UserAccount): Response<TokensResponse> {
        // Implement login API call
        return TODO()
    }

    override suspend fun requestRegister(code: RegisterCode): Response<TokensResponse> {
        return TODO()

    }

    @POST("api/support/devices/register")
    suspend fun requestRegisterDevice(
        @Header("Authorization") accessToken: String,
        @Body registerDeviceRequest: RegisterDeviceRequest
    ) {
        Log.d(TAG, "requestRegisterDevice: ")
    }


}
