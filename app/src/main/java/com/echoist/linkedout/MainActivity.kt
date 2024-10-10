package com.echoist.linkedout

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.navigation.MobileApp
import com.echoist.linkedout.presentation.TabletApp
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.presentation.home.ReLogInWaringAlert
import com.echoist.linkedout.presentation.myLog.mylog.ExitAppErrorBox
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.getCurrentRoute
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import com.echoist.linkedout.presentation.util.startActivityToPlayStore
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //top,bottom 시스템 바 등의 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val startDestination = viewModel.startDestination()

        setContent {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val navController = rememberNavController()
            val isReAuthenticationRequired by homeViewModel.isReAuthenticationRequired.collectAsState()

            var isClickedExit by remember {
                mutableStateOf(false)
            }

            val isTablet = ((resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE)
            
            LaunchedEffect(key1 = isClickedExit) {
                if (isClickedExit) {
                    delay(2000)
                    isClickedExit = false
                }
            }

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

                //release 버전에는 아직 테블릿 제외
                if (isTablet) {
                    TabletApp(navController, startDestination)
                } else { //mobile
                    MobileApp(navController, startDestination)
                }

                if (isClickedExit) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(bottom = 40.dp), contentAlignment = Alignment.BottomCenter
                    ) { ExitAppErrorBox() }
                }

                val currentRoute = getCurrentRoute(navController)
                //온보딩,로그인 화면 에서는 401에도 재 로그인 ui 표시 x
                if (isReAuthenticationRequired &&
                    currentRoute != Routes.OnBoarding && currentRoute != Routes.LoginPage
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) { }
                        .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center) {
                        ReLogInWaringAlert {
                            navigateWithClearBackStack(navController, Routes.LoginPage)
                            homeViewModel.setReAuthenticationRequired(false)
                            viewModel.saveClickedAutoLogin(false)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //카카오 sdk 초기화
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
        Firebase.auth.signOut()

        getSSAID(this)
        Log.d("SSAID", "SSAID: ${DeviceId.ssaid}") //고유식별자
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestAppVersion()

        lifecycleScope.launch {
            viewModel.isVersionMatching.collect { isVersionMatching ->
                if (!isVersionMatching) {
                    // 다른 클래스의 메서드 호출 시 this를 context로 전달
                    startActivityToPlayStore(this@MainActivity)
                }
            }
        }
    }

    private fun getSSAID(context: Context) {
        DeviceId.ssaid =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}