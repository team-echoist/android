package com.echoist.linkedout.presentation

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.page.home.EssayNotificationBox
import com.echoist.linkedout.page.home.NotificationTimePickerBox
import com.echoist.linkedout.page.home.WritingNotificationBox
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel

@Composable
fun TabletSettingRoute(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    onCloseClick: () -> Unit
) {

    viewModel.readUserNotification()
    val context = LocalContext.current

    val savedTimeSelection = remember { SharedPreferencesUtil.getTimeSelection(context) }
    val hour by remember { mutableStateOf(SharedPreferencesUtil.getHourString(savedTimeSelection.hourIndex)) }
    val min by remember { mutableStateOf(SharedPreferencesUtil.getMinuteString(savedTimeSelection.minuteIndex)) }
    val period by remember { mutableStateOf(SharedPreferencesUtil.getPeriodString(savedTimeSelection.periodIndex)) }

    var writingRemindNotification by remember {
        mutableStateOf(
            SharedPreferencesUtil.getWritingRemindNotification(
                context
            )
        )
    }

    LinkedOutTheme {
        Scaffold(
            topBar = {
                TabletDrawableTopBar(title = "환경 설정") {
                    onCloseClick()
                }
            },
            content = {
                var isClickedTimeSelection by remember { mutableStateOf(false) }
                if (viewModel.isApifinished) {
                    Column(
                        Modifier
                            .padding(it)
                            .navigationBarsPadding()
                            .padding(bottom = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "글 조회 알림",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        EssayNotificationBox(
                            "글 ",
                            "조회 알림",
                            viewModel.viewedNotification
                        ) { it ->
                            viewModel.viewedNotification = it
                            Log.d(ContentValues.TAG, "NotificationPage: $it")
                        }
                        EssayNotificationBox(
                            "신고 결과",
                            "알림",
                            viewModel.reportNotification
                        ) { it ->
                            viewModel.reportNotification = it
                            Log.d(ContentValues.TAG, "NotificationPage: $it")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "글쓰기 알림",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        WritingNotificationBox(
                            writingRemindNotification,
                            { it ->
                                writingRemindNotification = it
                                Log.d(ContentValues.TAG, "NotificationPage: $it")
                            },
                            { isClickedTimeSelection = true },
                            hour, min, period
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "그 외 알림",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        EssayNotificationBox(
                            "이벤트 혜택 ",
                            "정보 알림",
                            viewModel.marketingNotification
                        ) { it ->
                            viewModel.marketingNotification = it
                            Log.d(ContentValues.TAG, "NotificationPage: $it")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "위치 서비스 설정",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        EssayNotificationBox(
                            "위치 기반 서비스 ",
                            "동의",
                            viewModel.locationNotification
                        ) { it ->
                            viewModel.locationNotification = it
                            Log.d(ContentValues.TAG, "NotificationPage: $it")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                AnimatedVisibility(
                    visible = isClickedTimeSelection,
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
                    NotificationTimePickerBox({ isClickedTimeSelection = false }, navController)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 60.dp), contentAlignment = Alignment.BottomCenter
                ) {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .height(61.dp), shape = RoundedCornerShape(20),
                        onClick = {
                            viewModel.updateUserNotification(
                                navController,
                                viewModel.locationNotification
                            )
                            SharedPreferencesUtil.saveWritingRemindNotification(
                                context,
                                writingRemindNotification
                            ) //글쓰기 시간 알림 설정 저장
                            if (writingRemindNotification) {
                                //homeViewModel.setAlarmAfter10(context) //테스트용 1초후알람
                                viewModel.setAlarmFromTimeString(
                                    context = context,
                                    hour,
                                    min,
                                    period
                                ) //정해진 시간에 알람설정.
                            } else {
                                viewModel.cancelAlarm(context) //알람 취소
                            }
                        }) {
                        Text(text = "저장", color = Color.Black)
                    }
                }
            }
        )
    }
}