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
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.echoist.linkedout.components.CropImagePage
import com.echoist.linkedout.components.ExitAppErrorBox
import com.echoist.linkedout.data.Notice
import com.echoist.linkedout.page.community.CommunityDetailPage
import com.echoist.linkedout.page.community.CommunityPage
import com.echoist.linkedout.page.community.CommunitySavedEssayPage
import com.echoist.linkedout.page.community.FullSubscriberPage
import com.echoist.linkedout.page.home.DarkModeSettingPage
import com.echoist.linkedout.page.home.HomePage
import com.echoist.linkedout.page.home.InquiryPage
import com.echoist.linkedout.page.home.LinkedOutSupportPage
import com.echoist.linkedout.page.home.NoticeDetailPage
import com.echoist.linkedout.page.home.NoticePage
import com.echoist.linkedout.page.home.NotificationPage
import com.echoist.linkedout.page.home.NotificationSettingPage
import com.echoist.linkedout.page.home.SupportPage
import com.echoist.linkedout.page.home.UpdateHistoryPage
import com.echoist.linkedout.page.home.legal_Notice.FontCopyRight
import com.echoist.linkedout.page.home.legal_Notice.LocationPolicyPage
import com.echoist.linkedout.page.home.legal_Notice.PrivacyPolicyPage
import com.echoist.linkedout.page.home.legal_Notice.TermsAndConditionsPage
import com.echoist.linkedout.page.login.AgreeOfProvisionsPage
import com.echoist.linkedout.page.login.LoginPage
import com.echoist.linkedout.page.login.OnBoardingPage
import com.echoist.linkedout.page.login.SignUpCompletePage
import com.echoist.linkedout.page.login.SignUpPage
import com.echoist.linkedout.page.myLog.CompletedEssayPage
import com.echoist.linkedout.page.myLog.DetailEssayInStoryPage
import com.echoist.linkedout.page.myLog.MyLogDetailPage
import com.echoist.linkedout.page.myLog.MyLogPage
import com.echoist.linkedout.page.myLog.StoryDetailPage
import com.echoist.linkedout.page.myLog.StoryPage
import com.echoist.linkedout.page.myLog.TemporaryStoragePage
import com.echoist.linkedout.page.myLog.Token
import com.echoist.linkedout.page.myLog.WritingCompletePage
import com.echoist.linkedout.page.myLog.WritingPage
import com.echoist.linkedout.page.settings.AccountPage
import com.echoist.linkedout.page.settings.AccountWithdrawalPage
import com.echoist.linkedout.page.settings.BadgePage
import com.echoist.linkedout.page.settings.ChangeEmailPage
import com.echoist.linkedout.page.settings.ChangePwPage
import com.echoist.linkedout.page.settings.MyPage
import com.echoist.linkedout.page.settings.ProfilePage
import com.echoist.linkedout.page.settings.RecentEssayDetailPage
import com.echoist.linkedout.page.settings.RecentViewedEssayPage
import com.echoist.linkedout.page.settings.ResetPwPage
import com.echoist.linkedout.page.settings.ResetPwPageWithEmail
import com.echoist.linkedout.presentation.TabletApp
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.SettingsViewModel
import com.echoist.linkedout.viewModels.SignUpViewModel
import com.echoist.linkedout.viewModels.SupportViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
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

            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Notice::class.java)

            super.onCreate(savedInstanceState)

            val homeViewModel: HomeViewModel by viewModels()
            val writingViewModel: WritingViewModel by viewModels()
            val signUpViewModel: SignUpViewModel by viewModels()
            val myLogViewModel: MyLogViewModel by viewModels()
            val communityViewModel: CommunityViewModel by viewModels()
            val settingsViewModel: SettingsViewModel by viewModels()
            val supportViewModel: SupportViewModel by viewModels()

