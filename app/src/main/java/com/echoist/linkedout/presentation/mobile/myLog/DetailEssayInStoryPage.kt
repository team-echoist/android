package com.echoist.linkedout.presentation.mobile.myLog

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TYPE_STORY
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.presentation.viewModels.MyLogViewModel
import com.echoist.linkedout.presentation.viewModels.WritingViewModel
import kotlinx.coroutines.delay

@Composable
fun DetailEssayInStoryPage(
    navController: NavController,
    viewModel: MyLogViewModel,
    writingViewModel: WritingViewModel
) {
    val scrollState = rememberScrollState()
    var isClicked by remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                viewModel.readDetailEssay(),
                viewModel.storyEssayNumber
            ) { viewModel.isActionClicked = !viewModel.isActionClicked }
        },
        content = {
            Box(
                Modifier
                    .padding(it)
                    .clickable { isClicked = !isClicked }
                    .fillMaxSize(), contentAlignment = Alignment.TopCenter
            ) {

                Column(Modifier.verticalScroll(scrollState)) {
                    DetailEssay(viewModel = viewModel)
                }
                //수정 옵션
                AnimatedVisibility(
                    visible = viewModel.isActionClicked,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearEasing
                        )
                    )
                ) {
                    ModifyOption(
                        viewModel,
                        navController = navController,
                        writingViewModel = writingViewModel
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                LaunchedEffect(isClicked) {
                    delay(3000)
                    isClicked = false
                }

                AnimatedVisibility(
                    visible = isClicked,
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
                    StoryBottomBar(viewModel.readDetailEssay(), viewModel, navController)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavController,
    essayItem: EssayApi.EssayItem,
    num: Int,
    isModifyClicked: () -> Unit
) {
    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let {
        navController.previousBackStackEntry
    }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route

    androidx.compose.material3.TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "0$num. ", fontSize = 16.sp, color = LinkedInColor)
                Text(text = "${essayItem.title}", fontSize = 16.sp)

            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                tint = Color(0xFF727070),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(30.dp)
                    .clickable {
                        navigateWithClearBackStack(navController, Routes.StoryDetailPage)
                    } //뒤로가기
            )
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .clickable {
                        isModifyClicked()
                    },
            )
        })
}


@Composable
fun StoryBottomBar(
    item: EssayApi.EssayItem,
    viewModel: MyLogViewModel,
    navController: NavController,
) {
    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let { navController.previousBackStackEntry }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route

    var noExistPreviousStack by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = noExistPreviousStack) { //1초 뒤에 이전 조회글 토스트 사라지게.
        delay(1500)
        noExistPreviousStack = false
    }

    Column {
        AnimatedVisibility(
            visible = noExistPreviousStack,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 20.dp)
                    .background(color = Color(0xFF212121), shape = RoundedCornerShape(size = 10.dp))
            ) {
                Text(
                    text = "이전 글이 없습니다.",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFF0E0E0E))
                .fillMaxWidth()
                .height(70.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.height(94.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrowleft),
                            contentDescription = "nav back",
                            Modifier
                                .size(20.dp)
                                .clickable {

                                    if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                        viewModel.detailEssayBackStack.pop()

                                        if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                            viewModel.detailEssay =
                                                viewModel.detailEssayBackStack.peek()
                                            viewModel.setBackDetailEssay(viewModel.detailEssayBackStack.peek()) //detailEssay값을 아예 수정
                                        }
                                    }
                                    navController.popBackStack()
                                }


                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "이전 글", fontSize = 12.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrowright),
                            contentDescription = "next random essay",
                            Modifier
                                .size(20.dp)
                                .clickable {
                                    viewModel.detailEssayBackStack.push(item)
                                    viewModel.readNextEssay(
                                        item.id!!, TYPE_STORY,
                                        navController,
                                        viewModel.getSelectedStory().id!!
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "다음 글", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}