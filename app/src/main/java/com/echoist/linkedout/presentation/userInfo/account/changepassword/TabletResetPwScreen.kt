package com.echoist.linkedout.presentation.userInfo.account.changepassword

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.userInfo.account.changeemail.SendEmailFinishedAlert
import com.echoist.linkedout.presentation.util.isEmailValid
import com.echoist.linkedout.ui.theme.LinkedInColor
import kotlinx.coroutines.delay

@Composable
fun TabletResetPwRoute(
    navController: NavHostController,
    viewModel: ResetPwViewModel = hiltViewModel()
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
        horizontalPadding = 350,
        scrollState = scrollState,
        isLoading = isLoading,
        isSendEmailVerifyApiFinished = isSendEmailVerifyApiFinished,
        onEmailSubmit = { email -> viewModel.requestChangePw(email) },
        isEmailValid = { email -> email.isEmailValid() },
        onAlertDismiss = { viewModel.isSendEmailVerifyApiFinished = false },
        onBackPress = { navController.popBackStack() }
    )
}

@Composable
internal fun TabletResetPwScreen(
    horizontalPadding: Int,
    scrollState: ScrollState,
    isLoading: Boolean,
    isSendEmailVerifyApiFinished: Boolean,
    onEmailSubmit: (String) -> Unit,
    isEmailValid: (String) -> Boolean,
    onAlertDismiss: () -> Unit,
    onBackPress: () -> Unit
) {
    if (isLoading) {
        LoadingScreen()
    } else {
        Content(
            horizontalPadding = horizontalPadding,
            scrollState = scrollState,
            onEmailSubmit = onEmailSubmit,
            isEmailValid = isEmailValid,
            onBackPress = onBackPress
        )

        if (isSendEmailVerifyApiFinished) {
            EmailVerificationAlert(onAlertDismiss)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = LinkedInColor)
    }
}

@Composable
fun Content(
    horizontalPadding: Int,
    scrollState: ScrollState,
    onEmailSubmit: (String) -> Unit,
    isEmailValid: (String) -> Boolean,
    onBackPress: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding.dp)
    ) {
        TabletDrawableTopBar(title = "비밀번호 재설정", isBack = true) {
            onBackPress()
        }

        Spacer(modifier = Modifier.height(42.dp))

        InstructionText()

        Spacer(modifier = Modifier.height(12.dp))

        EmailInputField(
            email = email,
            isError = isError,
            onEmailChange = {
                email = it
                isError = !isEmailValid(email)
            }
        )

        if (isError) {
            ErrorText()
        }

        Spacer(modifier = Modifier.height(40.dp))

        SubmitButton(
            enabled = !(isError || email.isEmpty()),
            onClick = { onEmailSubmit(email) }
        )
    }
}

@Composable
fun InstructionText() {
    Text(
        text = "가입 시 사용한 이메일 주소를 입력해주세요. \n" +
                "비밀번호를 다시 설정할 수 있는 링크를 보내드릴게요.",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun EmailInputField(
    email: String,
    isError: Boolean,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        singleLine = true,
        isError = isError,
        placeholder = {
            Text(text = "이메일", fontSize = 16.sp, color = Color(0xFF5D5D5D))
        },
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email, // Corrected to Email
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onEmailChange(email) }),
        trailingIcon = {
            if (email.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "cancel",
                    modifier = Modifier.clickable { onEmailChange("") }
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color(0xFF5D5D5D),
            focusedTextColor = Color(0xFF5D5D5D),
            unfocusedBorderColor = Color(0xFF252525),
            focusedBorderColor = LinkedInColor,
            unfocusedContainerColor = Color(0xFF111111),
            focusedContainerColor = Color(0xFF111111),
            errorBorderColor = Color.Red,
            errorTextColor = Color(0xFF5D5D5D)
        )
    )
}

@Composable
fun ErrorText() {
    Text(
        text = "*이메일 주소를 정확하게 입력해주세요.",
        color = Color.Red,
        fontSize = 12.sp
    )
}

@Composable
fun SubmitButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Button(
        onClick = {
            onClick()
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

@Composable
fun EmailVerificationAlert(onAlertDismiss: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable(enabled = false) {},
            contentAlignment = Alignment.BottomCenter
        ) {
            SendEmailFinishedAlert("이메일 주소로 인증메일이 발송됐습니다.") {
                onAlertDismiss()
            }
        }
    }
}