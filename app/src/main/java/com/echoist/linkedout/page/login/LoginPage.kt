package com.echoist.linkedout.page.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.DeviceId
import com.echoist.linkedout.R
import com.echoist.linkedout.Routes
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.components.CropImagePage
import com.echoist.linkedout.data.Notice
import com.echoist.linkedout.navigation.TabletNavHost
import com.echoist.linkedout.page.community.CommunityDetailPage
import com.echoist.linkedout.page.community.CommunityPage
import com.echoist.linkedout.page.community.CommunitySavedEssayPage
import com.echoist.linkedout.page.community.FullSubscriberPage
import com.echoist.linkedout.page.home.DarkModeSettingPage
import com.echoist.linkedout.page.home.HomePage
import com.echoist.linkedout.page.home.InquiryPage
import com.echoist.linkedout.page.home.LinkedOutSupportPage
import com.echoist.linkedout.page.home.MyBottomNavigation
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
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.SettingsViewModel
import com.echoist.linkedout.viewModels.SignUpViewModel
import com.echoist.linkedout.viewModels.SocialLoginViewModel
import com.echoist.linkedout.viewModels.SupportViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class LoginPage : ComponentActivity() {
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

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Notice::class.java)

        super.onCreate(savedInstanceState)

        val homeViewModel: HomeViewModel by viewModels()
        val viewModel: SocialLoginViewModel by viewModels()
        val writingViewModel: WritingViewModel by viewModels()
        val signUpViewModel: SignUpViewModel by viewModels()
        val myLogViewModel: MyLogViewModel by viewModels()
        val communityViewModel: CommunityViewModel by viewModels()
        val settingsViewModel: SettingsViewModel by viewModels()
        val supportViewModel: SupportViewModel by viewModels()
        //top,bottom 시스템 바 등의 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            val keyHash = Utility.getKeyHash(this)
            Log.d("Hash", keyHash)
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            Log.d("navBackStackEntry", navBackStackEntry.toString())
            val showBottomBar =
                navBackStackEntry?.destination?.route?.startsWith(Routes.Home) == true ||
                        navBackStackEntry?.destination?.route == Routes.Community ||
                        navBackStackEntry?.destination?.route?.startsWith(Routes.MyLog) == true ||
                        navBackStackEntry?.destination?.route == Routes.Settings

            val isTablet = ((resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE)

            if (isTablet) {
                LinkedOutTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
                            floatingActionButton = {
                                if (navBackStackEntry?.destination?.route?.startsWith(Routes.Home) == true ||
                                    navBackStackEntry?.destination?.route?.startsWith(Routes.MyLog) == true
                                ) {
                                    FloatingActionButton(
                                        modifier = Modifier.padding(end = 25.dp, bottom = 25.dp),
                                        onClick = {
                                            navController.navigate(Routes.WritingPage)
                                            homeViewModel.initializeDetailEssay()
                                            homeViewModel.setStorageEssay(EssayApi.EssayItem())
                                        },
                                        shape = RoundedCornerShape(100.dp),
                                        containerColor = Color.White
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ftb_edit),
                                            contentDescription = "edit",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color.Black
                                        )
                                    }
                                }
                            },
                            bottomBar = {
                                if (showBottomBar) {
                                    MyBottomNavigation(navController = navController)
                                }
                            }) { _ ->
                            TabletNavHost(
                                navController = navController
                            )
                        }
                    }
                }
            } else {
                NavHost(navController = navController, startDestination = Routes.OnBoarding) {
                    composable(Routes.OnBoarding) {
                        OnBoardingPage(navController)
                    }
                    composable(Routes.LoginPage) {
                        LoginPage(navController = navController, viewModel = viewModel)
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
                        arguments = listOf(navArgument("statusCode") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val statusCode = backStackEntry.arguments?.getInt("statusCode") ?: 200
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
                        arguments = listOf(navArgument("noticeJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val noticeJson = backStackEntry.arguments?.getString("noticeJson")
                        val decodedJson =
                            noticeJson?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.name()) }
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
                        DetailEssayInStoryPage(navController, myLogViewModel, writingViewModel)
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
                        ProfilePage(viewModel = communityViewModel, navController = navController)
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
                        ResetPwPage(navController, it.arguments?.getString("token").toString())
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
        }
    }
}

//구글로그인 버튼
@Composable
fun GoogleLoginBtn(navController: NavController, viewModel: SocialLoginViewModel) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        viewModel.handleGoogleLogin(result.data, navController)
    }
    Icon(
        painter = painterResource(id = R.drawable.social_googlebtn),
        contentDescription = "naver Login btn",
        modifier = Modifier
            .size(40.dp)
            .clickable { viewModel.signInWithGoogle(launcher, context) },
        tint = Color.Unspecified
    )
}

