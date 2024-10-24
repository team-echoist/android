package com.echoist.linkedout.presentation.home.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.data.dto.Alert
import com.echoist.linkedout.presentation.home.drawable.support.SupportViewModel
import com.echoist.linkedout.presentation.userInfo.account.SettingTopAppBar
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.ui.theme.LinkedInColor
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotificationPage(navController: NavController, viewModel: SupportViewModel = hiltViewModel()) {
    viewModel.readAlertsList()
    var isAlertClicked by remember { mutableStateOf(false) }
    var clickedAlert: Alert? by remember { mutableStateOf(null) }

    val myProfile = viewModel.readMyProfile()

    val annotatedString = AnnotatedString.Builder().apply {
        withStyle(style = SpanStyle(color = LinkedInColor)) { append("${myProfile.nickname} 아무개님의 ") }
        append("새 글")
        withStyle(style = SpanStyle(color = LinkedInColor)) { append("이\n") }
        append("숨바꼭질")
        withStyle(style = SpanStyle(color = LinkedInColor)) { append("을 시작했어요!") }
    }.toAnnotatedString()

    val navigateToCommunityDetail by viewModel.navigateToCommunityDetail.collectAsState()
    LaunchedEffect(key1 = navigateToCommunityDetail) {
        if (navigateToCommunityDetail) {
            navController.navigate(Routes.CommunityDetailPage)
            viewModel.onNavigated()
        }
    }

    Scaffold(
        topBar = {
            SettingTopAppBar("알림", navController)
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (viewModel.alertList.isNotEmpty()) {
                val groupedAlerts =
                    viewModel.alertList.groupBy { it -> it.createdDate.split("T")[0] }
                groupedAlerts.forEach { (date, alerts) ->
                    NotificationDate(date)
                    Spacer(modifier = Modifier.height(5.dp))
                    alerts.forEach { alert ->
                        NotificationItem(viewModel.readMyProfile().nickname ?: "", alert) {
                            viewModel.readAlert(alert.id)

                            when (alert.type) {
                                "published" -> viewModel.readDetailEssay(
                                    alert.essay.id ?: 0,
                                )

                                else -> { //링크드아웃, 고객지원 모두 오픈테러
                                    isAlertClicked = true
                                    clickedAlert = alert
                                }
                            }

                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
        if (viewModel.alertList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF191919),
                            shape = RoundedCornerShape(size = 10.dp)
                        )
                        .width(350.dp)
                        .height(119.dp)
                        .background(
                            color = Color(0xFF000000),
                            shape = RoundedCornerShape(size = 10.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = annotatedString
                    )
                }

            }
        }

        AnimatedVisibility(
            visible = isAlertClicked,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
        ) {
            NotificationOpenLetter(
                clickedAlert!!,
                isOkClicked = { isAlertClicked = false },
                isCancelClicked = { isAlertClicked = false }
            )

        }
    }
}

@Composable
fun NotificationDate(date: String) {
    val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
    val formattedDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(parsedDate!!)

    Text(
        text = formattedDate,
        style = TextStyle(
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Color(0xFF3E3E3E),
        ),
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
fun NotificationIcon(alert: Alert) {

    val text = when (alert.type) {
        "published" -> "발행한 글"
        "linkedout" -> "Linked-out"
        else -> "고객지원"
    }

    val imageResource = when (alert.type) {
        "published" -> R.drawable.alarm_publish_small
        "linkedout" -> R.drawable.alarm_linkedout_small
        else -> {
            R.drawable.social_googlebtn
        }
    }

    Box(modifier = Modifier.size(75.dp, 90.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp), contentAlignment = Alignment.TopCenter
        ) {
            Icon(
                painter = painterResource(id = imageResource),
                tint = Color.Unspecified,
                contentDescription = "icon"
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .width(78.dp)
                    .height(23.dp)
                    .background(
                        color = Color(0xFF121212),
                        shape = RoundedCornerShape(size = 20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = LinkedInColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun NotificationItem(nickName: String, alert: Alert, isAlertClicked: () -> Unit) {

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            append("다른 아무개가 $nickName 아무개님의 ")
            withStyle(
                style = SpanStyle(
                    color = LinkedInColor,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("'${alert.title}'")
            }
            append("글을 찾았어요! ")
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF777777),
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append(" 2시간")
            }
        }.toAnnotatedString()
    }
    var borderColor by remember { mutableStateOf(if (alert.read) Color(0xFF191919) else LinkedInColor) }

    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(size = 10.dp)
            )
            .fillMaxWidth()
            .height(119.dp)
            .clickable {
                isAlertClicked()
                borderColor = Color(0xFF191919)
            }
            .background(color = Color.Transparent, shape = RoundedCornerShape(size = 10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        NotificationIcon(alert)
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = annotatedString,
            color = Color.White,
            modifier = Modifier.padding(end = 20.dp),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(20.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NotificationOpenLetter(alert: Alert, isOkClicked: () -> Unit, isCancelClicked: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(0.7f))
            .clickable { isCancelClicked() }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(320.dp, 400.dp)
                    .padding(bottom = 30.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.alarm_textbox),
                    contentDescription = "box",
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(84.dp))

                    Box(
                        modifier = Modifier
                            .width(78.dp)
                            .height(23.dp)
                            .background(
                                color = Color(0xFF121212),
                                shape = RoundedCornerShape(size = 20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Linked-out",
                            color = LinkedInColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "'${alert.content}'",
                        maxLines = 3,
                        color = LinkedInColor,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(text = "${alert.body}", fontSize = 14.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(30.dp))

                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp)
                        .padding(horizontal = 20.dp), contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = { isOkClicked() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "확인", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                }

            }
            if (alert.type == "linkedout") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    GlideImage(model = R.drawable.alarm_linkedout, contentDescription = "")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    GlideImage(model = R.drawable.alarm_pending, contentDescription = "")
                }
            }

        }

    }

}
