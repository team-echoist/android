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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChangeEmailPage(navController: NavController) {



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

    val viewModel: SignUpViewModel = hiltViewModel()
    val scrollState = rememberScrollState()

    //이메일 보냄 api 가 끝나면 2초 후 사라지게
    LaunchedEffect(key1 = viewModel.isSendEmailVerifyApiFinished) {
        if (viewModel.isSendEmailVerifyApiFinished){
            delay(2000)
            viewModel.isSendEmailVerifyApiFinished = false
        }
    }

    LinkedOutTheme {
        Scaffold(
            
            topBar = {
                SettingTopAppBar("이메일 주소 변경",navController)
            },
            content = {
                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .padding(it)
                        .padding(horizontal = 20.dp)
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

                    var email by remember { mutableStateOf("") }
                    var isError by remember { mutableStateOf(false) }


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
                        onClick = { viewModel.sendEmailVerificationForChange(email)},
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
                        androidx.compose.material.Text(text = "인증하기")
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

@Composable
fun CustomOutlinedTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint : String,
    isError : Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        shape = RoundedCornerShape(20),
        value = text,
        placeholder = { Text(text = hint, fontSize = 18.sp, color = Color(0xFF5D5D5D)) },
        onValueChange = onTextChange,
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color(0xFF5D5D5D),
            focusedTextColor = Color(0xFF5D5D5D),
            unfocusedBorderColor = Color(0xFF252525),
            focusedBorderColor = LinkedInColor,
            unfocusedContainerColor = Color(0xFF111111),
            focusedContainerColor = Color(0xFF111111),
            errorBorderColor = Color.Red,
            errorTextColor = Color(0xFF5D5D5D)

        ),
        trailingIcon = {
            if (text.isNotEmpty())
                Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "cancel",
                modifier = Modifier.clickable {
                    onTextChange("")
                }
            )

        })
}

@Composable
fun SendEmailFinishedAlert(isClicked : ()->Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(81.dp)
        .padding(horizontal = 20.dp)
        .background(Color(0xFF212121), shape = RoundedCornerShape(20))){
        Box(modifier = Modifier.fillMaxSize().padding(start = 15.dp), contentAlignment = Alignment.CenterStart){
            Column {
                Text(text = "새 이메일 주소로 인증메일이 발송됐습니다.", fontSize = 14.sp)
                Text(text = "링크를 클릭해 인증을 완료해주세요.", color = LinkedInColor, fontSize = 14.sp)

            }
        }
        Box(modifier = Modifier.fillMaxSize().padding(end = 15.dp), contentAlignment = Alignment.CenterEnd){
            Icon(imageVector = Icons.Default.Close, contentDescription = "close", modifier = Modifier.clickable { isClicked() })
        }


    }
}

