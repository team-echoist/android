package com.echoist.linkedout.presentation.login.signup

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletSignUpRoute(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val passwordFocusRequester = remember { FocusRequester() }

    val isPortrait =
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(key1 = viewModel.isSignUpApiFinished) {
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

    TabletSignUpScreen(
        modifier = Modifier.padding(
            top = if (isPortrait) 150.dp else 0.dp,
            start = if (isPortrait) 150.dp else 350.dp,
            end = if (isPortrait) 150.dp else 350.dp
        ),
        scaffoldState = scaffoldState,
        screenHeightDp = screenHeightDp,
        passwordFocusRequester = passwordFocusRequester,
        viewModel = viewModel,
        detectTapGestures = { keyboardController?.hide() },
        onBackPressed = { navController.popBackStack() },
        onSubmitEmail = {
            keyboardController?.hide()
            viewModel.getUserEmailCheck(viewModel.userEmail)
        },
        onInputComplete = {
            keyboardController?.hide()
            viewModel.requestRegister(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TabletSignUpScreen(
    modifier: Modifier,
    scaffoldState: BottomSheetScaffoldState,
    screenHeightDp: Int,
    passwordFocusRequester: FocusRequester,
    viewModel: SignUpViewModel,
    detectTapGestures: () -> Unit,
    onBackPressed: () -> Unit,
    onSubmitEmail: () -> Unit,
    onInputComplete: (String) -> Unit
) {
    BottomSheetScaffold(
        sheetContainerColor = Color(0xFF191919),
        scaffoldState = scaffoldState,
        sheetContent = {
            SignUpBottomSheetContent(
                viewModel = viewModel,
                scaffoldState = scaffoldState,
                onInputComplete = onInputComplete
            )
        },
        sheetPeekHeight = (0.8 * screenHeightDp).dp
    ) {
        SignUpContent(
            modifier = modifier,
            detectTapGestures = detectTapGestures,
            onBackPressed = onBackPressed,
            viewModel = viewModel,
            onSubmitEmail = onSubmitEmail,
            passwordFocusRequester = passwordFocusRequester
        )
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
                    .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
            ) {
                SendSignUpFinishedAlert(
                    { viewModel.isSignUpApiFinished = false },
                    "이메일 주소로 인증 메일이 발송됐습니다.",
                    "링크를 클릭해 회원가입을 완료해주세요 !!"
                )
            }
        }
    }
    SignUpFinishedAlert(viewModel)
}

@Composable
private fun SignUpContent(
    modifier: Modifier,
    detectTapGestures: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: SignUpViewModel,
    onSubmitEmail: () -> Unit,
    passwordFocusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .pointerInput(Unit) { detectTapGestures { detectTapGestures() } }
    ) {
        TabletDrawableTopBar(title = "", isBack = true, onBackPressed)
        Box(modifier) {
            Image(
                painter = painterResource(id = R.drawable.background_logo_340),
                contentDescription = "background",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.TopEnd)
            )
            Column {
                Spacer(modifier = Modifier.height(20.dp))
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
                PwTextField(
                    viewModel,
                    passwordFocusRequester
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "*비밀번호는 영문(대소문자), 특수문자, 숫자 포함 8~12자를 조합해 주세요.",
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
                    enabled = !viewModel.userEmailError && viewModel.userPw.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(start = 20.dp, end = 20.dp, top = 50.dp),
                    onClick = {
                        onSubmitEmail()
                    }
                ) {
                    Text(text = "인증 메일 보내기", color = Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpBottomSheetContent(
    viewModel: SignUpViewModel,
    scaffoldState: BottomSheetScaffoldState,
    onInputComplete: (String) -> Unit
) {
    Box {
        Authentication_6_BottomModal(
            { viewModel.getUserEmailCheck(viewModel.userEmail) }, //재요청 인증
            isError = viewModel.errorCode >= 400,
            isTypedLastNumber = { list ->
                onInputComplete(list.joinToString(""))
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
}

@Composable
private fun SignUpFinishedAlert(viewModel: SignUpViewModel) {
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
                    .padding(bottom = 20.dp)
                    .navigationBarsPadding(), contentAlignment = Alignment.BottomCenter
            ) {
                SendSignUpFinishedAlert(
                    { viewModel.isSignUpApiFinished = false },
                    "에러가 발생했습니다.",
                    "사용중인 이메일이거나 비밀번호 조합 오류입니다."
                )
            }
        }
    }
}