package com.echoist.linkedout.presentation.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.navercorp.nid.NaverIdLoginSDK

@Composable
fun LoginPage(
    navController: NavController,
    viewModel: SocialLoginViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val clickedAutoLogin by viewModel.clickedAutoLogin.collectAsState()

    val autoLoginColor =
        if (clickedAutoLogin) LinkedInColor else Color.Gray

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Home -> navigateWithClearBackStack(
                navController,
                "${Routes.Home}/${(loginState as LoginState.Home).statusCode}"
            )

            is LoginState.AgreeOfProvisions -> navController.navigate(Routes.AgreeOfProvisionsPage)

            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) { // 배경 터치 시 키보드 숨김
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
                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = "안녕하세요!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.White
                )
                Text(
                    text = "링크드아웃에 오신 것을 환영합니다",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                    color = Color.White
                )

                LoginTextFields(viewModel)

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
                                viewModel.onChangeClickAutoLogin()
                            }
                    )
                    Text(text = "자동 로그인", fontSize = 14.sp, color = autoLoginColor)
                }
                Spacer(modifier = Modifier.height(10.dp))
                LoginBtn(viewModel)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 임시 아이디 찾기 닫아둠.
                    Text(
                        text = "아이디 찾기",
                        fontSize = 12.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color = Color(0xFF919191),
                        modifier = Modifier
                            .padding(end = 25.dp)
                            .clickable(enabled = false) { }
                    )
                    UnderlineText(text = "비밀번호 재설정") { navController.navigate(Routes.ResetPwPageWithEmail) }
                    UnderlineText(text = "회원가입") { navController.navigate(Routes.SignUp) }
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
                SocialLoginBar(viewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (loginState is LoginState.Error) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 0, easing = LinearEasing))
                ) {
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
                                text = (loginState as LoginState.Error).message,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LoginTextFields(viewModel: SocialLoginViewModel) {
    val passwordFocusRequester = remember { FocusRequester() }

    IdTextField(viewModel, passwordFocusRequester)
    PwTextField(viewModel, passwordFocusRequester)
}

@Composable
fun IdTextField(viewModel: SocialLoginViewModel, passwordFocusRequester: FocusRequester) {
    var text by remember { mutableStateOf("") }
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
    viewModel: SocialLoginViewModel,
    passwordFocusRequester: FocusRequester
) {
    var text by remember { mutableStateOf("") }
    viewModel.userPw = text
    var passwordVisible by remember { mutableStateOf(false) }

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
                viewModel.login()
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

@Composable
fun LoginBtn(
    viewModel: SocialLoginViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    val error = viewModel.userId.isEmpty() || viewModel.userPw.isEmpty()

    Button(
        shape = RoundedCornerShape(10.dp),
        enabled = !error,
        onClick = {
            viewModel.login()
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
        Text(text = "로그인", color = if (error) Color(0xFF919191) else Color.Black)
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
        color = Color(0xFF919191),
        modifier = Modifier
            .padding(end = 25.dp)
            .clickable { onClick() }
    )
}

@Composable
fun SocialLoginBar(viewModel: SocialLoginViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GoogleLoginBtn(viewModel)
        KakaoLoginBtn(viewModel)
        NaverLoginBtn(viewModel)
        AppleLoginBtn(viewModel)
    }
}

@Composable
fun GoogleLoginBtn(viewModel: SocialLoginViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        viewModel.handleGoogleLogin(result.data)
    }
    Icon(
        painter = painterResource(id = R.drawable.social_googlebtn),
        contentDescription = "naver Login btn",
        modifier = Modifier
            .size(40.dp)
            .clickable { viewModel.signInWithGoogle(launcher) },
        tint = Color.Unspecified
    )
}

@Composable
fun KakaoLoginBtn(viewModel: SocialLoginViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.social_kakaobtn),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { viewModel.handleKaKaoLogin() },
            tint = Color.Unspecified
        )
    }
}

@Composable
fun NaverLoginBtn(viewModel: SocialLoginViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleNaverLoginResult(result)
    }
    viewModel.initializeNaverLogin()

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
fun AppleLoginBtn(viewModel: SocialLoginViewModel) {
    val context = LocalContext.current
    val activity = context as Activity

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.social_applebtn),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { viewModel.appleLoginHandle(activity) },
            tint = Color.Unspecified
        )
    }
}