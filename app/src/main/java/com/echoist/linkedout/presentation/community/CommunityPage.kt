package com.echoist.linkedout.presentation.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.community.search.SearchingPage
import com.echoist.linkedout.presentation.home.home.MyBottomNavigation
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun CommunityPage(navController: NavController, viewModel: CommunityViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val pagerstate = rememberPagerState { 1 } //todo 다음버전에 구독페이지 추가하기. pagerstate 2로 수정
    val color = if (pagerstate.currentPage == 0) Color(0xFFD9D9D9) else Color.Black

    if (pagerstate.currentPage == 1) {
        viewModel.isClicked = false
    }
    val isLoading by viewModel.isLoading.collectAsState()

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
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    Scaffold(
                        modifier = Modifier.background(color),
                        topBar = {
                            Column(Modifier.background(color)) {
                                CommunityTopAppBar(
                                    "커뮤니티",
                                    pagerstate,
                                    onSearchClick =
                                    {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    },
                                    onClickBookMarked =
                                    {
                                        viewModel.readMyBookMarks(navController)
                                    }
                                )
                                CommunityChips(pagerstate)
                            }
                        },
                        bottomBar = { MyBottomNavigation(navController) },
                        content = {
                            val isRefreshing by viewModel.isRefreshing.collectAsState()

                            SwipeRefresh(
                                indicatorPadding = PaddingValues(top = 70.dp),
                                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                                onRefresh = { viewModel.refresh() }) {
                                if (viewModel.isApifinished) {
                                    Box(
                                        modifier = Modifier
                                            .padding(it) // topbar 만큼의 패딩을 갖는다  아마 bottombar 만큼의 패딩값도 함께
                                    ) {
                                        CommunityPager(
                                            viewModel = viewModel,
                                            pagerState = pagerstate,
                                            navController = navController
                                        )

                                    }
                                    if (isLoading) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = LinkedInColor)
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = LinkedInColor)
                                    }
                                }
                            }
                        }
                    )
                }
            })
    }
}





