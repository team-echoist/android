package com.echoist.linkedout.presentation.userInfo.account.changepassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.login.signup.SendSignUpFinishedAlert
import com.echoist.linkedout.presentation.userInfo.account.SettingTopAppBar
import com.echoist.linkedout.presentation.userInfo.account.changeemail.CustomOutlinedTextField
import com.echoist.linkedout.presentation.userInfo.account.changeemail.SendEmailFinishedAlert
import com.echoist.linkedout.presentation.util.isEmailValid
import com.echoist.linkedout.presentation.util.throttleClick
import com.echoist.linkedout.ui.theme.LinkedInColor
import kotlinx.coroutines.delay

@Composable
fun ResetPwPageWithEmail(
    navController: NavController,
    viewModel: ResetPwViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp // 화면의 높이를 DP 단위로 가져옴

    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) } //todo 에러처리 할 구문 생각해야할것.

    val errorText = when (viewModel.errorCode) {
        500 -> "서버 에러입니다."
        400 -> "잘못된 이메일 주소입니다"
        404 -> "잘못된 요청입니다."
        else -> "잘못된 요청입니다."
    }

    LaunchedEffect(viewModel.isSendEmailVerifyApiFinished) {
        if (viewModel.isSendEmailVerifyApiFinished) {
            delay(2000)
            viewModel.isSendEmailVerifyApiFinished = false
        }
    }

    Scaffold(
        topBar = {
            SettingTopAppBar("비밀번호 재설정", navController)
        },
        content = {
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LinkedInColor)
                }
            }
            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(it)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(42.dp))
                Text(
                    text = "가입 시 사용한 이메일 주소를 입력해주세요. \n" +
                            "비밀번호를 다시 설정할 수 있는 링크를 보내드릴게요.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                CustomOutlinedTextField(
                    email,
                    { newText ->
                        email = newText
                        isError = !email.isEmailValid()
                    },

                    isError = isError,
                    hint = "이메일",
                    singLine = true
                )
                if (isError) {
                    Text(
                        text = "*이메일 주소를 정확하게 입력해주세요.",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))

                val enabled = !isError && email.isNotEmpty() && !viewModel.isLoading
                val focusManager = LocalFocusManager.current

                Text(
                    text = "이메일 보내기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(61.dp)
                        .background(
                            color = if (enabled) LinkedInColor else Color(0xFF868686),
                            shape = RoundedCornerShape(20)
                        )
                        .throttleClick {
                            if (enabled) {
                                viewModel.requestChangePw(email)
                                focusManager.clearFocus()
                            }
                        }
                        .padding(vertical = 16.dp),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(
                visible = viewModel.isSendEmailVerifyApiFinished,
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
                    .navigationBarsPadding()
                    .background(Color.Black.copy(0.7f))
                    .clickable(enabled = false) { }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = (0.8 * screenHeightDp).dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        SendEmailFinishedAlert(
                            "이메일 주소로 임시 비밀번호가 발송되었습니다.",
                            "임시 비밀번호로 로그인 후 비밀번호를 변경해주세요."
                        ) {
                            viewModel.isSendEmailVerifyApiFinished = false
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = viewModel.errorCode >= 400,
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
                            .padding(bottom = (0.8 * screenHeightDp).dp)
                            .navigationBarsPadding(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        SendSignUpFinishedAlert(
                            { viewModel.errorCode = 200 },
                            errorText,
                            viewModel.errorCode.toString()
                        )

                    }
                }
            }
        }
    )
}
