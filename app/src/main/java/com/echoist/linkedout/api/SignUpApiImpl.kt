
import com.echoist.linkedout.api.SignUpApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class SignUpApiImpl : SignUpApi {

    data class RegisterDeviceRequest(val deviceId : String, val deviceToken : String)


    override suspend fun emailDuplicateConfirm(email: SignUpApi.EmailRequest): Response<Unit> {
        // Implement emailDuplicateConfirm API call
        return TODO()
    }

    override suspend fun emailVerify(userAccount: SignUpApi.UserAccount): Response<Unit> {
        // Implement emailVerify API call
        return TODO()
    }

    override suspend fun sendEmailVerificationForChange(accessToken: String, email: SignUpApi.EmailRequest): Response<Unit> {
        // Implement sendEmailVerificationForChange API call
        return TODO()
    }

    override suspend fun requestChangePw(accessToken: String, email: SignUpApi.EmailRequest): Response<Unit> {
        // Implement requestChangePw API call
        return TODO()
    }

    override suspend fun verifyChangePw(accessToken: String, token: String): Response<Unit> {
        // Implement verifyChangePw API call
        return TODO()
    }

    override suspend fun resetPw(accessToken: String, resetPwRequest: SignUpApi.ResetPwRequest): Response<Unit> {
        // Implement resetPw API call
        return TODO()
    }

    override suspend fun login(userAccount: SignUpApi.UserAccount): Response<SignUpApi.LoginResponse> {
        // Implement login API call
        return TODO()
    }

    @POST("api/support/devices/register")
    suspend fun requestRegisterDevice(
        @Header("Authorization") accessToken: String,
        @Body registerDeviceRequest: RegisterDeviceRequest
    ): Response<Unit> {
        TODO("Not yet implemented")
    }


}
