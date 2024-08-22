package com.echoist.linkedout.presentation

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.echoist.linkedout.R
import com.echoist.linkedout.page.login.IdTextField
import com.echoist.linkedout.page.login.PwTextField
import com.echoist.linkedout.page.login.UnderlineText
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SocialLoginViewModel
import com.navercorp.nid.NaverIdLoginSDK

@Composable
fun TabletLoginRoute(
    navController: NavHostController,
    viewModel: SocialLoginViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    LaunchedEffect(key1 = Unit) {
        viewModel.requestAppVersion()
        viewModel.initializeNaverLogin(context)
    }

    TabletLoginScreen(
        navController = navController,
        viewModel = viewModel,
        horizontalPadding = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 350 else 100,
        onBackPressed = { onBackPressed() },
        navigateToResetPassword = { navigateToResetPassword() },
        navigateToSignUp = { navigateToSignUp() },
        onLoginButtonClick = { viewModel.login(navController) },
        onGoogleLoginClick = {
            val launcher = rememberLauncher(navController) { result, navController ->
                viewModel.handleGoogleLogin(result?.data, navController)
            }
            viewModel.signInWithGoogle(launcher, context)
        },
        onKakaoLoginClick = { viewModel.handleKaKaoLogin(context, navController) },
        onNaverLoginClick = {
            val launcher = rememberLauncher(navController) { result, navController ->
                result?.let { viewModel.handleNaverLoginResult(it, navController) }
            }
            NaverIdLoginSDK.authenticate(context, launcher)
        },
        onAppleLoginClick = {
            val activity = context as Activity
            viewModel.appleLoginHandle(activity, navController)
        }
    )
}

@Composable
fun rememberLauncher(
    navController: NavController,
    handleLoginResult: (result: ActivityResult?, navController: NavController) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        handleLoginResult(result, navController)
    }
}

@Composable
internal fun TabletLoginScreen(
    navController: NavHostController,
    viewModel: SocialLoginViewModel,
    horizontalPadding: Int = 100,
    onBackPressed: () -> Unit,
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit,
    onLoginButtonClick: () -> Unit,
    onGoogleLoginClick: @Composable () -> Unit,
    onKakaoLoginClick: () -> Unit,
    onNaverLoginClick: @Composable () -> Unit,
    onAppleLoginClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val interactionSource = remember { MutableInteractionSource() }
    val error = viewModel.userId.isEmpty() || viewModel.userPw.isEmpty()

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
                        .padding(horizontal = horizontalPadding.dp)
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
                            .clickable { onBackPressed() } //뒤로가기
                    )
                    Spacer(modifier = Modifier.height(70.dp))
                    Text(
                        text = "안녕하세요! 태블릿입니다.",
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

                    var clickedAutoLogin by remember { mutableStateOf(false) }
                    val autoLoginColor = if (clickedAutoLogin) LinkedInColor else Color.Gray

                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        enabled = !error,
                        onClick = {
                            onLoginButtonClick()
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        UnderlineText(text = "아이디 찾기") { }
                        UnderlineText(text = "비밀번호 재설정") { navigateToResetPassword() }
                        UnderlineText(text = "회원가입") { navigateToSignUp() }
                    }
                    Spacer(modifier = Modifier.height(80.dp))

                    Row(
                        modifier = Modifier.padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        )
                        Text(
                            text = "간편 회원가입/로그인",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        )
                    }
                    SocialLoginButtonGroup(
                        onGoogleLoginClick = onGoogleLoginClick,
                        onKakaoLoginClick = onKakaoLoginClick,
                        onNaverLoginClick = onNaverLoginClick,
                        onAppleLoginClick = onAppleLoginClick
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        )
    }
}

@Composable
fun LoginTextFields(
    viewModel: SocialLoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val passwordFocusRequester = remember { FocusRequester() }

    IdTextField(viewModel, passwordFocusRequester)
    PwTextField(navController, viewModel, passwordFocusRequester)
}

@Composable
fun SocialLoginButtonGroup(
    onGoogleLoginClick: @Composable () -> Unit,
    onKakaoLoginClick: () -> Unit,
    onNaverLoginClick: @Composable () -> Unit,
    onAppleLoginClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SocialLoginButton(
            iconRes = R.drawable.social_googlebtn,
            contentDescription = "Google Login",
            onClick = { onGoogleLoginClick() }
        )

        SocialLoginButton(
            iconRes = R.drawable.social_kakaobtn,
            contentDescription = "Kakao Login",
            onClick = { onKakaoLoginClick() }
        )

        SocialLoginButton(
            iconRes = R.drawable.social_naverbtn,
            contentDescription = "Naver Login",
            onClick = { onNaverLoginClick() }
        )

        SocialLoginButton(
            iconRes = R.drawable.social_applebtn,
            contentDescription = "Apple Login",
            onClick = { onAppleLoginClick() }
        )
    }
}

@Composable
fun SocialLoginButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: @Composable () -> Unit
) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick },
        tint = Color.Unspecified
    )
}
