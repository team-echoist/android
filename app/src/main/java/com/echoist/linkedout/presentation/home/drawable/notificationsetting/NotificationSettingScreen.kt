package com.echoist.linkedout.presentation.home.drawable.notificationsetting

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.home.ImageSwitch
import com.echoist.linkedout.presentation.home.NotificationViewModel
import com.echoist.linkedout.presentation.home.home.HomeViewModel
import com.echoist.linkedout.presentation.userInfo.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor

@Composable
fun NotificationSettingScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {

    homeViewModel.readUserNotification()
    val context = LocalContext.current

    val hour by notificationViewModel.hour.collectAsState()
    val min by notificationViewModel.min.collectAsState()
    val period by notificationViewModel.period.collectAsState()
    val writingRemindNotification by notificationViewModel.writingRemindNotification.collectAsState()

    Scaffold(
        topBar = {
            SettingTopAppBar("알림", navController)
        },
        content = {
            var isClickedTimeSelection by remember { mutableStateOf(false) }

            if (homeViewModel.isApifinished) {
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
                        homeViewModel.viewedNotification
                    ) { it ->
                        homeViewModel.viewedNotification = it
                        Log.d(TAG, "NotificationPage: $it")
                    }

                    EssayNotificationBox(
                        "신고 결과",
                        "알림",
                        homeViewModel.reportNotification
                    ) { it ->
                        homeViewModel.reportNotification = it
                        Log.d(TAG, "NotificationPage: $it")
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
                            notificationViewModel.updateWritingRemindNotification(it)
                            Log.d(TAG, "NotificationPage: $it")
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
                        homeViewModel.marketingNotification
                    ) { it ->
                        homeViewModel.marketingNotification = it
                        Log.d(TAG, "NotificationPage: $it")
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
                        homeViewModel.locationNotification
                    ) { it ->
                        homeViewModel.locationNotification = it
                        Log.d(TAG, "NotificationPage: $it")
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
                exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
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
                        homeViewModel.updateUserNotification(
                            homeViewModel.locationNotification
                        )
                        notificationViewModel.saveWritingRemindNotification(
                            writingRemindNotification
                        ) //글쓰기 시간 알림 설정 저장
                        if (writingRemindNotification) {
                            //homeViewModel.setAlarmAfter10(context) //테스트용 1초후알람
                            homeViewModel.setAlarmFromTimeString(
                                context = context,
                                hour,
                                min,
                                period
                            ) //정해진 시간에 알람설정.
                        } else {
                            homeViewModel.cancelAlarm(context) //알람 취소
                        }
                    }) {
                    Text(text = "저장", color = Color.Black)
                }
            }
        }
    )
}


@Composable
fun EssayNotificationBox(
    coloredText: String,
    normalText: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0E0E0E))
            .height(58.dp)
            .padding(horizontal = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Row {
                Text(text = "$coloredText ", color = LinkedInColor)
                Text(text = normalText, color = Color.White)
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            ImageSwitch(
                height = 26.dp,
                width = 50.dp,
                outerBackgroundOnResource = R.drawable.switch_true,
                outerBackgroundOffResource = R.drawable.switch_false,
                circleBackgroundOnResource = R.drawable.switch_ball,
                circleBackgroundOffResource = R.drawable.switch_ball,
                initialValue = if (isChecked) 1 else 0, // 초기값 설정
                onCheckedChanged = { checked ->
                    if (checked) {
                        Log.d("checked test", "true")
                    } else {
                        Log.d("checked test", "false")
                    }
                    onCheckChange(checked)
                }
            )
            //Switch(checked = isChecked, onCheckedChange = { onCheckChange(it) })

        }

    }
}

