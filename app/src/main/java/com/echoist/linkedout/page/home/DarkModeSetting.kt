package com.echoist.linkedout.page.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun DarkModeSettingPage(navController: NavController){

    val (selectedMode, setSelectedMode) = remember { mutableStateOf<String?>(null) }

    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("화면",navController)
            },
            content = {
                Column(Modifier.padding(it)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp), contentAlignment = Alignment.Center){

                        ModeItem(
                            modeText = "라이트 모드",
                            modeImg = R.drawable.mode_light, // 라이트 모드 아이콘 리소스
                            isSelected = selectedMode == "라이트 모드",
                            onItemSelected = { setSelectedMode("라이트 모드") },
                            modifier = Modifier.padding(end = 140.dp)
                        )
                        ModeItem(
                            modeText = "다크 모드",
                            modeImg = R.drawable.mode_dark, // 다크 모드 아이콘 리소스
                            isSelected = selectedMode == "다크 모드",
                            onItemSelected = { setSelectedMode("다크 모드") }, //todo setSeletedMode 만들것 기능. roomdb 활용
                            modifier = Modifier.padding(start = 140.dp)

                        )

                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModeItem(
    modeText: String,
    modeImg: Int,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) LinkedInColor else Color(0xFF252525)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onItemSelected() }
            .padding(16.dp)
    ) {
        GlideImage(model = modeImg, contentDescription = "modeImg")
        Spacer(modifier = Modifier.height(22.dp))
        Icon(
            imageVector = Icons.Default.CheckCircle,
            tint = color,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = modeText, fontSize = 16.sp)
    }
}
