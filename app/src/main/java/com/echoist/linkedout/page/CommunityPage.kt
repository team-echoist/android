package com.echoist.linkedout.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.echoist.linkedout.components.ChoiceBox
import com.echoist.linkedout.components.CommunityChips
import com.echoist.linkedout.components.CommunityPager
import com.echoist.linkedout.components.CommunityTopAppBar
import com.echoist.linkedout.components.SearchingBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel
import kotlinx.coroutines.launch


//@Preview
//@Composable
//fun Prev() {
//    val viewModel : CommunityViewModel = viewModel()
//    CommunityPage(rememberNavController(), viewModel)
//}

@Composable
fun CommunityPage(navController: NavController, viewModel: CommunityViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val pagerstate = rememberPagerState { 2 }
    val hasCalledApi = remember { mutableStateOf(false) }
    val color = if (pagerstate.currentPage == 0) Color(0xFFD9D9D9) else Color.Black

    //화면 새로 생길때 한번씩만 호출되게끔
    if (!hasCalledApi.value) {
        viewModel.readRandomEssays()
        viewModel.readFollowingEssays()
        viewModel.readOneSentences("first")
        viewModel.readOneSentences("last")
        hasCalledApi.value = true
    }

    if (pagerstate.currentPage == 1){
        viewModel.isClicked = false
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent =  {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    ModalDrawerSheet(
                        modifier = Modifier.fillMaxSize(),
                        drawerShape = RectangleShape,
                        drawerContainerColor = Color.Black
                    ) {
                        SearchingBar(viewModel = viewModel,{
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },drawerState)


                        // ...other drawer items
                    }
                }
            } ,
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr){

                LinkedOutTheme {
                    Scaffold(
                        modifier = Modifier.background(color),
                        topBar = {
                            Column(Modifier.background(color)) {
                                CommunityTopAppBar("커뮤니티", pagerstate){
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                                CommunityChips(pagerstate)
                            }
                        },
                        bottomBar = { MyBottomNavigation(navController) },
                        content = {
                            Box(modifier = Modifier.padding(it))

                            Box(modifier = Modifier.padding(top = 50.dp, bottom = 80.dp)) {
                                CommunityPager(
                                    viewModel = viewModel,
                                    pagerState = pagerstate,
                                    navController = navController
                                )

                            }
                            AnimatedVisibility(
                                visible = viewModel.isClicked,
                                enter = fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = FastOutSlowInEasing
                                    )
                                ),
                                exit = fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = LinearEasing
                                    )
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 155.dp, end = 18.dp),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    ChoiceBox(viewModel)
                                }
                            }

                        }
                    )
                }
            } }
        )
    }
}




