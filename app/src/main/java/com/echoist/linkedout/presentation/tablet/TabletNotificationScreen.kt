package com.echoist.linkedout.presentation.tablet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.data.dto.Alert
import com.echoist.linkedout.presentation.mobile.home.NotificationDate
import com.echoist.linkedout.presentation.mobile.home.NotificationItem
import com.echoist.linkedout.presentation.mobile.home.NotificationOpenLetter
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.presentation.viewModels.SupportViewModel

@Composable
fun TabletNotificationScreen(
    navController: NavController,
    onBackPress: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
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

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TabletDrawableTopBar(
            title = "알림",
            onCloseClick = { onBackPress() }
        )
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
                                alert.essay.id ?: 0
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