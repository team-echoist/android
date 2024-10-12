package com.echoist.linkedout.presentation.userInfo.account

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.presentation.home.LogoutBox
import com.echoist.linkedout.presentation.userInfo.MyPageViewModel

@Composable
fun TabletAccountRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    onClickChangeEmail: () -> Unit,
    onClickChangePassword: () -> Unit,
    onClickDeleteAccount: () -> Unit,
    isLogoutClicked: () -> Unit
) {
    var isLogoutClicked by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isPortrait =
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(
        Modifier
            .padding(
                start = if (isPortrait) 20.dp else 190.dp,
                end = if (isPortrait) 20.dp else 190.dp
            )
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "로그인 정보",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF616FED),
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TabletEmailBox(
                { onClickChangeEmail() },
                viewModel.getMyInfo().email ?: "noEmail"
            )
            TabletModifyBox("비밀번호 변경") { onClickChangePassword() }
            TabletModifyBox("로그아웃") {
                isLogoutClicked = true
            }
            Spacer(modifier = Modifier.height(20.dp))
            TabletModifyBox("탈퇴하기") { onClickDeleteAccount() }
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
                        userInfoViewModel.logout()
                        isLogoutClicked()
                    }
                )
            }
        }
    }
}

@Composable
fun TabletEmailBox(onClick: () -> Unit, email: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF111111))
        .clickable { onClick() }
        .padding(horizontal = 20.dp)
        .height(70.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "이메일 주소 변경")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = Color(0xFF5D5D5D),
                    maxLines = 1,  // 한 줄로 제한
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "arrowforward"
                )
            }
        }
    }
}

@Composable
fun TabletModifyBox(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .background(Color(0xFF111111))
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 20.dp)
        .height(70.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(text = text, fontSize = 16.sp)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "arrowforward",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}