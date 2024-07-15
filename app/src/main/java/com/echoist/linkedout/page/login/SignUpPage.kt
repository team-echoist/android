package com.echoist.linkedout.page.login

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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.R
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel
import kotlinx.coroutines.delay

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview

@Composable
fun PrevSignUpPage() {
    val viewModel : SignUpViewModel = viewModel()
    SignUpPage(navController = rememberNavController(), viewModel = viewModel)
}

@Composable
fun SignUpPage(
    navController: NavController,
    viewModel: SignUpViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = viewModel.isSignUpApiFinished) {
        if (viewModel.isSignUpApiFinished){

            delay(2000)
            viewModel.isSignUpApiFinished = false
            navController.navigate("LoginPage")
        }
    }

    LaunchedEffect(key1 = viewModel.isErr) {
        if (viewModel.isErr){

            delay(2000)
            viewModel.isErr = false
        }
    }

    LinkedOutTheme {
        Scaffold(
            modifier = Modifier.pointerInput(Unit) { //배경 터치 시 키보드 숨김
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
            content = {
                if (viewModel.isLoading){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrowback",
                        tint = if (isSystemInDarkTheme()) {
                            Color.White

                        } else Color.Gray,
                        modifier = Modifier

                            .padding(16.dp)
                            .clickable { navController.popBackStack() } //뒤로가기
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "이메일로 가입하기",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 16.dp),
                        color = if (isSystemInDarkTheme()) Color.White else Color.Gray
                    )
                    Text(
                        text = "회원 서비스 이용을 위해 회원가입을 해주세요.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                        color = if (isSystemInDarkTheme()) Color(0xFF919191) else Color.Gray
                    )
                    EmailTextField(viewModel)
                    PwTextField(viewModel)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "check",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(25.dp)
                                .clickable {
                                    viewModel.agreement_service =
                                        !viewModel.agreement_service
                                    viewModel.agreement_collection =
                                        !viewModel.agreement_collection
                                    viewModel.agreement_teen =
                                        !viewModel.agreement_teen
                                    viewModel.agreement_marketing =
                                        !viewModel.agreement_marketing
                                },
                            tint = if (viewModel.agreement_service &&
                                viewModel.agreement_collection &&
                                viewModel.agreement_teen &&
                                viewModel.agreement_marketing
                            )
                                Color.White
                            else Color(0xFF919191)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "전체 동의")
                    }
                    AgreementText(
                        text = "(필수)  서비스  이용약관  동의",
                        {
                            viewModel.agreement_service =
                                !viewModel.agreement_service
                        }, if (viewModel.agreement_service) Color.White else Color(0xFF919191)
                    )
                    AgreementText(
                        text = "(필수)  개인정보  수집  및  이용  동의",
                        {
                            viewModel.agreement_collection =
                                !viewModel.agreement_collection
                        }, if (viewModel.agreement_collection) Color.White else Color(0xFF919191)
                    )
                    AgreementText(
                        text = "(필수)   만  14세  이상입니다",
                        { viewModel.agreement_teen = !viewModel.agreement_teen },
                        if (viewModel.agreement_teen) Color.White else Color(0xFF919191)
                    )
                    AgreementText(
                        text = "(선택)   마케팅  정보  수신  동의",
                        {
                            viewModel.agreement_marketing =
                                !viewModel.agreement_marketing
                        }, if (viewModel.agreement_marketing) Color.White else Color(0xFF919191)
                    )

                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE4A89E),
                            disabledContainerColor = Color(0xFF868686)
                        ),
                        enabled = viewModel.agreement_service && viewModel.agreement_collection && viewModel.agreement_teen && !viewModel.userEmailError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(start = 20.dp, end = 20.dp, top = 43.dp),
                        onClick = {
                            viewModel.getUserEmailCheck(viewModel.userEmail,navController)
                        }
                    ) {
                        Text(text = "회원가입")
                    }

                }
                AnimatedVisibility(
                    visible = viewModel.isSignUpApiFinished ,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable(enabled = false) { }){
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter){
                            SendSignUpFinishedAlert({viewModel.isSignUpApiFinished = false},"이메일 주소로 인증 메일이 발송됐습니다.","링크를 클릭해 회원가입을 완료해주세요 !!")

                        }
                    }
                }

                AnimatedVisibility(
                    visible = viewModel.isErr,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable(enabled = false) { }){
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter){
                            SendSignUpFinishedAlert({viewModel.isErr = false},"에러가 발생했습니다.","사용중인 이메일이거나 뭔가오류임")

                        }
                    }
                }

            }

        )
    }

}

@Composable
fun EmailTextField(viewModel: SignUpViewModel) {
    var errorText by remember { mutableStateOf("") }

    Column {
        TextField(
            value = viewModel.userEmail,
            isError = viewModel.userEmailError,
            onValueChange = { new ->
                viewModel.userEmail = new
                if (new.isNotEmpty() && viewModel.isEmailValid(viewModel.userEmail)) {
                    viewModel.userEmailError = false
                    errorText = ""
                } else {
                    viewModel.userEmailError = true
                    errorText = "올바르지 않은 이메일 형식입니다."
                }
            },
            label = {
                Text(
                    "이메일 주소 또는 아이디",
                    color = if (isSystemInDarkTheme()) Color(0xFF919191) else Color.Gray,
                    fontSize = 14.sp
                )
            }, // 힌트를 라벨로 설정합니다.
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
                unfocusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
                errorTextColor = Color.Red,
                errorContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
        )
        if (viewModel.userEmailError) {
            if (errorText.isNotEmpty())
                Text(
                    text = errorText,
                    fontSize = 12.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp, bottom = 14.dp)
                )
        }
    }

}

@Composable
fun PwTextField(viewModel: SignUpViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = viewModel.userPw,
        onValueChange = { new ->
            if (new.length <= 20)
                viewModel.userPw = new
        },
        label = { Text("비밀번호", color = Color(0xFF919191), fontSize = 14.sp) }, // 힌트를 라벨로 설정합니다.
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
            unfocusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
            errorTextColor = Color.Red,
            errorContainerColor = if (isSystemInDarkTheme()) Color(0xFF252525) else Color.Black,
            errorIndicatorColor = Color.Transparent
        ),
        maxLines = 1, // 한 줄에만 입력할 수 있도록 설정

        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = { // 비밀번호 표시 여부입니다.
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = if (passwordVisible) R.drawable.pw_eye else R.drawable.pw_eye_off),
                    contentDescription = "pw_eye"
                )

            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 55.dp)
    )
}

@Composable
fun AgreementText(text: String, clickable: () -> Unit, color: Color) {


    Spacer(modifier = Modifier.height(10.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "check",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(25.dp)
                .clickable { clickable() },
            tint = color

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp, color = Color(0xFF919191))
    }
}

@Composable
fun SendSignUpFinishedAlert(isClicked : ()->Unit,text1 : String, text2 : String){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(81.dp)
        .padding(horizontal = 20.dp)
        .background(Color(0xFF212121), shape = RoundedCornerShape(20))){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp), contentAlignment = Alignment.CenterStart){
            Column {
                Text(text = text1, fontSize = 14.sp)
                Text(text = text2, color = LinkedInColor, fontSize = 14.sp)

            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp), contentAlignment = Alignment.CenterEnd){
            Icon(imageVector = Icons.Default.Close, contentDescription = "close", modifier = Modifier.clickable { isClicked() })
        }


    }
}