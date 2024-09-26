package com.echoist.linkedout.presentation.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.home.tutorial.TutorialScreen
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TUTORIAL_BULB
import com.echoist.linkedout.presentation.util.UserStatus
import com.echoist.linkedout.presentation.util.getCurrentDateFormatted
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TabletHomeRoute(
    statusCode: Int,
    viewModel: HomeViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel = hiltViewModel()
) {
    val isUserDeleteApiFinished by viewModel.isUserDeleteApiFinished.collectAsState()
    val isExistLatestUpdate by viewModel.isExistLatestUpdate.collectAsState()
    var userStatus by remember { mutableStateOf(UserStatus.Activated) }

    userStatus = when (statusCode) {
        202 -> UserStatus.DeActivated
        403 -> UserStatus.Banned
        else -> UserStatus.Activated //todo 현재 202만 탈퇴유저 밴, 관찰유저, 등등 추가예정
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.requestMyInfo()
        viewModel.requestUserGraphSummary()
        viewModel.requestGuleRoquis()
        viewModel.requestUnreadAlerts()
        viewModel.requestLatestNotice()
       // viewModel.requestRegisterDevice(context) //로그인 후 홈 진입 시 한번만 회원정보 등록
        viewModel.requestLatestUpdate()
    }

    LaunchedEffect(key1 = isUserDeleteApiFinished) {
        if (isUserDeleteApiFinished) {
            //navigateWithClearBackStack(navController, Routes.LoginPage)
            viewModel.setApiStatusToFalse()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            model = R.drawable.home_basic_tablet,
            contentDescription = "home_img",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = (LocalConfiguration.current.screenWidthDp * 0.25).dp,
                bottom = (LocalConfiguration.current.screenHeightDp * 0.30).dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(80.dp)
                .clickable { }) {
            GlideImage(
                model = TUTORIAL_BULB,
                contentDescription = "bulb_img",
                modifier = Modifier
                    .size(80.dp)
            )
        }
    }
    AnimatedVisibility(
        visible = viewModel.isVisibleGeulRoquis && !viewModel.isFirstUser, //튜토리얼을 건너뛰어야 글로키를 볼수있음
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        ) {
            GeulRoquis(
                isHoldClicked = { viewModel.isVisibleGeulRoquis = false },
                isAcceptClicked = {
                    writingViewModel.title.value =
                        TextFieldValue("${getCurrentDateFormatted()} GeulRoquis")
                    writingViewModel.hint =
                        ("글로키란? : 글(geul)과 크로키(croquis)의 합성어로 글을 본격적으로 쓰기 전, 주어진 상황을 묘사하거나 상상을 덧대어 빠르게 스케치 하듯이 글을 쓰는 몸풀기를 말합니다. ")
                    writingViewModel.imageUrl = viewModel.geulRoquisUrl
                    //navController.navigate("WritingPage")
                    viewModel.isVisibleGeulRoquis = false
                }, viewModel
            )
        }
    }
    if (viewModel.isFirstUser) { // 첫 회원이라면
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f))
        )
        TutorialScreen(
            isCloseClicked = {
                viewModel.isFirstUser = false
                viewModel.requestFirstUserToExistUser()
            },
            isSkipClicked = {
                viewModel.isFirstUser = false
                viewModel.requestFirstUserToExistUser()
            })
    }
    if (userStatus == UserStatus.DeActivated) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        )
        {
            ReactivateOrDeleteBox(
                isClickedReActivate = {
                    viewModel.requestUserReActivate()
                    userStatus = UserStatus.Activated
                })
            {
                viewModel.requestUserDelete()
            }
        }
    }
    if (viewModel.latestNoticeId != null) { //id값
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        )
        {
            NoticeAlert(isClickedClose = {
                viewModel.latestNoticeId = null
            }, isClickedOpened = {
                //navController.navigate("${Routes.NoticeDetailPage}/${viewModel.latestNoticeId!!}")
            })
        }
    }

    if (isExistLatestUpdate) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        )
        {
            UpdateAlert(isClickedClose = {
                viewModel.updateIsExistLatestUpdate(false)
            }, isClickedOpened = {
                //navController.navigate(Routes.UpdateHistoryScreen)
                viewModel.updateIsExistLatestUpdate(false)
            })
        }
    }
}

@Composable
fun TabletDrawableItems(text: String, isSelected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        modifier = Modifier.height(70.dp),
        label = {
            Text(
                text = text,
                color = if (isSelected) Color(0xFF616FED) else Color.White,
                fontWeight = FontWeight.SemiBold
            )
        },
        selected = false,
        onClick = { onClick() },
    )
}