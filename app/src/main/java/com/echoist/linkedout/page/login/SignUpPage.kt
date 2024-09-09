package com.echoist.linkedout.page.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.Routes
import com.echoist.linkedout.isEmailValid
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.SignUpViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    navController: NavController,
    viewModel: SignUpViewModel
) {

    val passwordFocusRequester = remember { FocusRequester() }

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp // 화면의 높이를 DP 단위로 가져옴

    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val navigateToComplete by viewModel.navigateToComplete.collectAsState()

    LaunchedEffect(navigateToComplete) {
        if (navigateToComplete) {
            navController.navigate(Routes.SignUpComplete)
            viewModel.onNavigated()
        }
    }

    LaunchedEffect(key1 = viewModel.isSignUpApiFinished) {
        //이메일 인증 클릭 성공시 모달 올라오게.
        if (viewModel.isSignUpApiFinished) {
            bottomSheetState.show()
            delay(3000)
            viewModel.isSignUpApiFinished = false
        }
    }

    LaunchedEffect(key1 = viewModel.errorCode) {
        if (viewModel.errorCode >= 400) {
            delay(2000)
            viewModel.errorCode = 200
        }
    }
    val errorText = when (viewModel.errorCode) {
        500 -> "서버 에러입니다."
        400 -> "잘못된 요청입니다."
        409 -> "중복된 이메일입니다."
        else -> "잘못된 요청입니다."
    }

    BottomSheetScaffold(
        sheetContainerColor = Color(0xFF191919),
        scaffoldState = scaffoldState,
        sheetContent = {
            Box {
                Authentication_6_BottomModal(
                    { viewModel.getUserEmailCheck(viewModel.userEmail) }, //재요청 인증
                    isError = viewModel.errorCode >= 400,
                    isTypedLastNumber = { list ->
                        keyboardController?.hide()
                        Log.d("6자리 코드 ", list.joinToString(""))
                        viewModel.requestRegister(list.joinToString(""))

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
        Scaffold(
            modifier = Modifier.pointerInput(Unit) { //배경 터치 시 키보드 숨김
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
            content = {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "arrowback",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(30.dp)
                                .clickable {
                                    navController.popBackStack()
                                    viewModel.userEmail = ""
                                    viewModel.userPw = ""
                                }
                        )
                        Spacer(modifier = Modifier.height(30.dp))
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
                        EmailTextField(viewModel, passwordFocusRequester)
                        PwTextField(viewModel, passwordFocusRequester)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "* 비밀번호는 영문(대소문자), 특수문자, 숫자 포함 8~30자를 조합해 주세요.",
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
                            enabled = !viewModel.userEmailError && viewModel.userPw.isNotEmpty() && !viewModel.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(start = 20.dp, end = 20.dp, top = 50.dp),
                            onClick = {
                                keyboardController?.hide()
                                viewModel.getUserEmailCheck(viewModel.userEmail)
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
                                .navigationBarsPadding()
                                .padding(bottom = (0.8 * screenHeightDp).dp),
                            contentAlignment = Alignment.BottomCenter
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
                if (viewModel.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }

            }

        )
    }
}


@Composable
fun EmailTextField(viewModel: SignUpViewModel, passwordFocusRequester: FocusRequester) {
    var errorText by remember { mutableStateOf("") }

    Column {
        TextField(
            value = viewModel.userEmail,
            isError = viewModel.userEmailError,
            onValueChange = { new ->
                viewModel.userEmail = new
                if (new.isNotEmpty() && viewModel.userEmail.isEmailValid()) {
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
                    color = Color(0xFF919191),
                    fontSize = 14.sp
                )
            }, // 힌트를 라벨로 설정합니다.
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequester.requestFocus()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF252525),
                unfocusedContainerColor = Color(0xFF252525),
                errorTextColor = Color.Red,
                errorContainerColor = Color(0xFF252525),
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = if (viewModel.userEmailError) 2.dp else 14.dp
                )
        )
        if (viewModel.userEmailError) {
            if (errorText.isNotEmpty())
                Text(
                    text = "* $errorText",
                    fontSize = 10.5.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 14.dp)
                )


        }
    }
}


@Composable
fun PwTextField(viewModel: SignUpViewModel, passwordFocusRequester: FocusRequester) {
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
            focusedContainerColor = Color(0xFF252525),
            unfocusedContainerColor = Color(0xFF252525),
            errorTextColor = Color.Red,
            errorContainerColor = Color(0xFF252525),
            errorIndicatorColor = Color.Transparent
        ),
        maxLines = 1, // 한 줄에만 입력할 수 있도록 설정

        keyboardActions = KeyboardActions(
            onDone = {
            }
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
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
            .padding(start = 16.dp, end = 16.dp)
            .focusRequester(passwordFocusRequester)
    )
}


@Composable
fun AgreementText(text: String, clickable: () -> Unit, color: Color) {

    Spacer(modifier = Modifier.height(18.dp))
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
fun SendSignUpFinishedAlert(isClicked: () -> Unit, text1: String, text2: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(81.dp)
            .padding(horizontal = 20.dp)
            .background(Color(0xFF212121), shape = RoundedCornerShape(20))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp), contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(text = text1, fontSize = 14.sp)
                Text(text = text2, color = LinkedInColor, fontSize = 14.sp)

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 15.dp), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                modifier = Modifier.clickable { isClicked() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Authentication_6_BottomModal(
    reAuthentication: () -> Unit,
    isError: Boolean,
    isTypedLastNumber: (List<String>) -> Unit,
    scaffoldState: BottomSheetScaffoldState
) {
    // MutableStateList로 6자리 입력 값을 저장
    val codeDigits = remember { mutableStateListOf("", "", "", "", "", "") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesters = List(6) { FocusRequester() }

    // 바텀시트가 다시 내려갔을 때 때 첫 번째 TextField에 포커스를 설정 && 텍스트필드값 초기화
    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden) {
            keyboardController!!.hide()
            focusRequesters[0].requestFocus()
            codeDigits.clear()
            repeat(6) { codeDigits.add("") }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .imePadding()
            .padding(bottom = 20.dp)
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(37.dp))
            Text(text = "인증번호 입력하기", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "이메일로 인증번호 6자리를 보내드렸어요.",
                fontSize = 16.sp, color = Color.White
            )
            Spacer(modifier = Modifier.height(41.dp))

            Row(Modifier.horizontalScroll(rememberScrollState())) {
                (0..5).forEach { index ->
                    Authentication_TextField(
                        text = codeDigits[index], // 해당 인덱스의 값을 전달
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() && index > 0) {
                                // 입력이 지워졌을 때 && 0번째 필드가 아니면 이전 TextField로 포커스 이동
                                focusRequesters[index - 1].requestFocus()
                            }
                            codeDigits[index] = newValue
                        },
                        focusRequester = focusRequesters[index],
                        onNextFocus = {
                            if (index < focusRequesters.size - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }
                            if (index == 5) { // 마지막 인증번호를 입력했을 경우 액션
                                isTypedLastNumber(codeDigits)
                            }
                        },
                        isError = isError
                    )
                    if (index < 5) {
                        Spacer(modifier = Modifier.width(7.dp))
                    }
                }
            }
            if (isError) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "*인증번호를 잘못 입력하셨습니다.",
                    color = Color(0xFFE43446),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(56.dp))
        }
        Row {
            Text(
                text = "인증번호를 못 받으셨나요?",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 20.4.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF6B6B6B)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "인증번호 재전송",
                modifier = Modifier.clickable { reAuthentication() },
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 20.4.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF616FED),
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
    }
}


@Composable
fun Authentication_TextField(
    text: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onNextFocus: () -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = text,
        onValueChange = { it ->
            if (it.length <= 1) {
                onValueChange(it) // 상위 컴포저블로 값 전달
                if (it.isNotEmpty()) {
                    onNextFocus()
                }
            }
        },
        isError = isError,
        modifier = Modifier
            .size(52.dp, 80.dp)
            .focusRequester(focusRequester),
        textStyle = TextStyle(fontSize = 32.sp, color = Color.White),
        shape = RoundedCornerShape(20),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFF252525),
            focusedContainerColor = Color(0xFF252525),
            errorBorderColor = Color(0xFFE43446),
            unfocusedBorderColor = if (text.isNotEmpty() && !isError) LinkedInColor else Color.Transparent,
            focusedBorderColor = LinkedInColor
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

