package com.echoist.linkedout.presentation.home.drawable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.home.LineChartExample
import com.echoist.linkedout.presentation.home.LogoutBtn
import com.echoist.linkedout.presentation.home.MyLinkedOutBar
import com.echoist.linkedout.presentation.home.MyProfile
import com.echoist.linkedout.presentation.home.ShopDrawerItem
import com.echoist.linkedout.presentation.home.TabletDrawableItems
import com.echoist.linkedout.presentation.home.drawable.setting.TabletSettingRoute
import com.echoist.linkedout.presentation.home.drawable.support.TabletSupportRoute
import com.echoist.linkedout.presentation.home.drawable.support.inquiry.TabletInquiryScreen
import com.echoist.linkedout.presentation.home.drawable.support.linkedoutsupport.TabletLinkedOutSupportRoute
import com.echoist.linkedout.presentation.home.drawable.thememode.TabletThemeModeScreen
import com.echoist.linkedout.presentation.home.drawable.updatehistory.TabletUpdateHistoryRoute

@Composable
fun TabletDrawableScreen(
    navController: NavController,
    scrollState: ScrollState,
    userInfo: UserInfo,
    essayCounts: List<Int>,
    selectedMenu: String,
    onClickMyInfo: () -> Unit,
    onClickLogout: () -> Unit,
) {
    var selectedMenu by remember { mutableStateOf(selectedMenu) }

    Row {
        Column(
            Modifier
                .width(360.dp)
                .fillMaxHeight()
                .background(Color(0xFF121212))
                .verticalScroll(scrollState),
        ) {
            MyProfile(item = userInfo) {
                onClickMyInfo()
            }
            HorizontalDivider(
                thickness = 6.dp,
                color = Color(0xFF191919)
            )
            MyLinkedOutBar()
            LineChartExample(essayCounts = essayCounts)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 6.dp,
                color = Color(0xFF191919)
            )
            ShopDrawerItem()
            HorizontalDivider(
                thickness = 6.dp,
                color = Color(0xFF191919)
            )
            TabletDrawableItems("화면 설정", selectedMenu == "화면 설정") {
                selectedMenu = "화면 설정"
            }
            TabletDrawableItems("환경 설정", selectedMenu == "환경 설정") {
                selectedMenu = "환경 설정"
            }
            TabletDrawableItems(
                "고객지원",
                selectedMenu == "고객지원" || selectedMenu == "링크드아웃 고객센터"
            ) {
                selectedMenu = "고객지원"
            }
            TabletDrawableItems("업데이트 기록", selectedMenu == "업데이트 기록") {
                selectedMenu = "업데이트 기록"
            }

            LogoutBtn { onClickLogout() } //isLogoutClicked = true } //todo logout 기능 만들기
        }
        Box(Modifier.fillMaxSize()) {
            when (selectedMenu) {
                "Default" -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .alpha(0.0f)
                    )
                }

                "화면 설정" -> {
                    TabletThemeModeScreen {
                        selectedMenu = "Default"
                    }
                }

                "환경 설정" -> {
                    TabletSettingRoute(navController = navController) {
                        selectedMenu = "Default"
                    }
                }

                "고객지원" -> {
                    TabletSupportRoute(onCloseClick = {
                        selectedMenu = "Default"
                    }, onClickSupport = {
                        selectedMenu = "링크드아웃 고객센터"
                    })
                }

                "업데이트 기록" -> {
                    TabletUpdateHistoryRoute {
                        selectedMenu = "Default"
                    }
                }

                "링크드아웃 고객센터" -> {
                    TabletLinkedOutSupportRoute(onBackPressed = {
                        selectedMenu = "고객지원"
                    }, onClickInquiry = {
                        selectedMenu = "1:1 문의하기"
                    })
                }

                "1:1 문의하기" -> {
                    TabletInquiryScreen {
                        selectedMenu = "링크드아웃 고객센터"
                    }
                }
            }
        }
    }
}