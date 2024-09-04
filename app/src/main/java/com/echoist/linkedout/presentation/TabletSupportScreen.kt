package com.echoist.linkedout.presentation

import android.annotation.SuppressLint
import android.webkit.WebView
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.echoist.linkedout.LOCATION_POLICY_URL
import com.echoist.linkedout.PRIVACY_POLICY_URL
import com.echoist.linkedout.TERMS_POLICY_URL
import com.echoist.linkedout.page.home.Legal_NoticeMenu
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TabletSupportRoute(
    onCloseClick: () -> Unit,
    onClickSupport: () -> Unit
) {
    LinkedOutTheme {
        Scaffold(topBar = {
            TabletDrawableTopBar(title = "고객지원") {
                onCloseClick()
            }
        }) {
            Column(Modifier.padding(it)) {
                SupportBox(title = "링크드아웃 고객센터") {
                    onClickSupport()
                }
                SupportBox(title = "공지사항") {

                }
                ExpandableBox(title = "이용약관")
            }
        }
    }
}

@Composable
fun SupportBox(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF111111))
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "arrowforward",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ExpandableBox(title: String) {
    var isClicked by remember { mutableStateOf(false) }
    val iconImg =
        if (isClicked) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight
    var showWebViewDialog by remember { mutableStateOf(false) }
    var urlToLoad by remember { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111111))
                .clickable(onClick = {
                    isClicked = !isClicked
                })
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Icon(
                iconImg,
                contentDescription = "arrowforward",
                modifier = Modifier.size(20.dp)
            )
        }
        AnimatedVisibility(
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111111))
            ) {
                Legal_NoticeMenu("이용약관") {
                    showWebViewDialog = true
                    urlToLoad = TERMS_POLICY_URL
                }
                Legal_NoticeMenu("위치 기반 서비스 이용 약관") {
                    showWebViewDialog = true
                    urlToLoad = LOCATION_POLICY_URL
                }
                Legal_NoticeMenu("개인정보처리방침") {
                    showWebViewDialog = true
                    urlToLoad = PRIVACY_POLICY_URL
                }
                Legal_NoticeMenu("글꼴 저작권") {

                }
            }
        }
    }
    if (showWebViewDialog) {
        AlertDialog(
            onDismissRequest = { showWebViewDialog = false },
            title = {
                Text(
                    text = "서비스 이용 약관",
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .background(Color.White)
                    ) {
                        WebViewComponent(url = urlToLoad)
                    }
                }
            },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showWebViewDialog = false }
                ) {
                    Text("확인")
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
fun WebViewComponent(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
    )
}