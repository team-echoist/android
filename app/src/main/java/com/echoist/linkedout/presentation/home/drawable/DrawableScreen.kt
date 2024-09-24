package com.echoist.linkedout.presentation.home.drawable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.presentation.home.LineChartExample
import com.echoist.linkedout.presentation.home.LogoutBox
import com.echoist.linkedout.presentation.home.LogoutBtn
import com.echoist.linkedout.presentation.home.MyDrawableItem
import com.echoist.linkedout.presentation.home.MyLinkedOutBar
import com.echoist.linkedout.presentation.home.MyProfile
import com.echoist.linkedout.presentation.home.ShopDrawerItem
import com.echoist.linkedout.presentation.userInfo.account.UserInfoViewModel
import com.echoist.linkedout.presentation.util.Routes

@Composable
fun DrawableScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    userInfoViewModel: UserInfoViewModel = hiltViewModel()
) {
    var isLogoutClicked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.9f),
        drawerShape = RectangleShape,
        drawerContainerColor = Color(0xFF121212)
    ) {
        Column(Modifier.verticalScroll(scrollState)) {
            MyProfile(item = viewModel.getMyInfo()) { navController.navigate("SETTINGS") }
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            MyLinkedOutBar()
            LineChartExample(essayCounts = viewModel.essayCount)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            ShopDrawerItem()
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            MyDrawableItem("화면 설정") { navController.navigate(Routes.ThemeModeScreen) }
            MyDrawableItem("환경 설정") { navController.navigate("NotificationSettingScreen") }
            MyDrawableItem("고객지원") { navController.navigate("SupportScreen") }
            MyDrawableItem("업데이트 기록") { navController.navigate("UpdateHistoryScreen") }

            LogoutBtn { isLogoutClicked = true } //todo logout 기능 만들기
            // ...other drawer items
        }
    }
    AnimatedVisibility(
        visible = isLogoutClicked,
        enter = slideInVertically(
            initialOffsetY = { 2000 },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { 2000 },
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        LogoutBox(
            isCancelClicked = { isLogoutClicked = false },
            isLogoutClicked = {
                userInfoViewModel.logout()
                isLogoutClicked = false
                navController.navigate(Routes.LoginPage) {
                    popUpTo(Routes.LoginPage) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}