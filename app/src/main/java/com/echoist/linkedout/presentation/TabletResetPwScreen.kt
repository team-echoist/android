package com.echoist.linkedout.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.echoist.linkedout.page.settings.CustomOutlinedTextField
import com.echoist.linkedout.page.settings.SendEmailFinishedAlert
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel
import kotlinx.coroutines.delay

@Composable
fun TabletResetPwRoute(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val isSendEmailVerifyApiFinished = viewModel.isSendEmailVerifyApiFinished
    val isLoading = viewModel.isLoading
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = isSendEmailVerifyApiFinished) {
        if (isSendEmailVerifyApiFinished) {
            delay(2000)
            viewModel.isSendEmailVerifyApiFinished = false
        }
    }
    TabletResetPwScreen(
        scrollState = scrollState,
        isLoading = isLoading,
        isSendEmailVerifyApiFinished = isSendEmailVerifyApiFinished,
        onEmailSubmit = { email ->
            viewModel.requestChangePw(email)
        },
        isEmailValid = { email ->
            viewModel.isEmailValid(email)
        },
        onAlertDismiss = {
            viewModel.isSendEmailVerifyApiFinished = false
        },
        onBackPress = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TabletResetPwScreen(
    scrollState: ScrollState,
    isLoading: Boolean,
    isSendEmailVerifyApiFinished: Boolean,
    onEmailSubmit: (String) -> Unit,
    isEmailValid: (String) -> Boolean,
    onAlertDismiss: () -> Unit,
    onBackPress: () -> Unit
) {
    LinkedOutTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                    title = {
                        Text(
                            text = "비밀번호 재설정",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "arrow back",
                            modifier = Modifier
                                .clickable { onBackPress() }
                                .padding(start = 10.dp)
                                .size(30.dp),
                            tint = Color.White
                        )

                    }
                )
            },
            content = {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }
                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .padding(it)
                        .padding(horizontal = 350.dp)
                ) {
                    Spacer(modifier = Modifier.height(42.dp))
                    Text(
                        text = "가입 시 사용한 이메일 주소를 입력해주세요. \n" +
                                "비밀번호를 다시 설정할 수 있는 링크를 보내드릴게요.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    var email by remember { mutableStateOf("") }
                    var isError by remember { mutableStateOf(false) }

                    CustomOutlinedTextField(
                        email,
                        { newText ->
                            email = newText
                            isError = !isEmailValid(email)
                        },
                        isError = isError,
                        hint = "이메일"
                    )

                    if (isError) {
                        Text(
                            text = "*이메일 주소를 정확하게 입력해주세요.",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    val enabled = !(isError || email.isEmpty())
                    val focusManager = LocalFocusManager.current

                    Button(
                        onClick = {
                            onEmailSubmit(email)
                            focusManager.clearFocus()
                        },
                        enabled = enabled,
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(61.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LinkedInColor,
                            disabledContainerColor = Color(0xFF868686)
                        )
                    ) {
                        Text(text = "이메일 보내기", color = Color.Black)
                    }
                }

                AnimatedVisibility(
                    visible = isSendEmailVerifyApiFinished,
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .background(Color.Black.copy(0.7f))
                            .clickable(enabled = false) {},
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        SendEmailFinishedAlert { onAlertDismiss() }
                    }
                }
            }
        )
    }
}
