package com.echoist.linkedout.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Preview
@Composable
fun NotificationPage(navController: NavController){


    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("알림",navController)
            },
            content = {
                Column(Modifier.padding(it)) {

                    Text(
                        text = "글 조회 알림",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    var publishedNotification by remember { mutableStateOf(false) }
                    EssayNotificationBox("발행한 글","조회 알림",publishedNotification) { it -> publishedNotification = it}

                    var linkedOutNotification by remember { mutableStateOf(false) }
                    EssayNotificationBox("링크드아웃한 글","조회 알림",linkedOutNotification) { it -> linkedOutNotification = it}

                    var reportNotification by remember { mutableStateOf(false) }
                    EssayNotificationBox("신고 완료","알림",reportNotification) { it -> reportNotification = it}

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "글쓰기 알림",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    var writingNotification by remember { mutableStateOf(false) }
                    WritingNotificationBox(writingNotification){it -> writingNotification = it}
                }
            }
        )
    }
}

@Composable
fun EssayNotificationBox(
    coloredText: String,
    normalText: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF0E0E0E))
        .height(58.dp)
        .padding(horizontal = 20.dp)){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
            Row {
                Text(text = "$coloredText ", color = LinkedInColor)
                Text(text = normalText, color = Color.White)
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
            Switch(checked = isChecked, onCheckedChange = {onCheckChange(it)})

        }

    }
}

@Composable
fun WritingNotificationBox(
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF0E0E0E))
        .height(120.dp)
        .padding(horizontal = 20.dp)){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp), contentAlignment = Alignment.TopStart){
            Row {
                Text(text = "글쓰기 시간 알림 설정", color = Color.White)
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp), contentAlignment = Alignment.TopEnd){
            Switch(checked = isChecked, onCheckedChange = {onCheckChange(it)})

        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp), contentAlignment = Alignment.BottomEnd){
            Box(modifier = Modifier.size(90.dp,34.dp).background(Color(0xFF222222)), contentAlignment = Alignment.Center){
                Text(text = "11:00 PM", color = Color(0xFF979797))
            }

        }

    }
}