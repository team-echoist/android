package com.echoist.linkedout.viewModels

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
import androidx.navigation.NavController
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.Routes
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.TokenRepository
import com.echoist.linkedout.api.NaverApiService
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.api.SocialSignUpApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.calculateDateAfter30Days
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.TokensResponse
import com.echoist.linkedout.page.myLog.Token
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
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var googleLoginstate = mutableStateOf(false)
    var kakaoLoginstate = mutableStateOf(false)
    var naverLoginstate = mutableStateOf(false)
    var isSocialLoginLoading by mutableStateOf(false)

    private var googleUserToken by mutableStateOf("")
    private var googleUserId by mutableStateOf("")

    private var kakaoUserToken by mutableStateOf("")
    private var kakaoUserId by mutableStateOf("")

    private var naverUserToken by mutableStateOf("")
    private var naverUserId by mutableStateOf("")

    private var appleUserToken by mutableStateOf("")
    private var appleUserId by mutableStateOf("")

    var clickedAutoLogin by mutableStateOf(false)

    var userId by mutableStateOf("")
    var userPw by mutableStateOf("")

    private suspend fun readMyInfo(navController: NavController) {

        try {
            Log.d("헤더 토큰 여부", Token.accessToken)
            val response = userApi.getMyInfo()

            Log.d(TAG, "readMyInfo: suc1")
            Log.d("헤더 토큰", Token.accessToken)
            Log.i("본인 유저 정보 + 에세이", "readMyInfo: ${response.data.user}")
            exampleItems.myProfile = response.data.user
            exampleItems.myProfile.essayStats = response.data.essayStats
            Log.i("본인 유저 정보 + 에세이", "readMyInfo: ${exampleItems.myProfile}")

            if (response.data.user.isFirst == true) {
                Log.d("첫 회원가입 여부 체크", "true")
                navController.navigate("AgreeOfProvisionsPage")
            } else { //아니라면 바로 홈화면으로 이동
                Log.d("첫 회원가입 여부 체크", "false")
                navController.navigate("${Routes.Home}/200")
            }
        } catch (e: Exception) {
            Log.e("내 정보 요청 에러", "readMyInfo: error")
            e.printStackTrace()
        }
    }

    var loginStatusCode by mutableStateOf(200)

    //로그인
    fun login(navController: NavController) {
        viewModelScope.launch {
            try {
                val userAccount = SignUpApi.UserAccount(userId, userPw)
                val response = signUpApi.login(userAccount)

                if (response.isSuccessful) {
                    Log.d("유저 상태코드", "${response.code()}")
                    Token.accessToken = response.body()!!.data.accessToken
                    Token.refreshToken = response.body()!!.data.refreshToken

                    loginStatusCode = response.code()
                    navController.popBackStack("OnBoarding", false) //onboarding까지 전부 삭제.
                    tokenRepository.setReAuthenticationRequired(false)

                    when {
                        //자동로그인 클릭 + 로그인 성공 시 엑세스토큰, 리프레시토큰 저장 + 토큰의 30일 이후 날짜 계산
                        clickedAutoLogin -> {
                            saveLocalTokens(
                                navController.context,
                                Token.accessToken,
                                Token.refreshToken
                            )
                            SharedPreferencesUtil.saveRefreshTokenValidTime(
                                navController.context,
                                calculateDateAfter30Days()
                            ) //토큰 유효 기간 저장
                        }
                        //자동로그인 클릭x 시 빈 값 저장
                        !clickedAutoLogin -> saveLocalTokens(navController.context, "", "")
                    }

                    when (response.code()) {
                        202 -> { // 탈퇴유저. 정보 읽지 않고 홈으로 이동.
                            Log.d("유저 상태코드", "${response.code()} 탈퇴 신청 유저 입니다. ")
                            navController.navigate("HOME/${response.code()}")
                        }

                        else -> readMyInfo(navController)// 첫 회원가입 여부 확인하고 화면이동
                    }

                } else {
                    loginStatusCode = response.code()
                    Log.e("login_failed", "$loginStatusCode")
                    Log.e("login_failed", response.message())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("login_failed", "Failed: ${e.message}")
            }
        }
    }

    fun signInWithGoogle(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        context: Context
    ) {
        isSocialLoginLoading = true
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
                        googleUserId = account.id.toString()
                        googleUserToken = account.idToken.toString()

                        requestGoogleLogin(navController)
                        Log.i(
                            "Google_Firebase_UserInfo:", "구글 사용자 정보 요청 성공:" +
                                    "\nEmail: ${auth.currentUser?.email}" +
                                    "\nUser Token: $googleUserToken" +  //회원 토큰
                                    "\nPhoto URL: ${auth.currentUser?.photoUrl}" +  // 회원 사진 URL
                                    "\nUser ID: $googleUserId" //회원 아이디
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

    //서버로 구글로그인 인증받아온 후 홈화면진입.
    private fun requestGoogleLogin(navController: NavController) {
        requestSocialLogin(navController, googleUserToken, "구글") { userAccount ->
            socialSignUpApi.requestGoogleLogin(userAccount)
        }
    }


    fun handleKaKaoLogin(context: Context, navController: NavController) {
        // 카카오 로그인 조합 예제

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { kakaoToken, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (kakaoToken != null) {
                kakaoLoginstate.value = true
                kakaoUserToken = kakaoToken.accessToken
                getKakaoUserInfo(navController)

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
                    kakaoLoginstate.value = true
                    kakaoUserToken = kakaoToken.accessToken
                    Log.i("Kakao_Success", "카카오톡으로 로그인 성공 ${kakaoToken.accessToken}")

                    getKakaoUserInfo(navController)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    //카카오 유저 정보 획득
    private fun getKakaoUserInfo(navController: NavController) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Kakao_err", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                kakaoUserId = user.id.toString()
                Log.i(
                    "Kakao_userInfo", "카카오 사용자 정보 요청 성공" +
                            "\n토큰: $kakaoUserToken" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )

                requestKakaoLogin(navController)

            }
        }
    }

    // 카카오 sdk 거친 후에 서버로 카카오인증 토큰,아이디 보내고 홈화면진입
    private fun requestKakaoLogin(navController: NavController) {
        requestSocialLogin(navController, kakaoUserToken, "카카오") { userAccount ->
            socialSignUpApi.requestKakaoLogin(userAccount)
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
                naverUserToken = NaverIdLoginSDK.getAccessToken() ?: "null"
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.i(
                    "Naver_TokenInfo", "Naver Token Information:" +
                            "\nAccess Token: ${NaverIdLoginSDK.getAccessToken()}" +
                            "\nRefresh Token: ${NaverIdLoginSDK.getRefreshToken()}" +
                            "\nExpires At: ${NaverIdLoginSDK.getExpiresAt()}" +
                            "\nToken Type: ${NaverIdLoginSDK.getTokenType()}" +
                            "\nState: ${NaverIdLoginSDK.getState()}"
                )

                naverLoginstate.value = true
                // 로그인 성공 시 유저 정보 획득
                getNaverUserInfo(navController)
            }

            RESULT_CANCELED -> {
                // 실패 or 에러
                Log.d("Naver_errorCode", NaverIdLoginSDK.getLastErrorCode().code)
                Log.d(
                    "Naver_errorDescription",
                    NaverIdLoginSDK.getLastErrorDescription().toString()
                )

                // Handle failure accordingly
            }
        }
    }

    fun handleNaverLogout() {
        NaverIdLoginSDK.logout()
    }

    private fun getNaverUserInfo(navController: NavController) {
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
                    naverUserId = info.id ?: "null"
                    Log.i(
                        "Naver_userInfo", "네이버 사용자 정보 요청 성공" +
                                "\n토큰: $naverUserToken" +
                                "\n이름: ${info.name}" +
                                "\n아이디: ${info.id}" +
                                "\n이메일: ${info.email}" +
                                "\n모바일: ${info.mobile}" +
                                "\n프로필사진: ${info.profile_image}"
                    )
                    requestNaverLogin(navController)
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

    // 네이버 sdk 거친 후에 서버로 카카오인증 토큰,아이디 보내고 홈화면진입
    private fun requestNaverLogin(navController: NavController) {
        requestSocialLogin(navController, naverUserToken, "네이버") { userAccount ->
            socialSignUpApi.requestNaverLogin(userAccount)
        }
    }

    fun appleLoginHandle(activity: Activity, navController: NavController) {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.setScopes(arrayOf("email", "name").toMutableList())
        // Localize the Apple authentication screen in 한국어
        provider.addCustomParameter("locale", "ko")

        val pending = auth.pendingAuthResult
        if (pending != null) { //로그인한 정보가 있다면
            pending.addOnSuccessListener { authResult ->
                Log.d(TAG, "checkPending:onSuccess:$authResult")
                val credential = authResult.credential as OAuthCredential //토큰


                appleUserToken = credential.idToken!! //아마도 토큰
                appleUserId = authResult.user!!.uid //고유 아이디

                Log.i(
                    "Apple_Firebase_UserInfo:", "애플 사용자 정보 요청 성공:" +
                            "\nEmail: ${authResult.user!!.email}" +//이메일
                            "\nUser Token: $appleUserToken" +  //회원 토큰
                            "\nPhoto URL: ${authResult.user!!.photoUrl}" +  // 회원 사진 URL
                            "\nDisplay Name: ${authResult.user!!.displayName}" + //디스플레이 네임
                            "\nPhoneNumber: ${authResult.user!!.phoneNumber}" +//번호
                            "\nProviderId: ${authResult.user!!.providerId}" +// 제공자 파이어베이스
                            "\nProvider: ${authResult.credential!!.provider}" + // apple.com
                            "\nUid: $appleUserId" +
                            "\nProviderData: ${authResult.user!!.providerData}" +
                            "\nsecret : $${credential.secret}" +
                            "\nid 토큰 굉장히 긴것. : $${credential.idToken}"

                )
                requestAppleLogin(navController = navController)

            }.addOnFailureListener { e ->
                Log.w(TAG, "checkPending:onFailure", e)
            }
        } else { //처음로그인 이라면
            Log.d(TAG, "pending: null")
            auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { authResult ->
                    // Sign-in successful!
                    authResult.additionalUserInfo

                    val credential = authResult.credential as OAuthCredential //토큰

                    appleUserToken = credential.idToken!! //아마도 토큰
                    appleUserId = authResult.user!!.uid //고유 아이디

                    Log.i(
                        "Apple_Firebase_UserInfo:", "애플 사용자 정보 요청 성공:" +
                                "\nEmail: ${authResult.user!!.email}" +//이메일
                                "\nUser Token: $appleUserToken" +  //회원 토큰
                                "\nPhoto URL: ${authResult.user!!.photoUrl}" +  // 회원 사진 URL
                                "\nDisplay Name: ${authResult.user!!.displayName}" + //디스플레이 네임
                                "\nPhoneNumber: ${authResult.user!!.phoneNumber}" +//번호
                                "\nProviderId: ${authResult.user!!.providerId}" +// 제공자 파이어베이스
                                "\nProvider: ${authResult.credential!!.provider}" + // apple.com
                                "\nUid: $appleUserId" +
                                "\nProviderData: ${authResult.user!!.providerData}" +
                                "\nsecret : $${credential.secret}" +
                                "\nid토큰 굉장히 긴것. : $${credential.idToken}"

                    )

                    requestAppleLogin(navController = navController)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "activitySignIn:onFailure", e)
                }
        }


    }

    private fun requestAppleLogin(navController: NavController) {
        requestSocialLogin(navController, appleUserToken, "애플") { userAccount ->
            socialSignUpApi.requestAppleLogin(userAccount)
        }
    }

    private fun saveLocalTokens(context: Context, accessToken: String, refreshToken: String) {
        SharedPreferencesUtil.saveTokensInfo(context, accessToken, refreshToken)
        SharedPreferencesUtil.saveClickedAutoLogin(context, clickedAutoLogin)
    }

    private fun requestSocialLogin(
        navController: NavController,
        userToken: String,
        loginType: String,
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

                    Log.i("server header access token($loginType)", accessToken)
                    Log.i("server refresh token($loginType)", refreshToken)
                    loginStatusCode = response.code()

                    if (response.code() == 202) {
                        // 탈퇴유저. 첫유저일리 없고 정보읽지않고 홈으로 이동.
                        navController.navigate("HOME/${response.code()}")
                    } else {
                        // 첫 회원가입 여부 확인하고 화면 이동
                        clickedAutoLogin = true
                        saveLocalTokens(navController.context, accessToken, refreshToken)
                        SharedPreferencesUtil.saveRefreshTokenValidTime(
                            navController.context,
                            calculateDateAfter30Days()
                        ) // 토큰 유효 기간 저장
                        readMyInfo(navController)
                    }
                } else {
                    loginStatusCode = response.code()
                    Log.e("$loginType 서버와 api", "Failed ${response.code()}")
                }
            } catch (e: Exception) {
                // API 요청 실패
                e.printStackTrace()
                Log.e("$loginType login failed", "Failed: ${e.message}")
            }
        }
    }
}
