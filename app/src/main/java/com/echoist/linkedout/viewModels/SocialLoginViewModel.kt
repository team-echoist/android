package com.echoist.linkedout.viewModels

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.api.GoogleSignUpApi
import com.echoist.linkedout.api.NaverApiService
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.page.Token
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@HiltViewModel
class SocialLoginViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var accessToken by mutableStateOf("")

    var googleLoginstate = mutableStateOf(false)
    var kakaoLoginstate = mutableStateOf(false)
    var naverLoginstate = mutableStateOf(false)

    var googleUserToken by mutableStateOf("")
    var googleUserId by mutableStateOf("")
    var googleUserEmail by mutableStateOf("")


    var userId by mutableStateOf("")
    var userPw by mutableStateOf("")

    fun signInWithGoogle(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        context: Context
    ) {
        val token = BuildConfig.google_native_api_key //토큰값 -> local.properties 통해 git ignore
        Token.accessToken = token
        // Google 로그인을 구성합니다.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun handleGoogleLogin(data: Intent?, navController: NavController) {
        // Google ID 토큰을 사용하여 Firebase에 인증합니다.
        try {
            // Google 로그인이 성공하면 Firebase로 인증합니다.
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        googleLoginstate.value = true
                        // 로그인 성공
                        // 회원정보 획득 가능
                        googleUserId = account.id.toString()
                        googleUserToken = account.idToken.toString()
                        googleUserEmail = account.email.toString()
                        googleLogin(navController)
                        Log.d(
                            TAG,
                            "firebaseAuthWithGoogle current user:" + auth.currentUser!!.email.toString()
                        )
                        Log.d(
                            TAG,
                            "firebaseAuthWithGoogle current user:" + auth.currentUser!!.displayName.toString()
                        ) //회원 이름
                        Log.d(
                            TAG,
                            "firebaseAuthWithGoogle current user:" + auth.currentUser!!.photoUrl.toString()
                        ) //회원 사진 Url

                        // 추가 작업 수행
                    } else {
                        // 로그인 실패
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // 실패 처리 작업 수행
                    }
                }

        } catch (e: ApiException) { // sha-1 또는 app key가 잘못된 경우 로그확인. dialog메세지 띄워줄수있음
            Log.w("TAG", "GoogleSign in Failed", e)
        }
    }

    //서버로 구글로그인 인증받아온 후 홈화면진입.
    private fun googleLogin(navController: NavController) {
        viewModelScope.launch {
            try {
                val userAccount = GoogleSignUpApi.UserGoogleAccount(
                    googleUserToken,
                    googleUserId
                )
                val response = googleAuthApi.googleLogin(userAccount)

                if (response.isSuccessful) {
                    Log.d("tokentoken", response.headers()["authorization"].toString())
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    Log.d(TAG, "tokentoken"+Token.accessToken)
                    Log.e("authApiSuccess2", response.message())

                    navController.navigate("HOME")
                } else {
                    Log.e("authApiFailed2", "Failed : ${response.headers().get("authorization")}")
                    Log.e("authApifailed32", "${response.code()}")
                    Log.e("authApifailed32", googleUserToken)
                    Log.e("authApifailed32", googleUserId)
                    Log.e("authApifailed32", googleUserEmail)

                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
            }
        }
    }


    fun handleKaKaoLogin(context: Context, navController: NavController) {
        // 카카오 로그인 조합 예제

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                kakaoLoginstate.value = true
                navController.navigate("HOMEaccessToken")
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken} ")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    kakaoLoginstate.value = true
                    navController.navigate("HOMEaccessToken")
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }

        getKakaoUserInfo()

    }

    // 로그인과 동시에 사용자 정보 요청 (기본) 따로 메서드를 빼서 활용가능할듯합니다.
    // 메일이나 프로필, 등의 추가정보를 얻기위해선 비즈니스 앱 등록이 필요함.
    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }

    fun handleKaKaoLogout() {
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    //네이버 로그인 초기화
    fun initializeNaverLogin(context: Context) {
        val naverClientId = BuildConfig.naver_client_id
        val naverClientSecret = BuildConfig.naver_slient_secret
        val naverClientName = BuildConfig.naver_client_name
        NaverIdLoginSDK.initialize(context, naverClientId, naverClientSecret, naverClientName)
    }

    fun handleNaverLoginResult(result: ActivityResult, navController: NavController) {
        when (result.resultCode) {
            RESULT_OK -> {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d("Naver_getAccessToken", NaverIdLoginSDK.getAccessToken().toString())
                Log.d("Naver_getRefreshToken", NaverIdLoginSDK.getRefreshToken().toString())
                Log.d("Naver_getExpiresAt", NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("Naver_getTokenType", NaverIdLoginSDK.getTokenType().toString())
                Log.d("Naver_getState", NaverIdLoginSDK.getState().toString())

                naverLoginstate.value = true
                navController.navigate("HOMEaccessToken")
                // 로그인 성공 시 유저 정보 획득
                getNaverUserInfo()
            }

            RESULT_CANCELED -> {
                // 실패 or 에러
                Log.d("errorCode", NaverIdLoginSDK.getLastErrorCode().code)
                Log.d("errorDescription", NaverIdLoginSDK.getLastErrorDescription().toString())

                // Handle failure accordingly
            }
        }
    }

    fun handleNaverLogout() {
        NaverIdLoginSDK.logout()
    }

    private fun getNaverUserInfo() {
        val token = NaverIdLoginSDK.getAccessToken()
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val api = Retrofit
            .Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NaverApiService::class.java)

        if (token != null) {
            viewModelScope.launch {
                try {
                    val userInfo = api.getUserInfo("Bearer ${NaverIdLoginSDK.getAccessToken()}")
                    val info = userInfo.response
                    // info.name 등으로 유저 정보 획득
                    Log.d("NaverUserInfo success", info.name.toString())
                    Log.d("NaverUserInfo success", info.profile_image.toString())

                } catch (e: Exception) {
                    // 사용자 정보 가져오기 실패
                    Log.e("NaverUserInfo", "Failed to get user info: ${e.message}")
                }
            }
        } else {
            //토큰 없는 경우
            Log.e("NaverUserInfo", "Access token is null")
        }

    }

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val authApi = Retrofit
        .Builder()
        .baseUrl("https://www.linkedoutapp.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SignUpApi::class.java)

    private val googleAuthApi = Retrofit
        .Builder()
        .baseUrl("https://www.linkedoutapp.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(GoogleSignUpApi::class.java)

    //로그인
    fun login(navController: NavController) {
        viewModelScope.launch {
            try {
                val userAccount = SignUpApi.UserAccount(userId, userPw)
                val response = authApi.login(userAccount)


                if (response.isSuccessful) {
                    Log.d("tokentoken", response.headers()["authorization"].toString())
                    accessToken = (response.headers()["authorization"].toString())
                    Log.e("authApiSuccess2", response.body()?.success.toString())
                    Log.e("authApiSuccess2", response.message())
                    Log.e("authApiSuccess2", "${response.raw()}")
                    Log.e(
                        "authApiSuccess3",
                        "로그인 성공! ${response.headers()["authorization"]}"
                    ) // 이값을 항상 헤더에 넣을것.
                    Log.e("authApiSuccess3", "${response.code()}")
                    Log.e("authApiSuccess3 헤더", "${response.headers()}")

                    val encodedUrl = URLEncoder.encode( // http 인코드
                        "android-app://androidx.navigation/HOMEaccessToken",
                        StandardCharsets.UTF_8.toString()
                    )
                    navController.popBackStack("OnBoarding", false) //onboarding까지 전부 삭제.
                    navController.navigate("HOMEaccessToken")
                } else {
                    Log.e("authApiFailed2", "Failed : ${response.headers().get("authorization")}")
                    Log.e("authApifailed32", "${response.code()}")
                    Log.e("authApifailed32", response.message())

                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
            }
        }
    }
}


//로그인 성공시 일단 간단한 토스트메세지 띄움
@Composable
fun LoginSuccessDialog(successMsg: String, dialogState: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { dialogState.value = false },
        confirmButton = {
            Button(onClick = { dialogState.value = false }) {
                Text(text = "확인")
            }
        },
        text = { Text(text = successMsg) },
    )
}