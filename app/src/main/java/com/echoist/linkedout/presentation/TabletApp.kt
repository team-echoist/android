package com.echoist.linkedout.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.echoist.linkedout.R
import com.echoist.linkedout.navigation.TabletNavHost
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.presentation.home.LogoutBox
import com.echoist.linkedout.presentation.home.MyBottomNavigation
import com.echoist.linkedout.presentation.home.drawable.DrawableViewModel
import com.echoist.linkedout.presentation.home.drawable.TabletDrawableScreen
import com.echoist.linkedout.presentation.home.notification.TabletNotificationScreen
import com.echoist.linkedout.presentation.home.tutorial.TabletTutorialScreen
import com.echoist.linkedout.presentation.myLog.mylog.MyLogViewModel
import com.echoist.linkedout.presentation.userInfo.account.UserInfoViewModel
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import kotlinx.coroutines.launch

@Composable
fun TabletApp(
    navController: NavHostController,
    startDestination: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
    drawableViewModel: DrawableViewModel = hiltViewModel(),
    myLogViewModel: MyLogViewModel = hiltViewModel(),
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar =
        navBackStackEntry?.destination?.route?.startsWith(Routes.Home) == true ||
                navBackStackEntry?.destination?.route == Routes.Community ||
                navBackStackEntry?.destination?.route?.startsWith(Routes.MyLog) == true ||
                navBackStackEntry?.destination?.route == Routes.Settings

    var selectedMenu by remember { mutableStateOf("Default") }
    var isLogoutClicked by remember { mutableStateOf(false) }
    var isNotificationClicked by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        drawableViewModel.requestUserGraphSummary()
    }

    val essayCount by drawableViewModel.essayCount.collectAsState()
    val myInfo by homeViewModel.getMyInfo().collectAsState()

    val fillWidthFraction = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> if (isNotificationClicked) 0.7f else 1f
        else -> if (isNotificationClicked) 0.55f else 1f
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                TabletDrawableScreen(
                    scrollState = scrollState,
                    userInfo = myInfo,
                    essayCounts = essayCount,
                    selectedMenu = selectedMenu,
                    onClickMyInfo = {
                        scope.launch {
                            drawerState.close()
                        }
                        selectedMenu = "Default"
                        navController.navigate(Routes.Settings)
                    },
                    onClickLogout = {
                        isLogoutClicked = true
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    val currentRoute = navBackStackEntry?.destination?.route

                    TopBarForRoute(
                        modifier = Modifier
                            .fillMaxWidth(fillWidthFraction),
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
                        onBackPress = { navController.popBackStack() },
                        onClickBookmarkList = {
                            navController.navigate(Routes.CommunitySavedEssayPage)
                        },
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
                                writingViewModel.initialize()
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
                                .fillMaxWidth(fillWidthFraction)
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
                                .fillMaxWidth(1f - fillWidthFraction)
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
                    scope.launch {
                        drawerState.close()
                    }
                    userInfoViewModel.logout()
                    isLogoutClicked = false
                    navigateWithClearBackStack(navController, Routes.LoginPage)
                }
            )
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