//        lifecycleScope.launch {
//            signUpViewModel.requestRegisterDevice(context = applicationContext)
//        }
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

                    if (isTablet) {
                        TabletApp(this, navController, homeViewModel, myLogViewModel)
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(Routes.OnBoarding) {
                                OnBoardingPage(navController)
                            }
                            composable(Routes.LoginPage) {
                                LoginPage(navController = navController)
                            }
                            composable(Routes.SignUp) {
                                SignUpPage(navController, signUpViewModel)
                            }
                            composable(Routes.AgreeOfProvisionsPage) {
                                AgreeOfProvisionsPage(navController, signUpViewModel)
                            }
                            composable(Routes.SignUpComplete) {
                                SignUpCompletePage(homeViewModel, navController)
                            }
                            composable(
                                "${Routes.Home}/{statusCode}",
                                arguments = listOf(navArgument("statusCode") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val statusCode =
                                    backStackEntry.arguments?.getInt("statusCode") ?: 200
                                HomePage(navController, homeViewModel, writingViewModel, statusCode)
                            }
                            composable(Routes.DarkModeSettingPage) {
                                DarkModeSettingPage(navController)
                            }
                            composable(Routes.NotificationPage) {
                                NotificationPage(navController)
                            }
                            composable(Routes.NotificationSettingPage) {
                                NotificationSettingPage(navController)
                            }
                            composable(Routes.SupportPage) {
                                SupportPage(navController)
                            }
                            composable(Routes.LinkedOutSupportPage) {
                                LinkedOutSupportPage(navController)
                            }
                            composable(Routes.InquiryPage) {
                                InquiryPage(navController)
                            }
                            composable(Routes.NoticePage) {
                                NoticePage(navController, supportViewModel)
                            }
                            composable(
                                route = "${Routes.NoticeDetailPage}/{noticeJson}",
                                arguments = listOf(navArgument("noticeJson") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val noticeJson = backStackEntry.arguments?.getString("noticeJson")
                                val decodedJson =
                                    noticeJson?.let {
                                        URLDecoder.decode(
                                            it,
                                            StandardCharsets.UTF_8.name()
                                        )
                                    }
                                val notice = jsonAdapter.fromJson(decodedJson!!)

                                NoticeDetailPage(
                                    navController,
                                    notice ?: Notice(0, "no title", "no content", "2024 08 03")
                                )
                            }
                            composable(Routes.UpdateHistoryPage) {
                                UpdateHistoryPage(navController)
                            }
                            composable(
                                route = "${Routes.MyLog}/{page}",
                                arguments = listOf(navArgument("page") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val page = backStackEntry.arguments?.getInt("page") ?: 0
                                MyLogPage(navController, myLogViewModel, writingViewModel, page)

                            }
                            composable(Routes.StoryPage,
                                enterTransition = {
                                    slideInVertically(
                                        initialOffsetY = { 2000 },
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                },
                                exitTransition = {
                                    slideOutVertically(
                                        targetOffsetY = { 2000 },
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                            ) {
                                StoryPage(myLogViewModel, navController)
                            }
                            composable(Routes.StoryDetailPage) {
                                StoryDetailPage(myLogViewModel, navController)
                            }
                            composable(Routes.DetailEssayInStoryPage) {
                                DetailEssayInStoryPage(
                                    navController,
                                    myLogViewModel,
                                    writingViewModel
                                )
                            }
                            composable(Routes.MyLogDetailPage) {
                                MyLogDetailPage(
                                    navController = navController,
                                    myLogViewModel,
                                    writingViewModel
                                )
                            }
                            composable(Routes.CompletedEssayPage) {
                                CompletedEssayPage(
                                    navController = navController,
                                    myLogViewModel,
                                    writingViewModel
                                )
                            }
                            composable(Routes.Community) {
                                CommunityPage(navController = navController, communityViewModel)
                            }
                            composable(Routes.CommunityDetailPage) {
                                CommunityDetailPage(navController, communityViewModel)
                            }
                            composable(Routes.CommunitySavedEssayPage) {
                                CommunitySavedEssayPage(navController, communityViewModel)
                            }
                            composable(Routes.SubscriberPage) {
                                ProfilePage(
                                    viewModel = communityViewModel,
                                    navController = navController
                                )
                            }
                            composable(Routes.FullSubscriberPage) {
                                FullSubscriberPage(communityViewModel, navController)
                            }
                            composable(Routes.Settings) {
                                MyPage(navController)
                            }
                            composable(Routes.RecentViewedEssayPage) {
                                RecentViewedEssayPage(navController, communityViewModel)
                            }
                            composable(Routes.RecentEssayDetailPage) {
                                RecentEssayDetailPage(navController, communityViewModel)
                            }
                            composable(
                                Routes.AccountPage,
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "https://linkedoutapp.com/${Routes.AccountPage}"
                                })
                            ) {
                                AccountPage(navController)
                            }
                            composable(Routes.ChangeEmailPage) {
                                ChangeEmailPage(navController)
                            }
                            composable(Routes.ChangePwPage) {
                                ChangePwPage(navController)
                            }
                            composable(Routes.ResetPwPageWithEmail) {
                                ResetPwPageWithEmail(navController)
                            }
                            composable(
                                Routes.ResetPwPage,
                                deepLinks = listOf(navDeepLink {
                                    uriPattern =
                                        "https://linkedoutapp.com/${Routes.ResetPwPage}?token={token}"
                                }),

                                arguments = listOf(navArgument("token") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                })
                            ) {
                                if (it.arguments?.getString("token").toString().isNotEmpty()) {
                                    Token.accessToken = it.arguments?.getString("token").toString()
                                    Log.i("header token by deepLink:", " ${Token.accessToken}")
                                }
                                ResetPwPage(
                                    navController,
                                    it.arguments?.getString("token").toString()
                                )
                            }
                            composable(Routes.AccountWithdrawalPage) {
                                AccountWithdrawalPage(navController)
                            }
                            composable(Routes.BadgePage) {
                                BadgePage(navController, settingsViewModel)
                            }
                            composable(
                                Routes.WritingPage,
                                enterTransition = {
                                    slideInVertically(
                                        initialOffsetY = { 2000 },
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                },
                                exitTransition = {
                                    slideOutVertically(
                                        targetOffsetY = { 2000 },
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                            ) {
                                WritingPage(navController, writingViewModel)
                            }
                            composable(Routes.WritingCompletePage) {
                                WritingCompletePage(navController, writingViewModel)
                            }
                            composable(Routes.TemporaryStoragePage) {
                                TemporaryStoragePage(navController, writingViewModel)
                            }
                            composable(Routes.CropImagePage) {
                                CropImagePage(navController, writingViewModel)
                            }
                            composable(Routes.TermsAndConditionsPage) {
                                TermsAndConditionsPage(navController)
                            }
                            composable(Routes.PrivacyPolicyPage) {
                                PrivacyPolicyPage(navController)
                            }
                            composable(Routes.LocationPolicyPage) {
                                LocationPolicyPage(navController)
                            }
                            composable(Routes.FontCopyRight) {
                                FontCopyRight(navController)
                            }

                        }
                    }
                    if (isClickedExit) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .navigationBarsPadding()
                                .padding(bottom = 40.dp), contentAlignment = Alignment.BottomCenter
                        ) { ExitAppErrorBox() }
                    }
                }


            }
        }
    }
