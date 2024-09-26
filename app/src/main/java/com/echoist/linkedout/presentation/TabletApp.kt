package com.echoist.linkedout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.echoist.linkedout.R
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.navigation.TabletNavHost
import com.echoist.linkedout.presentation.home.notification.TabletNotificationScreen
import com.echoist.linkedout.presentation.home.drawable.support.inquiry.TabletInquiryScreen
import com.echoist.linkedout.presentation.home.drawable.support.linkedoutsupport.TabletLinkedOutSupportRoute
import com.echoist.linkedout.presentation.home.drawable.setting.TabletSettingRoute
import com.echoist.linkedout.presentation.home.drawable.support.TabletSupportRoute
import com.echoist.linkedout.presentation.home.drawable.thememode.TabletThemeModeScreen
import com.echoist.linkedout.presentation.home.drawable.updatehistory.TabletUpdateHistoryRoute
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.presentation.home.LineChartExample
import com.echoist.linkedout.presentation.home.LogoutBtn
import com.echoist.linkedout.presentation.home.MyBottomNavigation
import com.echoist.linkedout.presentation.home.MyLinkedOutBar
import com.echoist.linkedout.presentation.home.MyProfile
import com.echoist.linkedout.presentation.home.ShopDrawerItem
import com.echoist.linkedout.presentation.home.TabletDrawableItems
import com.echoist.linkedout.presentation.home.tutorial.TabletTutorialScreen
import com.echoist.linkedout.presentation.home.tutorial.TutorialScreen
import com.echoist.linkedout.presentation.myLog.mylog.MyLogViewModel
import com.echoist.linkedout.presentation.util.Routes
import kotlinx.coroutines.launch

@Composable
fun TabletApp(
    navController: NavHostController,
    startDestination: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
    myLogViewModel: MyLogViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar =
        navBackStackEntry?.destination?.route?.startsWith(Routes.Home) == true ||
                navBackStackEntry?.destination?.route == Routes.Community ||
                navBackStackEntry?.destination?.route?.startsWith(Routes.MyLog) == true ||
                navBackStackEntry?.destination?.route == Routes.Settings

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenu by remember { mutableStateOf("Default") }
    var isLogoutClicked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var isNotificationClicked by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                        MyProfile(item = homeViewModel.getMyInfo()) {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(Routes.Settings)
                        }
                        HorizontalDivider(
                            thickness = 6.dp,
                            color = Color(0xFF191919)
                        )
                        MyLinkedOutBar()
                        LineChartExample(essayCounts = homeViewModel.essayCount)
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
            Scaffold(
                topBar = {
                    val currentRoute = navBackStackEntry?.destination?.route

                    TopBarForRoute(
                        modifier = Modifier
                            .fillMaxWidth(if (isNotificationClicked) 0.7f else 1f),
                        currentRoute = currentRoute,
                        homeViewModel = homeViewModel,
                        myLogViewModel = myLogViewModel,
                        onClickDrawable = {
                            scope.launch {
                                drawerState.open()
                            }
                            isNotificationClicked = false
                        },
                        onClickSearch = {
                            navController.navigate(Routes.Search)
                        },
                        onClickNotification = {
                            isNotificationClicked = !isNotificationClicked
                        },
                        onClickTutorial = {
                            homeViewModel.isFirstUser = true
                        },
                        onBackPress = { navController.popBackStack() }
                    )
                },
                floatingActionButton = {
                    if ((navBackStackEntry?.destination?.route?.startsWith(Routes.Home) == true ||
                                navBackStackEntry?.destination?.route?.startsWith(
                                    Routes.MyLog
                                ) == true) && !isNotificationClicked
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(
                                end = 25.dp,
                                bottom = 25.dp
                            ),
                            onClick = {
                                navController.navigate(Routes.WritingPage)
                                homeViewModel.initializeDetailEssay()
                                homeViewModel.setStorageEssay(EssayApi.EssayItem())
                            },
                            shape = RoundedCornerShape(100.dp),
                            containerColor = Color.White
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ftb_edit),
                                contentDescription = "edit",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                bottomBar = {
                    if (showBottomBar) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(if (isNotificationClicked) 0.7f else 1f)
                        ) {
                            MyBottomNavigation(navController = navController) {
                                if (!it.startsWith(Routes.Home) && !it.startsWith(
                                        Routes.MyLog
                                    )
                                )
                                    isNotificationClicked = false
                            }
                        }
                    }
                }) { contentPadding ->
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        TabletNavHost(
                            startDestination = startDestination,
                            navController = navController,
                            contentPadding = contentPadding
                        )
                    }

                    if (isNotificationClicked) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight()
                        ) {
                            TabletNotificationScreen(
                                navController = navController,
                                onBackPress = { isNotificationClicked = false }
                            )
                        }
                    }
                }
            }
        }
        if (homeViewModel.isFirstUser) { // 첫 회원이라면
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.7f))
            )
            TabletTutorialScreen(
                isCloseClicked = {
                    homeViewModel.isFirstUser = false
                    homeViewModel.requestFirstUserToExistUser()
                },
                isSkipClicked = {
                    homeViewModel.isFirstUser = false
                    homeViewModel.requestFirstUserToExistUser()
                })
        }
    }
}