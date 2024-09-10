package com.echoist.linkedout.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.page.home.LogoutBox
import com.echoist.linkedout.page.settings.EmailBox
import com.echoist.linkedout.page.settings.ModifyBox
import com.echoist.linkedout.viewModels.MyPageViewModel

@Composable
fun TabletAccountRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onClickChangeEmail: () -> Unit,
    onClickChangePassword: () -> Unit,
    onClickDeleteAccount: () -> Unit,
    isLogoutClicked: () -> Unit
) {
    val context = LocalContext.current
    var isLogoutClicked by remember { mutableStateOf(false) }
    Box(
        Modifier
            .padding(contentPadding)
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.6f)
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Text(
                text = "로그인 정보",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            EmailBox(
                { onClickChangeEmail() },
                viewModel.getMyInfo().email ?: "noEmail"
            )
            ModifyBox("비밀번호 변경") { onClickChangePassword() }
            ModifyBox("로그아웃") {
                isLogoutClicked = true
            }
            Spacer(modifier = Modifier.height(20.dp))
            ModifyBox("탈퇴하기") { onClickDeleteAccount() }
        }
        AnimatedVisibility(
            visible = isLogoutClicked,
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
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 10.dp)
                    .background(Color.Black.copy(0.7f)),
                contentAlignment = Alignment.BottomCenter
            ) {

                LogoutBox(
                    isCancelClicked = { isLogoutClicked = false },
                    isLogoutClicked = {
                        isLogoutClicked = false
                        SharedPreferencesUtil.saveClickedAutoLogin(context, false)

                        isLogoutClicked()
                    }
                )
            }
        }
    }
}