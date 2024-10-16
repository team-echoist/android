package com.echoist.linkedout.presentation.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun TabletCommunityRoute(
    navController: NavController,
    viewModel: CommunityViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val pagerstate = rememberPagerState { 1 } //todo 다음버전에 구독페이지 추가하기. pagerstate 2로 수정
    val color = if (pagerstate.currentPage == 0) Color(0xFFD9D9D9) else Color.Black

    if (pagerstate.currentPage == 1) {
        viewModel.isClicked = false
    }
    val isLoading by viewModel.isLoading.collectAsState()

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