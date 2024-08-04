package com.echoist.linkedout.page.login

import android.app.Activity
import android.content.Context
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
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.DeviceId
import com.echoist.linkedout.R
import com.echoist.linkedout.components.CropImagePage
import com.echoist.linkedout.page.community.CommunityDetailPage
import com.echoist.linkedout.page.community.CommunityPage
import com.echoist.linkedout.page.community.CommunitySavedEssayPage
import com.echoist.linkedout.page.community.FullSubscriberPage
import com.echoist.linkedout.page.home.DarkModeSettingPage
import com.echoist.linkedout.page.home.HomePage
import com.echoist.linkedout.page.home.InquiryPage
import com.echoist.linkedout.page.home.LinkedOutSupportPage
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
import com.echoist.linkedout.viewModels.WritingViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginPage : ComponentActivity() {
    private fun getSSAID(context: Context){
        DeviceId.deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun onStart() {
        super.onStart()
        //카카오 sdk 초기화
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
        Firebase.auth.signOut()

        getSSAID(this)
        Log.d("SSAID", "SSAID: ${DeviceId.deviceId}") //고유식별자
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel : HomeViewModel by viewModels()
        val viewModel : SocialLoginViewModel by viewModels()
        val writingViewModel : WritingViewModel by viewModels()
        val signUpViewModel : SignUpViewModel by viewModels()
        val myLogViewModel : MyLogViewModel by viewModels()
        val communityViewModel : CommunityViewModel by viewModels()
        val settingsViewModel : SettingsViewModel by viewModels()
        //top,bottom 시스템 바 등의 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val keyHash = Utility.getKeyHash(this)
            Log.d("Hash", keyHash)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "OnBoarding") {
                composable("OnBoarding") {
                    OnBoardingPage(navController)
                }
                composable("LoginPage"){
                    LoginPage(navController = navController, viewModel = viewModel,)
                }
                composable("SIGNUP") {
                    SignUpPage(navController, signUpViewModel)
                }
                composable("AgreeOfProvisionsPage") {
                    AgreeOfProvisionsPage(navController, signUpViewModel)
                }
                composable("SignUpComplete",
                    deepLinks = listOf(navDeepLink { uriPattern = "https://linkedoutapp.com/SignUpComplete?token={token}" }),
                    arguments = listOf(navArgument("token"){
                        type = NavType.StringType
                        defaultValue = ""
                    })
                ) {
                    //딥링크를 통해서 token을 받고 저장함. 없을 시 쓰던 토큰으로 사용함
                    if (it.arguments?.getString("token").toString().isNotEmpty()){
                        Token.accessToken = it.arguments?.getString("token").toString()
                        Log.i("header token by deepLink:", " ${Token.accessToken}")
                    }


                    SignUpCompletePage(homeViewModel,navController)
                }
                composable("HOME") {
                    HomePage(navController,homeViewModel,writingViewModel)
                }
                composable("DarkModeSettingPage") {
                    DarkModeSettingPage(navController)
                }
                composable("NotificationPage") {
                    NotificationPage(navController)
                }
                composable("NotificationSettingPage") {
                    NotificationSettingPage(navController)
                }
                composable("SupportPage") {
                    SupportPage(navController)
                }
                composable("LinkedOutSupportPage") {
                    LinkedOutSupportPage(navController)
                }
                composable("InquiryPage") {
                    InquiryPage(navController)
                }
                composable("UpdateHistoryPage") {
                    UpdateHistoryPage(navController)
                }
                composable("MYLOG") {
                    MyLogPage(navController = navController,myLogViewModel,writingViewModel)
                }
                composable("StoryPage",
                    enterTransition = { slideInVertically(initialOffsetY = { 2000 }, animationSpec = tween(durationMillis = 500) ) },
                    exitTransition = { slideOutVertically(targetOffsetY = { 2000 }, animationSpec = tween(durationMillis = 500)) })
                 {
                    StoryPage(myLogViewModel,navController)
                }
                composable("StoryDetailPage") {
                    StoryDetailPage(myLogViewModel,navController)
                }
                composable("DetailEssayInStoryPage") {
                    DetailEssayInStoryPage(navController,myLogViewModel,writingViewModel)
                }

                composable("MyLogDetailPage") {
                    MyLogDetailPage(navController = navController,myLogViewModel,writingViewModel)
                }
                composable("CompletedEssayPage") {
                    CompletedEssayPage(navController = navController,myLogViewModel,writingViewModel)
                }
                composable("COMMUNITY") {
                    CommunityPage(navController = navController,communityViewModel)
                    //community page
                }
                composable("CommunityDetailPage") {
                    CommunityDetailPage(navController,communityViewModel)
                    //community page
                }
                composable("CommunitySavedEssayPage") {
                    CommunitySavedEssayPage(navController,communityViewModel)
                    //community page
                }
                composable("SubscriberPage") {
                    ProfilePage(viewModel = communityViewModel, navController = navController)
                }
                composable("FullSubscriberPage") {
                    FullSubscriberPage(communityViewModel,navController)
                }
                composable("SETTINGS") {
                    MyPage(navController)
                    //settings page
                }
                composable("RecentViewedEssayPage") {
                    RecentViewedEssayPage(navController,communityViewModel)
                    //settings page
                }
                composable("RecentEssayDetailPage") {
                    RecentEssayDetailPage(navController,communityViewModel)
                    //settings page
                }
                composable("AccountPage",
                    deepLinks = listOf(navDeepLink { uriPattern = "https://linkedoutapp.com/AccountPage" })) {
                    AccountPage(navController)
                    //settings page
                }
                composable("ChangeEmailPage") {
                    ChangeEmailPage(navController)
                    //settings page
                }
                composable("ChangePwPage") {
                    ChangePwPage(navController)
                    //settings page
                }
                composable("ResetPwPageWithEmail") {
                    ResetPwPageWithEmail(navController)
                    //settings page
                }
                composable("ResetPwPage",
                    deepLinks = listOf(navDeepLink { uriPattern = "https://linkedoutapp.com/ResetPwPage?token={token}" }),
                    arguments = listOf(navArgument("token"){
                        type = NavType.StringType
                        defaultValue = ""
                    })) {
                    ResetPwPage(navController, it.arguments?.getString("token").toString())
                    //settings page
                }
                composable("AccountWithdrawalPage") {
                    AccountWithdrawalPage(navController)
                    //settings page
                }
                composable("BadgePage") {
                    BadgePage( navController,settingsViewModel)
                    //settings page
                }
                composable("WritingPage",
                    enterTransition = { slideInVertically(initialOffsetY = { 2000 }, animationSpec = tween(durationMillis = 500) ) },
                    exitTransition = { slideOutVertically(targetOffsetY = { 2000 }, animationSpec = tween(durationMillis = 500)) })
                {
                    WritingPage(navController, writingViewModel)
                }
                composable("WritingCompletePage") {
                    WritingCompletePage(navController, writingViewModel)
                }
                composable("TemporaryStoragePage")
                 {
                    TemporaryStoragePage(navController,writingViewModel)
                }
                composable("CropImagePage") {
                    CropImagePage(navController,writingViewModel)
                }
                composable("TermsAndConditionsPage") {
                    TermsAndConditionsPage(navController)
                }
                composable("PrivacyPolicyPage") {
                    PrivacyPolicyPage(navController)
                }
                composable("LocationPolicyPage") {
                    LocationPolicyPage(navController)
                }
                composable("FontCopyRight") {
                    FontCopyRight(navController)
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
                .clickable {viewModel.appleLoginHandle(activity,navController) },
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

                        tint = if (isSystemInDarkTheme()) Color.White else Color.Gray,
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
                        color = if (isSystemInDarkTheme()) Color.White else Color.Gray
                    )
                    Text(
                        text = "링크드아웃에 오신 것을 환영합니다",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                        color = if (isSystemInDarkTheme()) Color.White else Color.Gray
                    )
                    IdTextField(viewModel)
                    PwTextField(viewModel)

                    var clickedAutoLogin by remember { mutableStateOf(false) }
                    val autoLoginColor = if (clickedAutoLogin) LinkedInColor else Color.Gray

                    Row (modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically){
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = autoLoginColor,
                            contentDescription = "check",
                            modifier = Modifier
                                .clickable { clickedAutoLogin = !clickedAutoLogin }
                        )
                        Text(text = "자동 로그인", fontSize = 14.sp, color = autoLoginColor)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LoginBtn(navController = navController,viewModel)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        UnderlineText(text = "아이디 찾기") { } //아이디찾기 페이지 이동
                        UnderlineText(text = "비밀번호 재설정") { navController.navigate("ResetPwPageWithEmail")} //비밀번호 재설정 페이지 이동
                        UnderlineText(text = "회원가입") { navController.navigate("SIGNUP") } // 회원가입 페이지 이동
                    }
                    Spacer(modifier = Modifier.height(150.dp))

                    Row(
                        modifier = Modifier.padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp)) // 공간을 만듭니다
                        Text(
                            text = "간편 회원가입/로그인",
                            color = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp)) // 공간을 만듭니다
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        )
                    }

                    SocialLoginBar(navController, viewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        )
    }
}

