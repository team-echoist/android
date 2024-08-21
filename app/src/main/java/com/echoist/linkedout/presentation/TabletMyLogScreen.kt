package com.echoist.linkedout.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.components.EssayChips
import com.echoist.linkedout.components.EssayPager
import com.echoist.linkedout.components.Essaychip
import com.echoist.linkedout.components.ModifyStoryBox
import com.echoist.linkedout.components.MyLogTopAppBar
import com.echoist.linkedout.page.community.SearchingPage
import com.echoist.linkedout.page.home.MyBottomNavigation
import com.echoist.linkedout.page.home.WriteFTB
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TabletMyLogRoute(
    navController: NavController,
    viewModel: MyLogViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel = hiltViewModel(),
    page: Int = 0
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val pagerState = rememberPagerState { 3 }
    val hasCalledApi = remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()
    val configuration = LocalConfiguration.current
    val contentPadding =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 250 else 100

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
        drawerState = drawerState,
        pagerState = pagerState,
        contentPadding = contentPadding,
        navController = navController,
        homeViewModel = homeViewModel,
        writingViewModel = writingViewModel,
        viewModel = viewModel,
        scope = scope
    )
}

@Composable
internal fun TabletMyLogScreen(
    drawerState: DrawerState,
    pagerState: PagerState,
    contentPadding: Int,
    navController: NavController,
    homeViewModel: HomeViewModel,
    writingViewModel: WritingViewModel,
    viewModel: MyLogViewModel,
    scope: CoroutineScope
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(navController, drawerState)
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    LinkedOutTheme {
                        Scaffold(
                            topBar = {
                                TopBarSection(
                                    scope = scope,
                                    drawerState = drawerState,
                                    contentPadding = contentPadding,
                                    viewModel = viewModel,
                                    pagerState = pagerState,
                                    navController = navController
                                )
                            },
                            bottomBar = { MyBottomNavigation(navController) },
                            floatingActionButton = {
                                WriteFTB(navController, homeViewModel, writingViewModel)
                            },
                            content = { padding ->
                                ContentSection(
                                    pagerState = pagerState,
                                    padding = padding,
                                    contentPadding = contentPadding,
                                    viewModel = viewModel,
                                    navController = navController,
                                    writingViewModel = writingViewModel
                                )
                            }
                        )
                        ModifyStoryOverlay(viewModel, navController)
                    }
                }
            }
        )
    }
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        ModalDrawerSheet(
            modifier = Modifier.fillMaxSize(),
            drawerShape = RectangleShape,
            drawerContainerColor = Color.Black
        ) {
            SearchingPage(drawerState = drawerState, navController = navController)
        }
    }
}

@Composable
fun TopBarSection(
    scope: CoroutineScope,
    drawerState: DrawerState,
    contentPadding: Int,
    viewModel: MyLogViewModel,
    pagerState: PagerState,
    navController: NavController
) {
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
            viewModel.isExistUnreadAlerts
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(10.dp))
        TabletMyLogTabView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = contentPadding.dp),
            pagerState = pagerState,
            viewModel = viewModel
        )
    }
}

@Composable
fun ContentSection(
    pagerState: PagerState,
    padding: PaddingValues,
    contentPadding: Int,
    viewModel: MyLogViewModel,
    navController: NavController,
    writingViewModel: WritingViewModel
) {
    Box(
        Modifier
            .padding(padding)
            .padding(horizontal = contentPadding.dp)
    ) {
        EssayPager(
            pagerState = pagerState,
            viewModel = viewModel,
            navController = navController,
            writingViewModel = writingViewModel
        )
    }
}

@Composable
fun TabletMyLogTabView(modifier: Modifier, pagerState: PagerState, viewModel: MyLogViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.height(30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp) // 탭 사이 간격을 5dp로 설정
        ) {
            TabItem(
                text = "나만의 글 ${viewModel.myEssayList.size}",
                dividerWidth = 75.dp,
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
                dividerWidth = 75.dp,
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
                dividerWidth = 75.dp,
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
}

@Composable
fun TabItem(
    text: String,
    dividerWidth: Dp,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(end = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 14.sp,
            text = text,
            color = color,
            modifier = Modifier.clickable { onClick() }
        )
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            modifier = Modifier.width(dividerWidth),
            color = color,
            thickness = 2.dp
        )
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