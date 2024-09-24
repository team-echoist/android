package com.echoist.linkedout.presentation.home.drawable.thememode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TabletThemeModeScreen(onCloseClick: () -> Unit) {
    LinkedOutTheme {
        Scaffold(
            topBar = {
                TabletDrawableTopBar(title = "화면") {
                    onCloseClick()
                }
            },
            content = {
                Column(Modifier.padding(it)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable(enabled = false) { }
                                    .padding(end = 15.dp) // 30dp의 절반인 15dp로 패딩을 줌
                            ) {
                                GlideImage(
                                    model = R.drawable.mode_light,
                                    contentDescription = "modeImg"
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "라이트 모드",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .size(60.dp, 30.dp)
                                        .background(
                                            Color(0xFF191919),
                                            shape = RoundedCornerShape(40)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "  준비중  ",
                                        fontWeight = FontWeight.SemiBold,
                                        color = LinkedInColor,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(30.dp)) // 두 이미지 사이의 간격

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable(enabled = false) { }
                                    .padding(start = 15.dp) // 30dp의 절반인 15dp로 패딩을 줌
                            ) {
                                GlideImage(
                                    model = R.drawable.mode_dark,
                                    contentDescription = "modeImg"
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "다크 모드",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    tint = LinkedInColor,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp) // 아이콘의 크기를 30dp로 설정
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
