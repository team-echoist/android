package com.echoist.linkedout.presentation.home.drawable.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.home.notification.NotificationViewModel
import com.echoist.linkedout.presentation.home.HomeViewModel

@Composable
fun TabletSettingRoute(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onCloseClick: () -> Unit
) {
    viewModel.readUserNotification()

    val hour by notificationViewModel.hour.collectAsState()
    val min by notificationViewModel.min.collectAsState()
    val period by notificationViewModel.period.collectAsState()
    val writingRemindNotification by notificationViewModel.writingRemindNotification.collectAsState()

    var isClickedTimeSelection by remember { mutableStateOf(false) }
    if (viewModel.isApifinished) {
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
                viewModel,
                onReadNotificationChange = { viewModel.viewedNotification = it },
                onReportResultNotificationChange = { viewModel.reportNotification = it }
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
            OtherNotificationSettings(viewModel = viewModel) {
                viewModel.marketingNotification = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            LocationServiceSettings(
                viewModel,
                onLocationNotificationChange = { viewModel.locationNotification = it }
            )
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
        //NotificationTimePickerBox({ isClickedTimeSelection = false }, navController)
    }
}

@Composable
fun ReadNotificationSettings(
    viewModel: HomeViewModel,
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
        viewModel.viewedNotification
    ) {
        onReadNotificationChange(it)
    }
    EssayNotificationBox(
        "신고 결과",
        "알림",
        viewModel.reportNotification
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
    viewModel: HomeViewModel,
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
    viewModel: HomeViewModel,
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