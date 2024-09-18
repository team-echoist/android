package com.echoist.linkedout.page.login

import android.app.Activity
import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.R
import com.echoist.linkedout.Routes
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.SocialLoginViewModel
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.delay

@Composable
fun LoginPage(
    navController: NavController,
    viewModel: SocialLoginViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val autoLoginColor =
        if (viewModel.clickedAutoLogin) LinkedInColor else Color.Gray
    viewModel.clickedAutoLogin = SharedPreferencesUtil.getClickedAutoLogin(context)

    // 앱버전 체크 후 최신버전 아닌경우 마켓업데이트.
    LaunchedEffect(key1 = Unit) {
        // 온보딩 확인했다는 체크
        SharedPreferencesUtil.saveIsOnboardingFinished(context, true)
    }

    // 로그인 상태코드로 인한 에러처리.
    LaunchedEffect(key1 = viewModel.loginStatusCode) {
        delay(1000)
        viewModel.loginStatusCode = 200
    }

    val errorText = when (viewModel.loginStatusCode) { // todo 유형 별 코드 파악
        400, 401 -> "이메일 또는 비밀번호가 잘못되었습니다."
        403 -> "정지된 계정입니다."
        409 -> "중복된 이메일 계정이 존재합니다."
        500 -> "예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        else -> "예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해 주세요. ${viewModel.loginStatusCode}"
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

                LoginTextFields(viewModel, navController)

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
                SocialLoginBar(navController, viewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 에러 박스
            AnimatedVisibility(
                visible = viewModel.loginStatusCode >= 400,
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
                            text = errorText,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun LoginTextFields(viewModel: SocialLoginViewModel, navController: NavController) {
    val passwordFocusRequester = remember { FocusRequester() }

    IdTextField(viewModel, passwordFocusRequester)
    PwTextField(navController, viewModel, passwordFocusRequester)
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
    navController: NavController,
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

@Composable
fun LoginBtn(
    navController: NavController,
    viewModel: SocialLoginViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
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
fun SocialLoginBar(navController: NavController, viewModel: SocialLoginViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GoogleLoginBtn(navController = navController, viewModel)
        KakaoLoginBtn(navController = navController, viewModel)
        NaverLoginBtn(navController = navController, viewModel)
        AppleLoginBtn(navController = navController, viewModel)
    }
}

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

enum class Keyboard {
    Opened, Closed
}