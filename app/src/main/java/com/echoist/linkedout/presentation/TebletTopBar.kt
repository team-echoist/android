package com.echoist.linkedout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.Routes
import com.echoist.linkedout.components.MyLogTopAppBar
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.MyLogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBarForRoute(
    currentRoute: String?,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    homeViewModel: HomeViewModel = hiltViewModel(),
    myLogViewModel: MyLogViewModel = hiltViewModel(),
    onSearchClick: () -> Unit,
    pagerState: PagerState = rememberPagerState { 1 }
) {
    when {
        currentRoute?.contains(Routes.Home) == true -> {
            TabletHomeTopBar(
                modifier = Modifier.background(Color.Transparent),
                onClick = {
                    scope.launch { drawerState.open() }
                },
                onClickNotification = { navController.navigate("NotificationPage") },
                isExistUnreadAlerts = homeViewModel.isExistUnreadAlerts,
                isClickedTutorial = { homeViewModel.isFirstUser = true }
            )
        }

        currentRoute?.contains(Routes.MyLog) == true -> {
            TabletMyLogTopBar(
                viewModel = myLogViewModel,
                navController = navController,
                onSearchClick = { onSearchClick() }
            )
        }

        currentRoute == Routes.Community -> {
            TabletCommunityTopBar(
                modifier = Modifier.background(Color(0xFFD9D9D9)),
                text = "Community", // 원하는 제목으로 변경 가능
                pagerState = pagerState,
                onSearchClick = { onSearchClick() },
                onClickBookMarked = { /* 북마크 클릭 시 동작 */ }
            )
        }

        currentRoute == Routes.Settings -> {
            TabletMyInfoTopBar()
        }

        else -> {
            // 기본 탑바 또는 아무 것도 표시하지 않음
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletHomeTopBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onClickNotification: () -> Unit,
    isExistUnreadAlerts: Boolean,
    isClickedTutorial: () -> Unit
) {
    val img = if (isExistUnreadAlerts) R.drawable.icon_noti_on else R.drawable.icon_noti_off
    TopAppBar(
        modifier = modifier,
        title = { }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.hamburber),
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(start = 10.dp)
                    .size(24.dp)
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.icon_information),
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier
                    .clickable { isClickedTutorial() }
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
            Icon(
                painter = painterResource(id = img),
                contentDescription = "Notifications",
                tint = Color.Unspecified,
                modifier = Modifier
                    .clickable { onClickNotification() }
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
        },
    )
}

@Composable
fun TabletMyLogTopBar(
    viewModel: MyLogViewModel,
    navController: NavController,
    onSearchClick: () -> Unit
) {
    Column {
        MyLogTopAppBar(
            { onSearchClick() },
            viewModel.getUserInfo().nickname!!,
            { navController.navigate("NotificationPage") },
            viewModel.isExistUnreadAlerts
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletCommunityTopBar(
    modifier: Modifier = Modifier,
    text: String,
    pagerState: PagerState,
    onSearchClick: () -> Unit,
    onClickBookMarked: () -> Unit
) {
    val color = if (pagerState.currentPage == 0) Color.Black else Color.White

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = text, fontWeight = FontWeight.Bold, color = color)
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .clickable { onSearchClick() },
                tint = color
            )
            Spacer(modifier = Modifier.width(13.dp))
            Icon(
                painter = painterResource(id = R.drawable.icon_bookmarkfill_black),
                contentDescription = "",
                Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
                    .clickable { onClickBookMarked() },
                tint = color
            )
        }
    )
}

@Composable
fun TabletMyInfoTopBar(
) {
    Text(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        text = "MY",
        color = Color.White,
        modifier = Modifier
            .padding(start = 20.dp, top = 10.dp)
            .safeDrawingPadding()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletDrawableTopBar(title: String, isBack: Boolean = false, onCloseClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            if (isBack.not()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    modifier = Modifier
                        .clickable { onCloseClick() }
                        .padding(end = 20.dp)
                        .size(30.dp),
                    tint = Color.White
                )
            }
        },
        navigationIcon = {
            if (isBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "arrow back",
                    modifier = Modifier
                        .clickable { onCloseClick() }
                        .padding(start = 10.dp)
                        .size(30.dp),
                    tint = Color.White
                )
            }
        }
    )
}