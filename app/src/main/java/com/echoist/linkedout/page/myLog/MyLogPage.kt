package com.echoist.linkedout.page.myLog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.components.EssayChips
import com.echoist.linkedout.components.EssayPager
import com.echoist.linkedout.components.ModifyStoryBox
import com.echoist.linkedout.components.MyLogTopAppBar
import com.echoist.linkedout.page.community.SearchingPage
import com.echoist.linkedout.page.home.MyBottomNavigation
import com.echoist.linkedout.page.home.WriteFTB
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import kotlinx.coroutines.launch


@Composable
fun MyLogPage(navController: NavController, viewModel: MyLogViewModel,writingViewModel: WritingViewModel,page : Int = 0) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val homeViewModel: HomeViewModel = hiltViewModel()

    val pagerstate = rememberPagerState { 3 }
    val hasCalledApi = remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = Unit) {
        pagerstate.animateScrollToPage(page)

        if (!hasCalledApi.value) {
            viewModel.myEssayList.clear()
            viewModel.publishedEssayList.clear()
            viewModel.readMyEssay()
            viewModel.readPublishEssay()
            viewModel.readMyStory()
            viewModel.requestUnreadAlerts()

            hasCalledApi.value = true
        }
    }


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    ModalDrawerSheet(
                        modifier = Modifier.fillMaxSize(),
                        drawerShape = RectangleShape,
                        drawerContainerColor = Color.Black
                    ) {
                        SearchingPage(drawerState = drawerState, navController = navController)
                        // ...other drawer items
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    LinkedOutTheme {
                        Scaffold(

                            topBar = {
                                Column {

                                    MyLogTopAppBar(
                                        {
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        },
                                        viewModel.getUserInfo().nickname!!,
                                        { navController.navigate("NotificationPage") },
                                        viewModel.isExistUnreadAlerts)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    EssayChips(pagerstate, viewModel)
                                }
                            },

                            bottomBar = { MyBottomNavigation(navController) },
                            floatingActionButton = { WriteFTB(navController, homeViewModel,writingViewModel) },
                            content = {
                                Box(Modifier.padding(it)) {
                                    EssayPager(
                                        pagerState = pagerstate,
                                        viewModel,
                                        navController = navController,
                                        writingViewModel
                                    )
                                }
                            }
                        )
//                        if (isLoading) {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CircularProgressIndicator(color = LinkedInColor)
//                            }
//                        }

                        AnimatedVisibility(
                            visible = viewModel.isModifyStoryClicked,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = LinearEasing
                                )
                            )
                        ) {
                            ModifyStoryBox(viewModel, navController)

                        }
                    }
                }
            })
    }
}
