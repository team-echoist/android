package com.echoist.linkedout.data.api
import android.content.ContentValues.TAG
import android.util.Log
import com.echoist.linkedout.data.dto.RegisterCode
import com.echoist.linkedout.data.dto.TokensResponse
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
        email: SignUpApi.EmailRequest
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postAuthChangeEmail(code: SignUpApi.ChangeEmail): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun requestChangePw(
        email: SignUpApi.EmailRequest
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyChangePw(
        token: String
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPw(
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
