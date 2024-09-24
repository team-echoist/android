package com.echoist.linkedout.presentation.login

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.data.api.NaverApiService
import com.echoist.linkedout.data.api.SignUpApi
import com.echoist.linkedout.data.api.SocialSignUpApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.TokensResponse
import com.echoist.linkedout.data.repository.TokenRepository
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.presentation.myLog.Token
import com.echoist.linkedout.presentation.util.calculateDateAfter30Days
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@HiltViewModel
class SocialLoginViewModel @Inject constructor(
    private val userApi: UserApi,
    private val exampleItems: ExampleItems,
    private val signUpApi: SignUpApi,
    private val socialSignUpApi: SocialSignUpApi,
    private val tokenRepository: TokenRepository,
    private val userDataRepository: UserDataRepository,
    private val context: Context
) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    val loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    private var loginToken by mutableStateOf("")
    private var loginUserId by mutableStateOf("")

    private val _clickedAutoLogin =
        MutableStateFlow(userDataRepository.getClickedAutoLogin())
    val clickedAutoLogin: StateFlow<Boolean> = _clickedAutoLogin

    var userId by mutableStateOf("")
    var userPw by mutableStateOf("")

    init {
        userDataRepository.saveIsOnboardingFinished(true)
        initializeNaverLogin()
    }

    fun onChangeClickAutoLogin() {
        _clickedAutoLogin.value = !_clickedAutoLogin.value
    }

    private suspend fun isFirstLoginAfterSignUp() {
        try {
            val response = userApi.getMyInfo()
            exampleItems.myProfile = response.data.user
            exampleItems.myProfile.essayStats = response.data.essayStats
            Log.i("본인 유저 정보 + 에세이", "readMyInfo: ${exampleItems.myProfile}")

            if (response.data.user.isFirst == true) {
                loginState.value = LoginState.AgreeOfProvisions
            } else {
                loginState.value = LoginState.Home(200)
            }
        } catch (e: Exception) {
            Log.e("내 정보 요청 에러", "readMyInfo: error")
            e.printStackTrace()
        }
    }

    //로그인
    fun login() {
        viewModelScope.launch {
            try {
                val userAccount = SignUpApi.UserAccount(userId, userPw)
                val response = signUpApi.login(userAccount)

                if (response.isSuccessful) {
                    Log.d("유저 상태코드", "${response.code()}")
                    Token.accessToken = response.body()!!.data.accessToken
                    Token.refreshToken = response.body()!!.data.refreshToken

                    tokenRepository.setReAuthenticationRequired(false)

                    if (clickedAutoLogin.value) {
                        saveLocalTokens(
                            Token.accessToken,
                            Token.refreshToken
                        )
                        userDataRepository.saveRefreshTokenValidTime(
                            calculateDateAfter30Days()
                        )
                    } else {
                        saveLocalTokens("", "")
                    }

                    when (response.code()) {
                        // 탈퇴 유저
                        202 -> {
                            loginState.value = LoginState.Home(response.code())
                        }

                        else -> isFirstLoginAfterSignUp()
                    }
                } else {
                    loginState.value = LoginState.Error(response.code().setErrorText())
                    delay(1000)
                    loginState.value = LoginState.Idle
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("login_failed", "Failed: ${e.message}")
            }
        }
    }

    fun signInWithGoogle(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        val token = BuildConfig.google_native_api_key //토큰값 -> local.properties 통해 git ignore
        // Google 로그인을 구성합니다.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun handleGoogleLogin(data: Intent?) {
        // Google ID 토큰을 사용하여 Firebase에 인증합니다.
        try {
            // Google 로그인이 성공하면 Firebase로 인증합니다.
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginUserId = account.id.toString()
                        loginToken = account.idToken.toString()

                        requestLogin(SocialLoginType.GOOGLE)
                        Log.i(
                            "Google_Firebase_UserInfo:", "구글 사용자 정보 요청 성공:" +
                                    "\nEmail: ${auth.currentUser?.email}" +
                                    "\nUser Token: $loginToken" +  //회원 토큰
                                    "\nPhoto URL: ${auth.currentUser?.photoUrl}" +  // 회원 사진 URL
                                    "\nUser ID: $loginUserId" //회원 아이디
                        )
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

    fun handleKaKaoLogin() {
        // 카카오 로그인 조합 예제

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { kakaoToken, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (kakaoToken != null) {
                loginToken = kakaoToken.accessToken
                getKakaoUserInfo()

                Log.i("Kakao_TokenInfo", "카카오계정으로 로그인 성공 ${kakaoToken.accessToken} ") //카카오 계정 토큰
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { kakaoToken, error ->
                if (error != null) {
                    Log.e("Kakao_err", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (kakaoToken != null) {
                    loginToken = kakaoToken.accessToken
                    Log.i("Kakao_Success", "카카오톡으로 로그인 성공 ${kakaoToken.accessToken}")

                    getKakaoUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    //카카오 유저 정보 획득
    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Kakao_err", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                loginUserId = user.id.toString()
                Log.i(
                    "Kakao_userInfo", "카카오 사용자 정보 요청 성공" +
                            "\n토큰: $loginUserId" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                requestLogin(SocialLoginType.KAKAO)
            }
        }
    }

    fun handleKaKaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    //네이버 로그인 초기화
    fun initializeNaverLogin() {
        val naverClientId = BuildConfig.naver_client_id
        val naverClientSecret = BuildConfig.naver_slient_secret
        val naverClientName = BuildConfig.naver_client_name
        NaverIdLoginSDK.initialize(context, naverClientId, naverClientSecret, naverClientName)
    }

    fun handleNaverLoginResult(result: ActivityResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                loginToken = NaverIdLoginSDK.getAccessToken() ?: "null"
                Log.i(
                    "Naver_TokenInfo", "Naver Token Information:" +
                            "\nAccess Token: ${NaverIdLoginSDK.getAccessToken()}" +
                            "\nRefresh Token: ${NaverIdLoginSDK.getRefreshToken()}" +
                            "\nExpires At: ${NaverIdLoginSDK.getExpiresAt()}" +
                            "\nToken Type: ${NaverIdLoginSDK.getTokenType()}" +
                            "\nState: ${NaverIdLoginSDK.getState()}"
                )
                getNaverUserInfo()
            }

            RESULT_CANCELED -> {
                viewModelScope.launch {
                    loginState.value =
                        LoginState.Error(NaverIdLoginSDK.getLastErrorDescription().toString())
                    delay(1000)
                    loginState.value = LoginState.Idle
                }
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
                    val userInfo = api.readUserInfo("Bearer ${NaverIdLoginSDK.getAccessToken()}")
                    val info = userInfo.response
                    loginUserId = info.id ?: "null"
                    Log.i(
                        "Naver_userInfo", "네이버 사용자 정보 요청 성공" +
                                "\n토큰: $loginToken" +
                                "\n이름: ${info.name}" +
                                "\n아이디: ${info.id}" +
                                "\n이메일: ${info.email}" +
                                "\n모바일: ${info.mobile}" +
                                "\n프로필사진: ${info.profile_image}"
                    )
                    requestLogin(SocialLoginType.NAVER)
                } catch (e: Exception) {
                    // 사용자 정보 가져오기 실패
                    e.printStackTrace()
                    Log.e("NaverUserInfo", "Failed to get user info: ${e.message}")
                }
            }
        } else {
            //토큰 없는 경우
            Log.e("NaverUserInfo", "Access token is null")
        }
    }

    fun appleLoginHandle(activity: Activity) {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.setScopes(arrayOf("email", "name").toMutableList())
        // Localize the Apple authentication screen in 한국어
        provider.addCustomParameter("locale", "ko")

        val pending = auth.pendingAuthResult
        if (pending != null) { //로그인한 정보가 있다면
            pending.addOnSuccessListener { authResult ->
                Log.d(TAG, "checkPending:onSuccess:$authResult")
                val credential = authResult.credential as OAuthCredential //토큰

                loginToken = credential.idToken!! //아마도 토큰
                loginUserId = authResult.user!!.uid //고유 아이디

                Log.i(
                    "Apple_Firebase_UserInfo:", "애플 사용자 정보 요청 성공:" +
                            "\nEmail: ${authResult.user!!.email}" +//이메일
                            "\nUser Token: $loginToken" +  //회원 토큰
                            "\nPhoto URL: ${authResult.user!!.photoUrl}" +  // 회원 사진 URL
                            "\nDisplay Name: ${authResult.user!!.displayName}" + //디스플레이 네임
                            "\nPhoneNumber: ${authResult.user!!.phoneNumber}" +//번호
                            "\nProviderId: ${authResult.user!!.providerId}" +// 제공자 파이어베이스
                            "\nProvider: ${authResult.credential!!.provider}" + // apple.com
                            "\nUid: $loginUserId" +
                            "\nProviderData: ${authResult.user!!.providerData}" +
                            "\nsecret : $${credential.secret}" +
                            "\nid 토큰 굉장히 긴것. : $${credential.idToken}"
                )
                requestLogin(SocialLoginType.APPLE)
            }.addOnFailureListener { e ->
                Log.w(TAG, "checkPending:onFailure", e)
            }
        } else { //처음로그인 이라면
            auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { authResult ->
                    authResult.additionalUserInfo

                    val credential = authResult.credential as OAuthCredential //토큰

                    loginToken = credential.idToken!! //아마도 토큰
                    loginUserId = authResult.user!!.uid //고유 아이디

                    Log.i(
                        "Apple_Firebase_UserInfo:", "애플 사용자 정보 요청 성공:" +
                                "\nEmail: ${authResult.user!!.email}" +//이메일
                                "\nUser Token: $loginToken" +  //회원 토큰
                                "\nPhoto URL: ${authResult.user!!.photoUrl}" +  // 회원 사진 URL
                                "\nDisplay Name: ${authResult.user!!.displayName}" + //디스플레이 네임
                                "\nPhoneNumber: ${authResult.user!!.phoneNumber}" +//번호
                                "\nProviderId: ${authResult.user!!.providerId}" +// 제공자 파이어베이스
                                "\nProvider: ${authResult.credential!!.provider}" + // apple.com
                                "\nUid: $loginUserId" +
                                "\nProviderData: ${authResult.user!!.providerData}" +
                                "\nsecret : $${credential.secret}" +
                                "\nid토큰 굉장히 긴것. : $${credential.idToken}"
                    )
                    requestLogin(SocialLoginType.APPLE)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "activitySignIn:onFailure", e)
                }
        }
    }

    private fun requestLogin(loginType: SocialLoginType) {
        requestSocialLogin(loginToken) { userAccount ->
            when (loginType) {
                SocialLoginType.APPLE -> socialSignUpApi.requestAppleLogin(userAccount)
                SocialLoginType.GOOGLE -> socialSignUpApi.requestGoogleLogin(userAccount)
                SocialLoginType.KAKAO -> socialSignUpApi.requestKakaoLogin(userAccount)
                SocialLoginType.NAVER -> socialSignUpApi.requestNaverLogin(userAccount)
            }
        }
    }

    private fun saveLocalTokens(accessToken: String, refreshToken: String) {
        userDataRepository.saveTokensInfo(accessToken, refreshToken)
        userDataRepository.saveClickedAutoLogin(clickedAutoLogin.value)
    }

    private fun requestSocialLogin(
        userToken: String,
        requestLogin: suspend (SocialSignUpApi.UserAccount) -> Response<TokensResponse>
    ) {
        viewModelScope.launch {
            try {
                val userAccount = SocialSignUpApi.UserAccount(userToken)
                val response = requestLogin(userAccount)

                if (response.isSuccessful) {
                    val accessToken = response.body()!!.data.accessToken
                    val refreshToken = response.body()!!.data.refreshToken

                    Token.accessToken = accessToken
                    Token.refreshToken = refreshToken
                    tokenRepository.setReAuthenticationRequired(false)

                    if (response.code() == 202) {
                        loginState.value = LoginState.Home(response.code())
                    } else {
                        // 첫 회원가입 여부 확인하고 화면 이동
                        _clickedAutoLogin.value = true
                        saveLocalTokens(accessToken, refreshToken)
                        userDataRepository.saveRefreshTokenValidTime(
                            calculateDateAfter30Days()
                        ) // 토큰 유효 기간 저장
                        isFirstLoginAfterSignUp()
                    }
                } else {
                    loginState.value = LoginState.Error(response.code().setErrorText())
                    delay(1000)
                    loginState.value = LoginState.Idle
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun Int.setErrorText(): String {
    return when (this) { // todo 유형 별 코드 파악
        400, 401 -> "이메일 또는 비밀번호가 잘못되었습니다."
        403 -> "정지된 계정입니다."
        409 -> "중복된 이메일 계정이 존재합니다."
        500 -> "예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        else -> "예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
    }
}

enum class SocialLoginType {
    APPLE,
    GOOGLE,
    KAKAO,
    NAVER
}

sealed class LoginState {
    object Idle : LoginState()
    object AgreeOfProvisions : LoginState()
    data class Home(val statusCode: Int) : LoginState()
    data class Error(val message: String) : LoginState()
}