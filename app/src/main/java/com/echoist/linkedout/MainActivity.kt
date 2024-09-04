package com.echoist.linkedout

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.components.ExitAppErrorBox
import com.echoist.linkedout.navigation.MobileApp
import com.echoist.linkedout.page.home.ReLogInWaringBox
import com.echoist.linkedout.page.myLog.Token
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
    class MainActivity : ComponentActivity() {
    private fun getSSAID(context: Context) {
        DeviceId.ssaid =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun onStart() {
        super.onStart()
        //카카오 sdk 초기화
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
        Firebase.auth.signOut()

        getSSAID(this)
        Log.d("SSAID", "SSAID: ${DeviceId.ssaid}") //고유식별자
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //top,bottom 시스템 바 등의 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        //온보딩을 한번 진행했다면 다음부턴 안나오게.
        val isOnboardingFinished = SharedPreferencesUtil.getIsOnboardingFinished(this)
        val isAutoLoginClicked = SharedPreferencesUtil.getClickedAutoLogin(this)
        val tokenValidTime = SharedPreferencesUtil.getRefreshTokenValidTime(this)
        val tokens = SharedPreferencesUtil.getTokensInfo(this)

        val startDestination = when { //자동로그인 + 토큰만료 전까지 home으로
            isAutoLoginClicked && isDateAfterToday(tokenValidTime) -> {
                Token.accessToken = tokens.accessToken
                Token.refreshToken = tokens.refreshToken
                "${Routes.Home}/200"
            }

            isOnboardingFinished -> Routes.LoginPage
            else -> Routes.OnBoarding
        }

        setContent {
            var isClickedExit by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = isClickedExit) {
                if (isClickedExit) {
                    delay(2000)
                    isClickedExit = false
                }
            }

            val navController = rememberNavController()

            val isTablet = ((resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE)

            LinkedOutTheme(navController = navController) {

                BackHandler { // 백키 이벤트 핸들링
                    if (navController.previousBackStackEntry == null) {
                        if (isClickedExit) {
                            this.finish() //두번클릭 시 앱종료
                        } else isClickedExit = true //한번클릭시 경고

                        // 추가적으로 원하는 동작 수행
                    } else {
                        navController.popBackStack() // 백 스택이 있다면 일반적으로 뒤로 가기
                    }
                }

//                  release 버전에는 아직 테블릿 제외
//                if (isTablet) {
//                    TabletApp(this, navController)
//                } else { //mobile
//                    MobileApp(navController, startDestination)
//                }

                MobileApp(navController, startDestination)

                if (isClickedExit) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(bottom = 40.dp), contentAlignment = Alignment.BottomCenter
                    ) { ExitAppErrorBox() }
                }
                if (AuthManager.isReAuthenticationRequired.value) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) { }
                        .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center) {
                        ReLogInWaringBox {
                            navigateWithClearBackStack(navController, Routes.LoginPage)
                            AuthManager.isReAuthenticationRequired.value = false
                        }
                    }
                }
            }
        }
    }
}
