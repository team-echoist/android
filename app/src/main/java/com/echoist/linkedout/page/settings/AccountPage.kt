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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.page.home.LogoutBox
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun AccountPage(navController: NavController) {
    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("계정 관리",navController)
            },
            content = {
                var isLogoutClicked by remember {
                    mutableStateOf(false)
                }

                Column(
                    Modifier
                        .padding(it)
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(42.dp))
                    Text(text = "로그인 정보", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    EmailBox { navController.navigate("ChangeEmailPage") }
                    ModifyBox("비밀번호 변경") {navController.navigate("ChangePwPage")}



                    ModifyBox("로그아웃") {isLogoutClicked = true}
                    Spacer(modifier = Modifier.height(20.dp))
                    ModifyBox("탈퇴하기") {}

                }

                AnimatedVisibility(
                    visible = isLogoutClicked,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f)),
                    contentAlignment = Alignment.BottomCenter
                ){

                        LogoutBox(
                            isCancelClicked = { isLogoutClicked = false },
                            isLogoutClicked = { isLogoutClicked = false }
                        ) //todo 수정필요
                    }
                }



            }
        )
    }
}

@Composable
fun EmailBox(onClick: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .height(70.dp)){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "이메일 주소 변경")
                Spacer(modifier = Modifier.width(15.dp))
                Text(text = "kkhyungyung0@naver.com", fontSize = 12.sp, color = Color(0xFF5D5D5D))

            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "arrowforward", modifier = Modifier.size(20.dp))

        }
    }
}
@Composable
fun ModifyBox(text : String,onClick : ()-> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick()}
        .height(70.dp)){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
            Text(text = text)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "arrowforward", modifier = Modifier.size(20.dp))

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopAppBar(text: String,navController: NavController){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(30.dp)
                    .padding(start = 10.dp),
                tint = Color.White
            )

        }
    )
}