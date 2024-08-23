package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.api.SignUpApiImpl
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.NotificationSettings
import com.echoist.linkedout.data.RegisterCode
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.myLog.Token
import com.echoist.linkedout.page.myLog.Token.bearerAccessToken
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpApi : SignUpApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems,
    private val supportApi: SupportApi
) : ViewModel() {

    var userEmail by mutableStateOf("")
    var userEmailError by mutableStateOf(false)
    var userPw by mutableStateOf("")

    var agreement_service by mutableStateOf(false)
    var agreement_collection by mutableStateOf(false)
    var agreement_teen by mutableStateOf(false)
    var agreement_marketing by mutableStateOf(false)
    var agreement_location by mutableStateOf(false)
    var agreement_serviceAlert by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var errorCode by mutableStateOf(200)

    fun getMyInfo() : UserInfo{
        return exampleItems.myProfile
    }
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private suspend fun readMyInfo(){
        try {
            val response = userApi.getMyInfo(bearerAccessToken,Token.refreshToken)

            exampleItems.myProfile = response.data.user
            exampleItems.myProfile.essayStats = response.data.essayStats

        }catch (_: Exception){
            Log.d(TAG, "readMyInfo: error err")
        }
    }

    //이메일 중복검사 1차
    var isSignUpApiFinished by mutableStateOf(false)
    fun getUserEmailCheck(userEmail: String,navController: NavController) {

        viewModelScope.launch {
            isLoading = true
            try {
                val email = SignUpApi.EmailRequest(userEmail)
                val response = signUpApi.emailDuplicateConfirm(email)


                if (response.isSuccessful) {
                    Log.e("authApiSuccess1", "${response.code()}")

                    emailVerify(navController)
                } else {
                    errorCode = response.code()
                    Log.e("authApiFailed1", "Failed : ${response.errorBody()}")

                    Log.e("authApiSuccess", "${response.code()}")
                    if (response.code() == 409) Log.e(TAG, "getUserEmailCheck: 이미 사용중인 이메일입니다.", )
                }

            } catch (e: Exception) {
                errorCode = 500

                // api 요청 실패
                Log.e("writeEssayApiFailed1", "Failed: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    fun requestRegister(code : String,navController: NavController){ //6자리 코드
        viewModelScope.launch {
            isLoading = true
            try {
                val registerCode = RegisterCode(code)
                val response = signUpApi.requestRegister(registerCode)
                Log.d("6자리 요청 코드", code)

                if (response.isSuccessful) {
                    Token.accessToken = response.body()!!.data.accessToken
                    Token.refreshToken = response.body()!!.data.refreshToken

                    readMyInfo()
                    navController.navigate("SignUpComplete")
                } else {
                    errorCode = response.code()

                    Log.e("회원가입 요청 실패", "Failed: ${response.code()}")
                }

            } catch (e: Exception) {
                errorCode = 500
                // api 요청 실패
                Log.e("회원가입 요청 실패", "Failed: ${e.printStackTrace()}")

            }
            finally { isLoading = false }
        }
    }


    //이메일 중복검사 2차
    private suspend fun emailVerify(navController: NavController) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로

        try {
            val userAccount = SignUpApi.UserAccount(userEmail, userPw)
            val response = signUpApi.emailVerify(userAccount)

            if (response.isSuccessful) {

                isSignUpApiFinished = true

                Log.d("tokentoken", response.headers()["authorization"].toString())
                Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken
            } else {
                errorCode = response.code()
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
            }

        } catch (e: Exception) {
            errorCode = 500
            // api 요청 실패
            Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
        }
        finally {
            isLoading = false

        }
    }



    var isSendEmailVerifyApiFinished by mutableStateOf(false)
    //이메일변경용 메일요청
    fun sendEmailVerificationForChange(email : String) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로
        isLoading = true
        viewModelScope.launch {
            try {
                val userEmail = SignUpApi.EmailRequest(email)
                val response = signUpApi.sendEmailVerificationForChange(bearerAccessToken,Token.refreshToken,userEmail)

                if (response.isSuccessful) {
                    Log.e("authApiSuccess2", "${response.headers()}")
                    Log.e("authApiSuccess2", "${response.code()}")

                    Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken
                    readMyInfo()
                    isSendEmailVerifyApiFinished = true

                } else {
                    Log.e("authApiFailed2", "Failed : ${response.headers()}")
                    Log.e("authApiFailed2", "${response.code()}")
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
            }
            finally {
                isLoading=false
            }
        }

    }

    fun requestChangePw(email : String){
        isLoading = true
        viewModelScope.launch {
            isLoading = true
            val userEmail = SignUpApi.EmailRequest(email)
            val response = signUpApi.requestChangePw(bearerAccessToken,Token.refreshToken,userEmail)

            if (response.code() == 201) { //성공
                Log.d("requestChangePw api header", "${response.headers()}")
                Log.d("requestChangePw api code", "${response.code()}")
                //헤더에 토큰이 없다.
                Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken
                isSendEmailVerifyApiFinished = true
            }
            else{
                // code == 400 잘못된 이메일주소
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")

            }
            isLoading = false

        }
    }

    fun verifyChangePw(token : String,newPw: String){
        viewModelScope.launch {

            val response = signUpApi.verifyChangePw(bearerAccessToken,Token.refreshToken,token)

            if (response.code() == 304) { //성공
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
                resetPw(token,newPw)
            }
            else{
                // code == 404 유효하지 않은토큰. 토큰을 못받았거나 10분이 지났거나
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")
            }

        }
    }

    private suspend fun resetPw(token : String, newPw : String)
    {
            val body = SignUpApi.ResetPwRequest(newPw,token)
            val response = signUpApi.resetPw(bearerAccessToken,Token.refreshToken,body)

            if (response.code() == 200) { //성공
                Log.e("authApiSuccess2", "${response.code()}")
            }
            else{
                Log.e("authApiFailed2", "${response.code()}")
            }
    }

    fun setAgreementOfSignUp(
        locationAgreement: Boolean,
        marketingAgreement: Boolean,
        serviceAlertAgreement: Boolean,
        navController: NavController,
        context: Context
    ) {
        val option = when {
            marketingAgreement -> NotificationSettings(serviceAlertAgreement, serviceAlertAgreement, true)
            else -> NotificationSettings(viewed = true, report = true, marketing = false)
        }

        viewModelScope.launch {
            try {
                // 사용자 기기 등록
                Log.d("약관 기기등록 ", "1")
                requestRegisterDevice(context)

                // 위치서비스 동의 등록
                Log.d("위치서비스 동의 등록 ", "2")
                if (locationAgreement) {
                    val userInfo = UserInfo(locationConsent = true)
                    val response = userApi.userUpdate(bearerAccessToken,Token.refreshToken, userInfo)

                    if (response.isSuccessful) {
                        Log.d(TAG, "위치서비스 동의 저장 성공: ${response.code()}")
                    } else {
                        Log.e(TAG, "위치서비스 동의 저장 실패: ${response.code()}")
                    }
                }

                // 사용자의 마케팅 동의
                Log.d("약관 동의 저장시작", "3")
                val response = supportApi.updateUserNotification(bearerAccessToken,Token.refreshToken, option)
                if (response.isSuccessful) {
                    Log.d(TAG, "마케팅 동의 저장 성공: ${response.code()}")

                    navController.navigate("SignUpComplete")
                } else {
                    Log.e(TAG, "마케팅 동의 저장 실패: ${response.code()}")
                    Log.e(TAG, "마케팅 동의 저장 실패: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("이용정보 동의 저장실패", "${e.printStackTrace()}")
            }
        }
    }

    suspend fun requestRegisterDevice(context: Context) {
        val ssaid = getSSAID(context)
        val token = getFCMToken() // Suspend 함수로 호출

        if (token != null) {
            // 서버에 토큰값 보내기 등의 작업을 여기서 처리할 수 있습니다.
            val body = SignUpApiImpl.RegisterDeviceRequest(ssaid, token)
            try {
                val response = supportApi.requestRegisterDevice(bearerAccessToken,Token.refreshToken, body)
                if (response.isSuccessful) {
                    Log.i("FCM Token", "ssaid 값 : $ssaid \n FCM token 값 : $token")
                    Log.d("기기 등록 성공입니다.", "success: ")
                } else {
                    Log.e("기기등록 실패", "Failed ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FCM Token", "Failed to fetch token")
            }
        } else {
            Log.e("FCM Token", "Failed to fetch token")
            // 토큰이 없으면 기기 등록도 안됨
        }
    }

    // FCM 토큰을 얻는 suspend 함수
    private suspend fun getFCMToken(): String? {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 작업이 성공하면 토큰 반환
                    continuation.resume(task.result)
                } else {
                    // 작업이 실패하면 null 반환
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    continuation.resume(null)
                }
            }
        }
    }

    private fun getSSAID(context: Context): String {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("DeviceID", "Device ID: $deviceId")
        return deviceId
    }

}
