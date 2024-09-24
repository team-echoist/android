package com.echoist.linkedout.presentation.home.drawable.legal_Notice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.userInfo.account.SettingTopAppBar

@Composable
fun FontCopyRight(navController : NavController){
    
        Scaffold(topBar = {
            SettingTopAppBar("글꼴 저작권",navController)
        }) {

            Column(
                Modifier
                    .padding(it)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())) {
                Text(
                    text = "프리텐다드(Pretendard) 라이선스",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Copyright (c) 2021, Kil Hyung-jin (https://github.com/orioncactus/pretendard),\nwith Reserved Font Name 'Pretendard'.\n\nhttps://cactus.tistory.com/306",
                    style =
                    TextStyle(
                        fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                )
                )

            }
        }
    }
