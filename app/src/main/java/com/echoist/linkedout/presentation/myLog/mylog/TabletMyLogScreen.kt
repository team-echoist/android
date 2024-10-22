package com.echoist.linkedout.presentation.myLog.mylog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.util.isPortrait
import kotlinx.coroutines.launch

@Composable
fun TabletMyLogRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyLogViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel = hiltViewModel(),
    page: Int = 0
) {
    val pagerState = rememberPagerState { 3 }
    val hasCalledApi = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        pagerState.animateScrollToPage(page)
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

    TabletMyLogScreen(
        modifier = modifier,
        pagerState = pagerState,
        navController = navController,
        writingViewModel = writingViewModel,
        viewModel = viewModel
    )
}

@Composable
internal fun TabletMyLogScreen(
    modifier: Modifier,
    pagerState: PagerState,
    navController: NavController,
    writingViewModel: WritingViewModel,
    viewModel: MyLogViewModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (isPortrait()) 90.dp else 180.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabletMyLogTabView(
            pagerState = pagerState,
            viewModel = viewModel
        )
        ContentSection(
            modifier = modifier,
            pagerState = pagerState,
            viewModel = viewModel,
            navController = navController,
            writingViewModel = writingViewModel
        )
    }
    ModifyStoryOverlay(viewModel, navController)
}

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    viewModel: MyLogViewModel,
    navController: NavController,
    writingViewModel: WritingViewModel
) {
    Box(modifier = modifier) {
        EssayPager(
            pagerState = pagerState,
            viewModel = viewModel,
            navController = navController,
            writingViewModel = writingViewModel
        )
    }
}

@Composable
fun TabletMyLogTabView(pagerState: PagerState, viewModel: MyLogViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        TabItem(
            text = "나만의 글 ${viewModel.myEssayList.size}",
            isSelected = pagerState.currentPage == 0,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            },
            color = if (pagerState.currentPage == 0) Color.White else Color.Gray,
            modifier = Modifier.weight(1f)
        )
        TabItem(
            text = "발행한 글 ${viewModel.publishedEssayList.size}",
            isSelected = pagerState.currentPage == 1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            },
            color = if (pagerState.currentPage == 1) Color.White else Color.Gray,
            modifier = Modifier.weight(1f)
        )
        TabItem(
            text = "스토리 ${viewModel.storyList.size}",
            isSelected = pagerState.currentPage == 2,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(2)
                }
            },
            color = if (pagerState.currentPage == 2) Color.White else Color.Gray,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(end = 12.dp)
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 14.sp,
            text = text,
            color = color,
            modifier = Modifier.clickable { onClick() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isSelected) {
            HorizontalDivider(
                modifier = modifier.fillMaxWidth(),
                color = color,
                thickness = 2.dp
            )
        }
    }
}

@Composable
fun ModifyStoryOverlay(viewModel: MyLogViewModel, navController: NavController) {
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