@Composable
fun KakaoLoginBtn(navController: NavController, viewModel: SocialLoginViewModel) {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.social_kakaobtn),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { viewModel.handleKaKaoLogin(context, navController) },
            tint = Color.Unspecified
        )
    }
}

@Composable
fun NaverLoginBtn(navController: NavController, viewModel: SocialLoginViewModel) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleNaverLoginResult(result, navController)
    }
    viewModel.initializeNaverLogin(context)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.social_naverbtn),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { NaverIdLoginSDK.authenticate(context, launcher) },
            tint = Color.Unspecified
        )
    }
}

@Composable
fun AppleLoginBtn(navController: NavController, viewModel: SocialLoginViewModel) {

    val context = LocalContext.current
    val activity = context as Activity

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.social_applebtn),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { viewModel.appleLoginHandle(activity, navController) },
            tint = Color.Unspecified
        )
    }
}


@Composable
fun LoginPage(
    navController: NavController,
    viewModel: SocialLoginViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Log.d("현재 사용중인 안드로이드 앱 버전", BuildConfig.VERSION_NAME)
    viewModel.clickedAutoLogin = SharedPreferencesUtil.getClickedAutoLogin(LocalContext.current)

    //앱버전 체크 후 최신버전 아닌경우 마켓업데이트.
    LaunchedEffect(key1 = Unit) {
        viewModel.requestAppVersion()
    }

    LaunchedEffect(key1 = viewModel.loginStatusCode) {
        delay(1000)
        viewModel.loginStatusCode = 200
    }
    val errorText = when (viewModel.loginStatusCode) { //todo 유형 별 코드 파악
        400, 401 -> "이메일 또는 비밀번호가 잘못되었습니다."
        403 -> "정지된 계정입니다."
        409 -> "중복된 이메일 계정이 존재합니다."
        else -> viewModel.loginStatusCode.toString()
    }

    LinkedOutTheme {
        Scaffold(
            modifier = Modifier.pointerInput(Unit) { //배경 터치 시 키보드 숨김
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrowback",

                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(16.dp)
                            .clickable { navController.popBackStack() } //뒤로가기
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "안녕하세요!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.White,
                    )
                    Text(
                        text = "링크드아웃에 오신 것을 환영합니다",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                        color = Color.White,
                    )

                    LoginTextFields(viewModel, navController)

                    val autoLoginColor =
                        if (viewModel.clickedAutoLogin) LinkedInColor else Color.Gray

                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = autoLoginColor,
                            contentDescription = "check",
                            modifier = Modifier
                                .clickable {
                                    viewModel.clickedAutoLogin = !viewModel.clickedAutoLogin
                                }
                        )
                        Text(text = "자동 로그인", fontSize = 14.sp, color = autoLoginColor)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LoginBtn(navController = navController, viewModel)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // UnderlineText(text = "아이디 찾기") { } //아이디찾기 페이지 이동
                        //임시 아이디 찾기 닫아둠.
                        LinkedOutTheme {
                            Text(
                                text = "아이디 찾기",
                                fontSize = 12.sp,
                                style = TextStyle(textDecoration = TextDecoration.LineThrough),
                                color = Color(0xFF919191),
                                modifier = Modifier
                                    .padding(end = 25.dp)
                                    .clickable(enabled = false) { }
                            )
                        }
                        UnderlineText(text = "비밀번호 재설정") { navController.navigate("ResetPwPageWithEmail") } //비밀번호 재설정 페이지 이동
                        UnderlineText(text = "회원가입") { navController.navigate("SIGNUP") } // 회원가입 페이지 이동
                    }
                    Spacer(modifier = Modifier.height(150.dp))

                    Row(
                        modifier = Modifier.padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp)) // 공간을 만듭니다
                        Text(
                            text = "간편 회원가입/로그인",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp)) // 공간을 만듭니다
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        )
                    }
                    SocialLoginBar(navController, viewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                //에러 박스
                AnimatedVisibility(
                    visible = viewModel.loginStatusCode >= 400,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 0, easing = LinearEasing))
                )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.7f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                                .height(60.dp)
                                .padding(horizontal = 20.dp)
                                .background(
                                    Color(0xFFE43446),
                                    shape = RoundedCornerShape(10)
                                )
                        ) {
                            Text(
                                text = errorText,
                                color = Color.White,
                                modifier = Modifier.align(
                                    Alignment.Center
                                )
                            )
                        }

                    }
                }
            }
        )
    }
}

