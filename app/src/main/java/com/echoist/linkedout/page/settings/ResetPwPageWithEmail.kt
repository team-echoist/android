package com.echoist.linkedout.page.settings

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel
import kotlinx.coroutines.delay

@Composable
fun ResetPwPageWithEmail(navController: NavController) {

    val scrollState = rememberScrollState()
    val viewModel: SignUpViewModel = hiltViewModel()

    LaunchedEffect(key1 = viewModel.isSendEmailVerifyApiFinished) {
        if (viewModel.isSendEmailVerifyApiFinished){
            delay(2000)
            viewModel.isSendEmailVerifyApiFinished = false
        }
    }

    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("비밀번호 재설정",navController)
            },
            content = {
                if (viewModel.isLoading){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
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
                    Text(text = "가입 시 사용한 이메일 주소를 입력해주세요. \n" +
                            "비밀번호를 다시 설정할 수 있는 링크를 보내드릴게요.", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(12.dp))

                    var email by remember { mutableStateOf("") }
                    var isError by remember { mutableStateOf(false) } //todo 에러처리 할 구문 생각해야할것.

                    CustomOutlinedTextField(
                        email,
                        { newText ->
                            email = newText
                            isError = !viewModel.isEmailValid(email)
                        },

                        isError = isError,
                        hint = "이메일"
                    )
                    if (isError) {
                        Text(text = "*이메일 주소를 정확하게 입력해주세요.", color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    val enabled = !(isError || email.isEmpty())
                    val focusManager = LocalFocusManager.current


                    Button(
                        //비번 변경 이메일요청
                        onClick = { viewModel.requestChangePw(email)
                                  focusManager.clearFocus()},
                        enabled =  enabled,
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(61.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LinkedInColor,
                            disabledContainerColor = Color(0xFF868686),

                            )
                    ) {
                        Text(text = "이메일 보내기", color = Color.Black)
                    }
                }
                AnimatedVisibility(
                    visible = viewModel.isSendEmailVerifyApiFinished,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable(enabled = false) {  }){
                        Box(modifier = Modifier.fillMaxSize().padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter){
                            SendEmailFinishedAlert{viewModel.isSendEmailVerifyApiFinished = false}

                        }
                    }
                }

            }
        )
    }
}