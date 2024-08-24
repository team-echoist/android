package com.echoist.linkedout.presentation

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.echoist.linkedout.page.login.Authentication_6_BottomModal
import com.echoist.linkedout.page.login.EmailTextField
import com.echoist.linkedout.page.login.PwTextField
import com.echoist.linkedout.page.login.SendSignUpFinishedAlert
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.SignUpViewModel
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
        navController = navController,
        scaffoldState = scaffoldState,
        screenHeightDp = screenHeightDp,
        passwordFocusRequester = passwordFocusRequester,
        viewModel = viewModel,
        horizontalPadding = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 350 else 100,
        detectTapGestures = { keyboardController?.hide() },
        onBackPressed = { navController.popBackStack() },
        onSubmitEmail = {
            keyboardController?.hide()
            viewModel.getUserEmailCheck(viewModel.userEmail, navController)
        },
        onInputComplete = {
            keyboardController?.hide()
            viewModel.requestRegister(it, navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TabletSignUpScreen(
    navController: NavController,
    scaffoldState: BottomSheetScaffoldState,
    screenHeightDp: Int,
    passwordFocusRequester: FocusRequester,
    viewModel: SignUpViewModel,
    horizontalPadding: Int,
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
                navController = navController,
                onInputComplete = onInputComplete
            )
        },
        sheetPeekHeight = (0.8 * screenHeightDp).dp
    ) {
        SignUpContent(
            horizontalPadding = horizontalPadding,
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
    horizontalPadding: Int,
    detectTapGestures: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: SignUpViewModel,
    onSubmitEmail: () -> Unit,
    passwordFocusRequester: FocusRequester
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { detectTapGestures() } }
            .padding(horizontal = horizontalPadding.dp)
    ) {
        Column {
            TabletDrawableTopBar(title = "회원가입", isBack = true, onBackPressed)
            Spacer(modifier = Modifier.height(50.dp))
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

@Composable
private fun SignUpBottomSheetContent(
    viewModel: SignUpViewModel,
    navController: NavController,
    onInputComplete: (String) -> Unit
) {
    Box {
        Authentication_6_BottomModal(
            { viewModel.getUserEmailCheck(viewModel.userEmail, navController) },
            isError = viewModel.errorCode >= 400,
            isTypedLastNumber = { list -> onInputComplete(list.joinToString("")) }
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