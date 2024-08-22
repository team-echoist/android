package com.echoist.linkedout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.components.CommunityChips
import com.echoist.linkedout.components.CommunityPager
import com.echoist.linkedout.page.community.SearchingPage
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun TabletCommunityRoute(
    navController: NavController,
    viewModel: CommunityViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
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
                        SearchingPage(drawerState, navController)
                        // ...other drawer items
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    val isRefreshing by viewModel.isRefreshing.collectAsState()

                    Column(modifier = modifier.background(color)) {
                        CommunityChips(pagerstate)
                        SwipeRefresh(
                            indicatorPadding = PaddingValues(top = 70.dp),
                            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                            onRefresh = { viewModel.refresh() }) {
                            if (viewModel.isApifinished) {
                                Box {
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
                }
            }
        )
    }
}