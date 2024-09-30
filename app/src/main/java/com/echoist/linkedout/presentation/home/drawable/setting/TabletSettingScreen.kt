package com.echoist.linkedout.presentation.home.drawable.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.home.notification.NotificationViewModel

@Composable
fun TabletSettingRoute(
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onCloseClick: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        notificationViewModel.readUserNotification()
    }

    val context = LocalContext.current
    val hour by notificationViewModel.hour.collectAsState()
    val min by notificationViewModel.min.collectAsState()
    val period by notificationViewModel.period.collectAsState()
    val writingRemindNotification by notificationViewModel.writingRemindNotification.collectAsState()

    var isClickedTimeSelection by remember { mutableStateOf(false) }
    if (notificationViewModel.isApifinished) {
        Column(
            Modifier
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            TabletDrawableTopBar(title = "환경 설정") {
                onCloseClick()
            }
            ReadNotificationSettings(
                    notificationViewModel,
                onReadNotificationChange = { notificationViewModel.viewedNotification = it },
                onReportResultNotificationChange = { notificationViewModel.reportNotification = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            WriteNotificationSettings(
                writingRemindNotification,
                onWriteNotificationChange = {
                    notificationViewModel.updateWritingRemindNotification(
                        it
                    )
                },
                onWriteTimeChange = { isClickedTimeSelection = true },
                hour, min, period
            )
            Spacer(modifier = Modifier.height(20.dp))
            OtherNotificationSettings(viewModel = notificationViewModel) {
                notificationViewModel.marketingNotification = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            LocationServiceSettings(
                    notificationViewModel,
                onLocationNotificationChange = { notificationViewModel.locationNotification = it }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = 120.dp), contentAlignment = Alignment.BottomCenter
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(61.dp), shape = RoundedCornerShape(20),
                onClick = {
                    notificationViewModel.updateUserNotification(
                            notificationViewModel.locationNotification
                    )
                    notificationViewModel.saveWritingRemindNotification(
                        writingRemindNotification
                    ) //글쓰기 시간 알림 설정 저장
                    if (writingRemindNotification) {
                        notificationViewModel.setAlarmFromTimeString(
                            context = context,
                            hour,
                            min,
                            period
                        ) //정해진 시간에 알람설정.
                    } else {
                        notificationViewModel.cancelAlarm(context) //알람 취소
                    }
                }) {
                Text(text = "저장", color = Color.Black)
            }
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
        NotificationTimePickerBox(
            isCancelClicked = { isClickedTimeSelection = false },
            onSaveTime = {
                notificationViewModel.updateTimeSelection()
                isClickedTimeSelection = false
            }
        )
    }
}

@Composable
fun ReadNotificationSettings(
        notificationViewModel: NotificationViewModel,
    onReadNotificationChange: (Boolean) -> Unit,
    onReportResultNotificationChange: (Boolean) -> Unit
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
            notificationViewModel.viewedNotification
    ) {
        onReadNotificationChange(it)
    }
    EssayNotificationBox(
        "신고 결과",
        "알림",
            notificationViewModel.reportNotification
    ) {
        onReportResultNotificationChange(it)
    }
}

@Composable
fun WriteNotificationSettings(
    writingRemindNotification: Boolean,
    onWriteNotificationChange: (Boolean) -> Unit,
    onWriteTimeChange: () -> Unit,
    hour: String,
    min: String,
    period: String
) {
    Text(
        text = "글쓰기 알림",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(20.dp))
    WritingNotificationBox(
        writingRemindNotification,
        {
            onWriteNotificationChange(it)
        },
        { onWriteTimeChange() },
        hour, min, period
    )
}

@Composable
fun OtherNotificationSettings(
    viewModel: NotificationViewModel,
    onOtherNotificationChange: (Boolean) -> Unit
) {
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
        onOtherNotificationChange(it)
    }
}

@Composable
fun LocationServiceSettings(
    viewModel: NotificationViewModel,
    onLocationNotificationChange: (Boolean) -> Unit
) {
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
    ) {
        onLocationNotificationChange(it)
    }
}