@Composable
fun WritingNotificationBox(
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    isTimeSelectionClicked: () -> Unit,
    hour: String,
    min: String,
    period: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0E0E0E), shape = RoundedCornerShape(6))
            .height(120.dp)
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp), contentAlignment = Alignment.TopStart
        ) {
            Row {
                Text(text = "글쓰기 시간 알림 설정", color = Color.White)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp), contentAlignment = Alignment.TopEnd
        ) {
            ImageSwitch(
                height = 26.dp,
                width = 50.dp,
                outerBackgroundOnResource = R.drawable.switch_true,
                outerBackgroundOffResource = R.drawable.switch_false,
                circleBackgroundOnResource = R.drawable.switch_ball,
                circleBackgroundOffResource = R.drawable.switch_ball,
                initialValue = if (isChecked) 1 else 0, // 초기값 설정
                onCheckedChanged = { checked ->
                    if (checked) {
                        Log.d("checked test", "true")
                    } else {
                        Log.d("checked test", "false")
                    }
                    onCheckChange(checked)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp, 34.dp)
                    .clickable { isTimeSelectionClicked() }
                    .background(Color(0xFF222222)), contentAlignment = Alignment.Center
            ) {
                Text(text = "${hour}:${min} $period", color = Color(0xFF979797))
            }
        }
    }
}

@Composable
fun NotificationTimePickerBox(
    isCancelClicked: () -> Unit,
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedTime by remember { mutableStateOf<TimeSelectionIndex?>(null) }
    val savedTimeSelection = remember { notificationViewModel.getTimeSelection() }

    Log.d("NotificationTimePickerBox", "Saved Time: $savedTimeSelection")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF212121), shape = RoundedCornerShape(5))
                .size(299.dp, 286.dp)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text(text = "알림 허용 시간", color = Color.White)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = Color(0xFF575757),
                    modifier = Modifier.clickable {
                        isCancelClicked()
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                NotificationTimePicker(
                    initialTime = savedTimeSelection,
                    onTimeSelected = { time ->
                        selectedTime = time
                    }
                )
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Button(
                    onClick = {
                        selectedTime?.let { time ->
                            val message =
                                "${time.periodIndex} ${time.hourIndex}:${time.minuteIndex}"
                            Log.d("NotificationTimePickerBox", "Selected Time: $message")
                            notificationViewModel.saveTimeSelection(time)
                            navController.navigate("NotificationSettingScreen")
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20)
                ) {
                    Text(text = "저장", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun NotificationTimePicker(
    initialTime: TimeSelectionIndex,
    onTimeSelected: (TimeSelectionIndex) -> Unit
) {
    var timePeriodIndex by remember { mutableIntStateOf(initialTime.periodIndex) }
    var hourIndex by remember { mutableIntStateOf(initialTime.hourIndex) }
    var minuteIndex by remember { mutableIntStateOf(initialTime.minuteIndex) }

    val timePeriods = listOf("오전", "오후")
    val hours = (1..12).map { it.toString().padStart(2, '0') }
    val minutes = (0..50 step 10).map { it.toString().padStart(2, '0') }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase time period",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        timePeriodIndex = (timePeriodIndex + 1) % timePeriods.size
                    },
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase hour",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        hourIndex = (hourIndex + 1) % hours.size
                    },
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase minute",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        minuteIndex = (minuteIndex + 1) % minutes.size
                    },
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            TimeBox(text = timePeriods[timePeriodIndex])
            Spacer(modifier = Modifier.width(16.dp))
            TimeBox(text = hours[hourIndex])
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = ":",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            TimeBox(text = minutes[minuteIndex])
            Spacer(modifier = Modifier.width(12.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease time period",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        timePeriodIndex =
                            (timePeriodIndex - 1 + timePeriods.size) % timePeriods.size
                    },
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease hour",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        hourIndex = (hourIndex - 1 + hours.size) % hours.size
                    },
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease minute",
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        minuteIndex = (minuteIndex - 1 + minutes.size) % minutes.size
                    },
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    val selectedTimeIndex = remember(timePeriodIndex, hourIndex, minuteIndex) {
        TimeSelectionIndex(timePeriodIndex, hourIndex, minuteIndex)
    }

    LaunchedEffect(selectedTimeIndex) {
        onTimeSelected(selectedTimeIndex)
    }
}

@Composable
fun TimeBox(text: String) {
    Box(
        modifier = Modifier
            .size(70.dp, 48.dp)
            .border(
                BorderStroke(1.dp, Color(0xFF313131)),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

data class TimeSelectionIndex(
    val periodIndex: Int,
    val hourIndex: Int,
    val minuteIndex: Int
)