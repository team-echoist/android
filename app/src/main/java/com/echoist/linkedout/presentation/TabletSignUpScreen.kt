package com.echoist.linkedout.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.echoist.linkedout.page.login.SendSignUpFinishedAlert
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletSignUpRoute(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current

    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(key1 = viewModel.isSignUpApiFinished) {
        //이메일 인증 클릭 성공시 모달 올라오게.
        if (viewModel.isSignUpApiFinished){
            bottomSheetState.show()
            delay(3000)
            viewModel.isSignUpApiFinished = false
        }
    }

    LaunchedEffect(key1 = viewModel.errorCode) {
        if (viewModel.errorCode >= 400){
            delay(2000)
            viewModel.errorCode = 200
        }
    }

    TabletSignUpScreen(
        navController = navController,
        viewModel = viewModel,
        horizontalPadding = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 350 else 100,
        detectTapGestures = { keyboardController?.hide() },
        onBackPressed = { navController.popBackStack() },
        onSubmitEmail = {
            keyboardController?.hide()
            viewModel.getUserEmailCheck(viewModel.userEmail, navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TabletSignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel,
    horizontalPadding: Int,
    detectTapGestures: () -> Unit,
    onBackPressed: () -> Unit,
    onSubmitEmail: () -> Unit
) {
    LinkedOutTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                    title = {},
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "arrow back",
                            modifier = Modifier
                                .clickable { onBackPressed() }
                                .padding(start = 10.dp)
                                .size(30.dp),
                            tint = Color.White
                        )

                    }
                )
            },
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    detectTapGestures()
                })
            },
            content = {
                if (viewModel.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }

                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .padding(horizontal = horizontalPadding.dp)
                    ) {
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            text = "이메일로 가입하기",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.White
                        )
                        Text(
                            text = "회원 서비스 이용을 위해 회원가입을 해주세요.",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                            color = Color(0xFF919191)
                        )

                        LoginTextFields(navController = navController)

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "*비밀번호는 영문(대소문자), 특수문자, 숫자 포함 8~12자를 조합해 주세요.",
                            fontSize = 10.5.sp,
                            color = LinkedInColor,
                            modifier = Modifier.padding(horizontal = 30.dp)
                        )

                        Button(
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LinkedInColor,
                                disabledContainerColor = Color(0xFF868686)
                            ),
                            enabled = !viewModel.userEmailError && viewModel.userPw.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(start = 20.dp, end = 20.dp, top = 50.dp),
                            onClick = {
                                onSubmitEmail()
                            }
                        ) {
                            Text(text = "인증 메일 보내기", color = Color.Black)
                        }
                    }
                }

                AnimatedVisibility(
                    visible = viewModel.isSignUpApiFinished,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearEasing
                        )
                    )
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable(enabled = false) { }) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
                        ) {
                            SendSignUpFinishedAlert(
                                { viewModel.isSignUpApiFinished = false },
                                "이메일 주소로 인증 메일이 발송됐습니다.",
                                "링크를 클릭해 회원가입을 완료해주세요 !!"
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = viewModel.isSignUpApiFinished,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearEasing
                        )
                    )
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable(enabled = false) { }) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 20.dp)
                                .navigationBarsPadding(), contentAlignment = Alignment.BottomCenter
                        ) {
                            SendSignUpFinishedAlert(
                                { viewModel.isSignUpApiFinished = false },
                                "에러가 발생했습니다.",
                                "사용중인 이메일이거나 비밀번호 조합 오류입니다."
                            )
                        }
                    }
                }
            }
        )
    }
}