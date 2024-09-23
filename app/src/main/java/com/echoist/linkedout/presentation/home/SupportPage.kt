package com.echoist.linkedout.presentation.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.userInfo.ModifyBox
import com.echoist.linkedout.presentation.userInfo.SettingTopAppBar

@Composable
fun SupportPage(navController: NavController) {

    Scaffold(topBar = {
        SettingTopAppBar("고객지원", navController)
    }) {

        Column(Modifier.padding(it)) {
            ModifyBox("링크드아웃 고객센터") { navController.navigate("LinkedOutSupportPage") }
            ModifyBox("공지사항") { navController.navigate(Routes.NoticePage) }
            Legal_Notice(navController = navController)

        }
    }
}

@Composable
fun Legal_Notice(navController: NavController) {
    var isClicked by remember { mutableStateOf(false) }

    Column {
        Box(modifier = Modifier
            .background(Color(0xFF000000))
            .padding(start = 20.dp)
            .fillMaxWidth()
            .clickable { isClicked = !isClicked }
            .height(70.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Text(text = "법적 고지", color = Color.White, fontSize = 16.sp)
            }
            val iconImg = if (isClicked) R.drawable.arrowup else R.drawable.arrowdown
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = iconImg),
                    contentDescription = "arrowforward",
                    tint = Color.White
                )

            }
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = isClicked,
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
            Column {
                Legal_NoticeMenu("이용약관") { navController.navigate("TermsAndConditionsPage") }
                Legal_NoticeMenu("위치 기반 서비스 이용 약관") { navController.navigate("LocationPolicyPage") }
                Legal_NoticeMenu("개인정보처리방침") { navController.navigate("PrivacyPolicyPage") }
                Legal_NoticeMenu("글꼴 저작권") { navController.navigate("FontCopyRight") }
            }
        }
    }
}

@Composable
fun Legal_NoticeMenu(text: String, isOptionClicked: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .height(64.dp)
        .padding(horizontal = 20.dp)
        .clickable { isOptionClicked() }) {
        Text(text = "•   ", color = Color.White, fontSize = 16.sp)
        Text(
            text = text,
            color = Color.White,
            textDecoration = TextDecoration.Underline,
            fontSize = 16.sp
        )

    }
}