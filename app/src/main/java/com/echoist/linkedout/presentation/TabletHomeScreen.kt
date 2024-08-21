package com.echoist.linkedout.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.page.home.LineChartExample
import com.echoist.linkedout.page.home.LogoutBtn
import com.echoist.linkedout.page.home.MyLinkedOutBar
import com.echoist.linkedout.page.home.MyProfile
import com.echoist.linkedout.page.home.ShopDrawerItem
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun TabletHomeRoute(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel = hiltViewModel(),
    statusCode: Int
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenu by remember { mutableStateOf("Default") }
    var isLogoutClicked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Row {
                Column(
                    Modifier
                        .width(360.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF121212))
                        .verticalScroll(scrollState),
                ) {
                    MyProfile(item = viewModel.getMyInfo()) { }
                    HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
                    MyLinkedOutBar()
                    LineChartExample(essayCounts = viewModel.essayCount)
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
                    ShopDrawerItem()
                    HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
                    TabletDrawableItems("화면 설정", selectedMenu == "화면 설정") {
                        selectedMenu = "화면 설정"
                    }
                    TabletDrawableItems("환경 설정", selectedMenu == "환경 설정") {
                        selectedMenu = "환경 설정"
                    }
                    TabletDrawableItems("고객지원", selectedMenu == "고객지원") {
                        selectedMenu = "고객지원"
                    }
                    TabletDrawableItems("업데이트 기록", selectedMenu == "업데이트 기록") {
                        selectedMenu = "업데이트 기록"
                    }

                    LogoutBtn { isLogoutClicked = true } //todo logout 기능 만들기
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
    ) {
        Column {
            TabletMainTopBar({
                scope.launch {
                    drawerState.apply {
                        selectedMenu = "Default"
                        if (isClosed) open() else close()
                    }
                }
            }, { navController.navigate("NotificationPage") },
                viewModel.isExistUnreadAlerts
            )
            {
                viewModel.isFirstUser = true //튜토리얼 화면띄우기위함
            }
            Box(modifier = Modifier.fillMaxSize()) {
                GlideImage(
                    model = R.drawable.home_basic_tablet,
                    contentDescription = "home_img",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
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