@Composable
fun SocialLoginBar(navController: NavController, viewModel: SocialLoginViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
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
fun IdTextField(viewModel: SocialLoginViewModel) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { new ->
            text = new
            viewModel.userId = text
        },
        label = {
            Text(
                "이메일 주소 또는 아이디",
                color = if (isSystemInDarkTheme()) Color(0xFF919191) else Color.Gray,
                fontSize = 14.sp
            )
        }, // 힌트를 라벨로 설정합니다.
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
            unfocusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black


        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
    )
}

@Composable
fun PwTextField(viewModel: SocialLoginViewModel) {
    var text by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = text,
        onValueChange = { new ->
            text = new
            viewModel.userPw = text
        },
        label = { Text("비밀번호", color = Color(0xFF919191), fontSize = 14.sp) }, // 힌트를 라벨로 설정합니다.
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
            unfocusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black


        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = { // 비밀번호 표시 여부입니다.
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = if (passwordVisible) R.drawable.pw_eye else R.drawable.pw_eye_off),
                    contentDescription = "pw_eye"
                )

            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 13.dp)
    )
}

@Composable
fun LoginBtn(
    navController: NavController,
    viewModel: SocialLoginViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    // val isPressed by interactionSource.collectIsPressedAsState()
    val error = viewModel.userId.isEmpty() || viewModel.userPw.isEmpty()
    
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
        Text(text = "로그인")
    }
}

@Composable
fun UnderlineText(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 12.sp,
        style = TextStyle(textDecoration = TextDecoration.Underline),
        color = if (isSystemInDarkTheme()) Color(0xFF919191) else Color.Black,
        modifier = Modifier
            .padding(end = 25.dp)
            .clickable { onClick() }

    )
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


