package com.echoist.linkedout.presentation.userInfo.account.changepassword

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.presentation.userInfo.MyPageViewModel
import com.echoist.linkedout.presentation.userInfo.account.changeemail.CustomOutlinedTextField
import com.echoist.linkedout.ui.theme.LinkedInColor

@Composable
fun TabletChangePasswordScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onClickResetPassword: () -> Unit,
    onChangePwFinished: () -> Unit
) {
    var oldPw by remember { mutableStateOf("") }
    var oldPwErr by remember { mutableStateOf(false) }
    var newPw by remember { mutableStateOf("") } //todo 이 값들을 페이지 나갔다 들어와도 유지되게끔 할것인지.
    var newPwErr by remember { mutableStateOf(false) } //todo 에러처리 할 구문 생각해야할것.

    val isChangePwFinished by viewModel.isChangePwFinished.collectAsState()

    LaunchedEffect(isChangePwFinished) {
        if (isChangePwFinished) {
            onChangePwFinished()
        }
    }

    Box(
        Modifier
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.6f)
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Text(text = "현재 비밀번호", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                oldPw,
                { newText ->
                    oldPw = newText
                },
                isError = oldPwErr,
                hint = "비밀번호",
                singLine = true
            )
            if (oldPwErr) {
                Text(text = "올바른 이메일 형식이 아닙니다.", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "비밀번호를 잊으셨나요? ", fontSize = 12.sp, color = Color(0xFF5D5D5D))
                Text(
                    text = "비밀번호 재설정",
                    fontSize = 12.sp,
                    color = LinkedInColor,
                    modifier = Modifier.clickable { onClickResetPassword() },
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "새 비밀번호", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                newPw,
                { newText ->
                    newPw = newText
                },
                isError = newPwErr,
                hint = "새 비밀번호",
                singLine = true
            )
            if (newPwErr) {
                Text(text = "올바른 이메일 형식이 아닙니다.", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "새 비밀번호 확인", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))

            var newPwCheck by remember { mutableStateOf("") }
            var newPwCheckErr by remember { mutableStateOf(false) } //todo 에러처리 할 구문 생각해야할것.

            CustomOutlinedTextField(
                newPwCheck, //이메일 체크 에러
                { newText ->
                    newPwCheck = newText
                    if (newPw != newPwCheck) newPwCheckErr = true
                },
                isError = newPwCheckErr,
                hint = "새 비밀번호 확인",
                singLine = true
            )
            if (newPwCheckErr) {
                Text(text = "비밀번호가 일치하지 않습니다.", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(187.dp))

            val enabled = newPw == newPwCheck && newPw.isNotBlank() //문자가 있어야함
            Button(
                onClick = { viewModel.updatePw(newPw) },
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
                Text(text = "변경하기", color = Color.Black)
            }
        }
    }
}