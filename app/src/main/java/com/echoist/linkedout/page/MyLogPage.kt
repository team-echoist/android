package com.echoist.linkedout.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.components.EssayChips
import com.echoist.linkedout.components.EssayPager
import com.echoist.linkedout.components.ModifyStoryBox
import com.echoist.linkedout.components.MyLogTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.SearchingViewModel
import kotlinx.coroutines.launch


@Composable
fun MyLogPage(navController : NavController,viewModel: MyLogViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val pagerstate = rememberPagerState { 3 }
    val hasCalledApi = remember { mutableStateOf(false) }

    if (!hasCalledApi.value) {
        viewModel.readMyEssay()
        viewModel.readPublishEssay()
        viewModel.readMyStory()

        hasCalledApi.value = true
    }
    //todo searchingviewmodel 은 서칭페이지 한정, 데이터공유가 필요하지않기때문에 컴포즈 내에서만 생성하여 사용함.
    val searchingViewModel : SearchingViewModel = hiltViewModel()


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

                                    MyLogTopAppBar() {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }

                                    EssayChips(pagerstate, viewModel)
                                }
                            },
                            bottomBar = { MyBottomNavigation(navController) },
                            floatingActionButton = { WriteFTB(navController) },
                            content = {
                                Box(Modifier.padding(it)) {
                                    EssayPager(
                                        pagerState = pagerstate,
                                        viewModel,
                                        navController = navController
                                    )
                                }
                            }
                        )

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
