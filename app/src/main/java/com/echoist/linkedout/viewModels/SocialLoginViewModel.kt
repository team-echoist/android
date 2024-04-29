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
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.BuildConfig
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

class SocialLoginViewModel : ViewModel() {
    private val auth : FirebaseAuth = Firebase.auth
    var googleLoginstate = mutableStateOf(false)
    var kakaoLoginstate = mutableStateOf(false)
    var naverLoginstate = mutableStateOf(false)


    val userName = auth.currentUser?.displayName.toString()

    fun signInWithGoogle(
        launcher: ManagedActivityResultLauncher<Intent,androidx.activity.result.ActivityResult>,
        context: Context
    ){
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

    fun handleGoogleSignInResult(data: Intent?, navController: NavController) {
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
                        navController.navigate("screen2")
                        Log.d("TAG", "Google Sign In Success")
                        Log.d(TAG, "firebaseAuthWithGoogle id:" + account.id)
                        Log.d(TAG, "firebaseAuthWithGoogle idtoken:" + account.idToken) //토큰값.
                        Log.d(TAG, "firebaseAuthWithGoogle current user:" + auth.currentUser!!.displayName.toString()) //회원 이름
                        Log.d(TAG, "firebaseAuthWithGoogle current user:" + auth.currentUser!!.photoUrl.toString()) //회원 사진 Url

                        // 추가 작업 수행
                    } else {
                        // 로그인 실패
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // 실패 처리 작업 수행
                    }
                }

        }catch (e: ApiException) { // sha-1 또는 app key가 잘못된 경우 로그확인. dialog메세지 띄워줄수있음
                Log.w("TAG", "GoogleSign in Failed", e)
        }
    }

    fun handleKaKaoLogin(context: Context){
        // 카카오 로그인 조합 예제

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                kakaoLoginstate.value = true
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
    private fun getKakaoUserInfo(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
        }
    }

    fun handleKaKaoLogout(){
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
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

    fun handleNaverLoginResult(result: ActivityResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d("Naver_getAccessToken", NaverIdLoginSDK.getAccessToken().toString())
                Log.d("Naver_getRefreshToken", NaverIdLoginSDK.getRefreshToken().toString())
                Log.d("Naver_getExpiresAt", NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("Naver_getTokenType", NaverIdLoginSDK.getTokenType().toString())
                Log.d("Naver_getState", NaverIdLoginSDK.getState().toString())
                naverLoginstate.value = true
                // Handle success accordingly
            }
            RESULT_CANCELED -> {
                // 실패 or 에러
                Log.d("errorCode", NaverIdLoginSDK.getLastErrorCode().code)
                Log.d("errorDescription", NaverIdLoginSDK.getLastErrorDescription().toString())

                // Handle failure accordingly
            }
        }
    }



}

//로그인 성공시 일단 간단한 토스트메세지 띄움
@Composable
fun LoginSuccessDialog(successMsg : String, dialogState: MutableState<Boolean>){
    AlertDialog(
        onDismissRequest = { dialogState.value = false},
        confirmButton = {
            Button(onClick = { dialogState.value = false },) {
                Text(text = "확인")
            }
        },
        text = { Text(text = successMsg)},
    )
}