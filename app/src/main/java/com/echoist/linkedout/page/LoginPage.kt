package com.echoist.linkedout.page

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.R
import com.echoist.linkedout.components.CropImagePage
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.LoginSuccessDialog
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
    override fun onStart() {
        super.onStart()
        //카카오 sdk 초기화
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
        Firebase.auth.signOut()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel : SocialLoginViewModel by viewModels()
        val writingViewModel : WritingViewModel by viewModels()
        val signUpViewModel : SignUpViewModel by viewModels()

        setContent {
            val keyHash = Utility.getKeyHash(this)
            Log.d("Hash", keyHash)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "OnBoarding") {
                composable("OnBoarding") {
                    OnBoardingPage(navController)
                }
                composable("LoginPage"){navBackStackEntry ->
                    LoginPage(
                        navController = navController,
                        viewModel = viewModel,
                       "emptyToken"
                    )

                }
                composable("SIGNUP") {
                    SignUpPage(navController, signUpViewModel)
                }
                composable(
                    "HOME/{accessToken}",
                    arguments = listOf(navArgument("accessToken") {
                        type = NavType.StringType
                    })
                ) {navBackStackEntry->
                    HomePage(
                        navController,
                        navBackStackEntry.arguments?.getString("accessToken").toString()
                    )
                }
                composable("MYLOG") {
                    //mylog page
                }
                composable("COMMUNITY") {
                    //community page
                }
                composable("SETTINGS") {
                    //settings page
                }
                composable(
                    "WritingPage/{accessToken}",
                    arguments = listOf(
                        navArgument("accessToken") {
                        type = NavType.StringType
                    })
                ) {navBackStackEntry->
                    WritingPage(
                        navController,
                        writingViewModel,
                        navBackStackEntry.arguments?.getString("accessToken").toString()
                    )
                }
                composable(
                    "WritingCompletePage/{accessToken}",
                    arguments = listOf(
                        navArgument("accessToken") {
                            type = NavType.StringType
                        })
                ) {navBackStackEntry->
                    WritingCompletePage(
                        navController,
                        writingViewModel,
                        navBackStackEntry.arguments?.getString("accessToken").toString()
                    )
                }
                composable("CropImagePage") {
                    CropImagePage(navController,writingViewModel)
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

    if (viewModel.googleLoginstate.value) {
        LoginSuccessDialog("google 로그인성공", viewModel.googleLoginstate)
    }
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
    if (viewModel.kakaoLoginstate.value) {
        LoginSuccessDialog("kakao 로그인성공", viewModel.kakaoLoginstate)
    }

}

@Composable
fun NaverLoginBtn(navController: NavController, viewModel: SocialLoginViewModel) {
    val context = LocalContext.current
    Log.d("naverviewmodel", viewModel.toString())


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
    if (viewModel.naverLoginstate.value) {
        LoginSuccessDialog("naver 로그인성공", viewModel.naverLoginstate)
    }

}

@Composable
fun AppleLoginBtn(navController: NavController, viewModel: SocialLoginViewModel) {

    val context = LocalContext.current
    Log.d("appleviewmodel", viewModel.toString())

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.social_applebtn),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable {/* todo 애플로그인 로직 구현필요 */ },
            tint = Color.Unspecified
        )


    }
    if (viewModel.naverLoginstate.value) {
        LoginSuccessDialog("apple 로그인성공", viewModel.naverLoginstate)
    }

}


@Composable
fun LoginPage(
    navController: NavController,
    viewModel: SocialLoginViewModel,
    accessToken: String
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    viewModel.accessToken = accessToken

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

                    LoginBtn(navController = navController,viewModel)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        UnderlineText(text = "아이디 찾기") { } //아이디찾기 페이지 이동
                        UnderlineText(text = "비밀번호 재설정") { } //비밀번호 재설정 페이지 이동
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
                }
            }
        )
    }
}

@Composable
fun SocialLoginBar(navController: NavController, viewModel: SocialLoginViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            .padding(start = 16.dp, end = 16.dp, bottom = 42.dp)
    )
}

@Composable
fun LoginBtn(
    navController: NavController,
    viewModel: SocialLoginViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        shape = RoundedCornerShape(10.dp),
        onClick = {
            viewModel.login(navController)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) {
                if (isPressed) Color.LightGray else Color.White
            } else {
                if (isPressed) Color.Gray else Color.Black
            }
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


