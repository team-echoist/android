package com.echoist.linkedout.presentation.userInfo.account.changeemail

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.login.signup.Authentication_6_BottomModal
import com.echoist.linkedout.presentation.userInfo.account.UserInfoViewModel
import com.echoist.linkedout.presentation.util.isEmailValid
import com.echoist.linkedout.ui.theme.LinkedInColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun TabletChangeEmailScreen(
    viewModel: ChangeEmailViewModel = hiltViewModel(),
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    moveToLoginPage: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp // 화면의 높이를 DP 단위로 가져옴
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val navigateToComplete by viewModel.navigateToComplete.collectAsState()

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            append("새 이메일 주소를 올바르게 입력한 후 ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = LinkedInColor
                )
            ) {
                append("아래의 '인증하기' 버튼")
            }
            append("을 눌러 해당 이메일로 인증을 완료해 주세요.")
        }.toAnnotatedString()
    }

    LaunchedEffect(navigateToComplete) {
        if (navigateToComplete) {
            bottomSheetState.hide()
            showToast = true
            delay(2000)
            userInfoViewModel.logout()
            moveToLoginPage()
            showToast = false
        }
    }

    //이메일 보냄 api 가 끝나면 2초 후 사라지게
    LaunchedEffect(key1 = viewModel.isSendEmailVerifyApiFinished) {
        if (viewModel.isSendEmailVerifyApiFinished) {
            bottomSheetState.show()
            delay(3000)
            viewModel.isSendEmailVerifyApiFinished = false
        }
    }

    BottomSheetScaffold(
        sheetContainerColor = Color(0xFF191919),
        scaffoldState = scaffoldState,
        sheetContent = {
            Box {
                Authentication_6_BottomModal(
                    reAuthentication = {
                        viewModel.sendEmailVerificationForChange(email)
                    }, //재요청 인증
                    isError = false,
                    isTypedLastNumber = { list ->
                        viewModel.postAuthChangeEmail(list.joinToString(""))
                        keyboardController?.hide()
                    }, scaffoldState
                )
                if (viewModel.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }
            }
        },
        sheetPeekHeight = (0.8 * screenHeightDp).dp
    ) {
        Box(
            Modifier
                .padding(contentPadding)
                .fillMaxSize(), contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.6f)
            ) {
                Spacer(modifier = Modifier.height(42.dp))
                Text(text = "현재 이메일 주소", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = viewModel.getMyInfo().email ?: "noEmail",
                    fontSize = 16.sp,
                    color = Color(0xFF5D5D5D)
                )
                Spacer(modifier = Modifier.height(32.dp))

                Text(text = "새 이메일 주소", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))

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

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = annotatedString, fontSize = 12.sp, color = Color(0xFF5D5D5D))

                Spacer(modifier = Modifier.height(27.dp))
                GlideImage(
                    model = R.drawable.box_change_email,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(106.dp)
                )
                Spacer(modifier = Modifier.height(209.dp))

                val enabled = !(isError || email.isEmpty())
                Button(
                    onClick = { viewModel.sendEmailVerificationForChange(email) },
                    enabled = enabled,
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(61.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkedInColor,
                        disabledContainerColor = Color(0xFF868686),
                    )
                ) {
                    Text(text = "인증하기")
                }
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
                    .background(Color.Black.copy(0.7f))
                    .clickable(enabled = false) { }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = (0.8 * screenHeightDp).dp)
                            .navigationBarsPadding(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        SendEmailFinishedAlert("새 이메일 주소로 인증메일이 발송됐습니다.") {
                            viewModel.isSendEmailVerifyApiFinished = false
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = navigateToComplete,
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
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .height(60.dp)
                            .padding(horizontal = 20.dp)
                            .background(
                                Color(0xFF616FED),
                                shape = RoundedCornerShape(10)
                            )
                    ) {
                        Text(
                            text = "이메일 주소가 변경되었습니다. \n 다시 로그인해주세요.",
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LinkedInColor)
                }
            }
        }
    }
}