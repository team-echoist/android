package com.echoist.linkedout.page.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.SignUpViewModel

@Composable
fun ResetPwPage(navController: NavController, token: String = "empty_token") {

    val scrollState = rememberScrollState()
    val viewModel: SignUpViewModel = hiltViewModel()




    Scaffold(
        topBar = {
            SettingTopAppBar("비밀번호 변경", navController)
        },
        content = {

            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(it)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(42.dp))

                Text(text = "새 비밀번호", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))


                var newPw by remember { mutableStateOf("") } //todo 이 값들을 페이지 나갔다 들어와도 유지되게끔 할것인지.
                val newPwErr by remember { mutableStateOf(false) } //todo 에러처리 할 구문 생각해야할것.

                CustomOutlinedTextField(
                    newPw,
                    { newText ->
                        newPw = newText
                    },
                    isError = newPwErr,
                    hint = "새 비밀번호"
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
                    hint = "새 비밀번호 확인"
                )
                if (newPwCheckErr) {
                    Text(text = "비밀번호가 불일치합니다.", color = Color.Red, fontSize = 12.sp)
                }


                Spacer(modifier = Modifier.height(187.dp))
                val focusManager = LocalFocusManager.current


                val enabled = newPw == newPwCheck && newPw.isNotBlank() //문자가 있어야함
                Button(
                    onClick = {
                        viewModel.verifyChangePw(token, newPw)
                        focusManager.clearFocus()
                    },
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
    )
}
