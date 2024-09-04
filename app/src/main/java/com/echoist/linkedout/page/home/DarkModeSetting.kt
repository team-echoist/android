package com.echoist.linkedout.page.home

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DarkModeSettingPage(navController: NavController) {

    val context = LocalContext.current
    // Destructuring Declaration을 통해 두 값을 나누어 사용 selectedMode를 설정하는 함수로 사용가능
    val (selectedMode, setSelectedMode) = remember {
        mutableStateOf<String?>(
            SharedPreferencesUtil.getDisplayInfo(
                context
            )
        )
    }



    Scaffold(
        topBar = {
            SettingTopAppBar("화면", navController)
        },
        content = {
            Column(Modifier.padding(it)) {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp), contentAlignment = Alignment.Center
                ) {

//

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(enabled = false) { }
                            .padding(16.dp)
                            .padding(end = 140.dp)
                    ) {
                        GlideImage(model = R.drawable.mode_light, contentDescription = "modeImg")
                        Spacer(modifier = Modifier.height(22.dp))
                        Box(
                            modifier = Modifier
                                .size(60.dp, 24.dp)
                                .background(Color(0xFF191919), shape = RoundedCornerShape(40)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "  준비중  ",
                                fontWeight = FontWeight.SemiBold,
                                color = LinkedInColor,
                                fontSize = 12.sp
                            )

                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "라이트 모드", fontSize = 16.sp)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(enabled = false) { }
                            .padding(16.dp)
                            .padding(start = 140.dp)
                    ) {
                        GlideImage(model = R.drawable.mode_dark, contentDescription = "modeImg")
                        Spacer(modifier = Modifier.height(22.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            tint = LinkedInColor,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "다크 모드", fontSize = 16.sp)
                    }


                    //ModeItem(
//                            modeText = "라이트 모드",
//                            modeImg = R.drawable.mode_light,
//                            isSelected = selectedMode == LIGHT_MODE,
//                            onItemSelected = {
//                                setSelectedMode("라이트 모드")
//                                SharedPreferencesUtil.saveDisplayInfo(context, LIGHT_MODE) //선택 시 shared에 저장
//                                             },
//                            modifier = Modifier.padding(end = 140.dp)
//                        )

//                        ModeItem(
//                            modeText = "다크 모드",
//                            modeImg = R.drawable.mode_dark,
//                            isSelected = selectedMode == DARK_MODE,
//                            onItemSelected = {
//                                setSelectedMode("다크 모드")
//                                SharedPreferencesUtil.saveDisplayInfo(context, DARK_MODE)
//                                             },
//                            modifier = Modifier.padding(start = 140.dp)
//
//                        )

                }
            }
        }
    )
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


@Preview
@Composable
fun WebViewExa2mple() {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()  // 페이지 내에서 링크 클릭 시 WebView 안에서 열리도록 설정
            loadUrl("https://www.naver.com")
        }
    }, update = { webView ->
        webView.loadUrl("https://www.naver.com")
    })
}