@Composable
fun LoginTextFields(viewModel: SocialLoginViewModel, navController: NavController) {
    val passwordFocusRequester = remember { FocusRequester() }

    IdTextField(viewModel, passwordFocusRequester)
    PwTextField(navController, viewModel, passwordFocusRequester)
}

@Composable
fun SocialLoginBar(navController: NavController, viewModel: SocialLoginViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoogleLoginBtn(navController = navController, viewModel)
        Spacer(modifier = Modifier.width(25.dp))
        KakaoLoginBtn(navController = navController, viewModel)
        Spacer(modifier = Modifier.width(25.dp))
        NaverLoginBtn(navController = navController, viewModel)
        Spacer(modifier = Modifier.width(25.dp))
        AppleLoginBtn(navController = navController, viewModel)
    }
}

@Composable
fun IdTextField(viewModel: SocialLoginViewModel, passwordFocusRequester: FocusRequester) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(SharedPreferencesUtil.getLocalAccountInfo(context).id) }
    viewModel.userId = text //초기값 설정 빈값으로 두지 않기위함

    TextField(
        value = text,
        onValueChange = { new ->
            text = new
            viewModel.userId = text
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                passwordFocusRequester.requestFocus()
            }
        ),
        label = {
            Text(
                "이메일 주소 또는 아이디",
                color = Color(0xFF919191),
                fontSize = 14.sp
            )
        }, // 힌트를 라벨로 설정합니다.
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF252525),
            unfocusedContainerColor = Color(0xFF252525)
        ),
        trailingIcon = {
            if (text.isNotEmpty())
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "cancel",
                    modifier = Modifier.clickable {
                        text = ""
                        viewModel.userId = ""
                    }
                )
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
    )
}

@Composable
fun PwTextField(
    navController: NavController,
    viewModel: SocialLoginViewModel,
    passwordFocusRequester: FocusRequester
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(SharedPreferencesUtil.getLocalAccountInfo(context).pw) }
    viewModel.userPw = text
    var passwordVisible by remember { mutableStateOf(false) }

    LinkedOutTheme {
        TextField(
            value = text,
            onValueChange = { new ->
                text = new
                viewModel.userPw = text
            },
            singleLine = true,
            label = { Text("비밀번호", color = Color(0xFF919191), fontSize = 14.sp) }, // 힌트를 라벨로 설정합니다.
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF252525),
                unfocusedContainerColor = Color(0xFF252525)
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.login(navController)
                }
            ),
            trailingIcon = { // 비밀번호 표시 여부입니다.
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (text.isNotEmpty())
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "cancel",
                            modifier = Modifier.clickable {
                                text = ""
                                viewModel.userPw = ""
                            }
                        )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = if (passwordVisible) R.drawable.pw_eye_on else R.drawable.pw_eye_off),
                            contentDescription = "pw_eye"
                        )

                    }
                }

            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 13.dp)
                .focusRequester(passwordFocusRequester)
        )
    }
}

@Composable
fun LoginBtn(
    navController: NavController,
    viewModel: SocialLoginViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    // val isPressed by interactionSource.collectIsPressedAsState()
    val error = viewModel.userId.isEmpty() || viewModel.userPw.isEmpty()


    LinkedOutTheme {
        Button(
            shape = RoundedCornerShape(10.dp),
            enabled = !error,
            onClick = {
                viewModel.login(navController)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (error) Color.Gray else LinkedInColor
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(text = "로그인", color = Color.Black)
        }


    }
}

@Composable
fun UnderlineText(
    text: String,
    onClick: () -> Unit
) {
    LinkedOutTheme {
        Text(
            text = text,
            fontSize = 12.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            color = Color(0xFF919191),
            modifier = Modifier
                .padding(end = 25.dp)
                .clickable { onClick() }
        )
    }
}

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }
    return keyboardState
}