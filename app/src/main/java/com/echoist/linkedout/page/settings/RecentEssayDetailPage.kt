package com.echoist.linkedout.page.settings

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.TYPE_RECOMMEND
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.formatElapsedTime
import com.echoist.linkedout.page.community.CommunityTopAppBar
import com.echoist.linkedout.page.community.DetailEssay
import com.echoist.linkedout.page.community.ReportComplete
import com.echoist.linkedout.page.community.ReportMenuBottomSheet
import com.echoist.linkedout.page.community.ReportOption
import com.echoist.linkedout.page.community.SequenceBottomBar
import com.echoist.linkedout.page.community.SubscriberSimpleItem
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentEssayDetailPage(navController: NavController, viewModel: CommunityViewModel) {

    val scope = rememberCoroutineScope()


    val peekHeight = if (viewModel.isReportClicked) 310.dp else 0.dp


    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    var isClicked by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.isReportClicked) {
        if (viewModel.isReportClicked) {
            bottomSheetState.partialExpand()
        }
    }
    LinkedOutTheme {
        BottomSheetScaffold(
            sheetContainerColor = Color(0xFF191919),
            scaffoldState = scaffoldState,
            sheetContent = {

                //신고하기 요청보냄.
                if (!viewModel.isReportCleared) {
                    ReportMenuBottomSheet(viewModel)

                } else { //신고 하기 버튼 눌렀을때 제대로 요청이 들어가고 접수가되었다면. 완료버튼 클릭시
                    ReportComplete {
                        viewModel.isReportCleared = false
                        viewModel.isReportClicked = false
                        viewModel.isOptionClicked = false
                        scope.launch {
                            bottomSheetState.hide()
                        }

                    }
                }


            },

            sheetPeekHeight = peekHeight
        ) {
            Scaffold(
                topBar = {

                    CommunityTopAppBar(navController = navController, viewModel, Color(0xFF0E0E0E))

                },
                content = {
                    Box(
                        Modifier
                            .clickable { isClicked = !isClicked }
                            .padding(it)
                            .fillMaxSize(), contentAlignment = Alignment.TopCenter
                    ) {
                        LazyColumn {
                            item {

                                DetailEssay(
                                    item = viewModel.readDetailEssay(),
                                    viewModel,
                                    navController
                                )
                                Spacer(modifier = Modifier.height(28.dp))
                                val userinfo = viewModel.readDetailEssay().author ?: UserInfo()

                                SubscriberSimpleItem(
                                    item = userinfo,
                                    viewModel = viewModel,
                                    navController = navController
                                )
                                Spacer(modifier = Modifier.height(36.dp))
                                Box(
                                    modifier = Modifier
                                        .height(12.dp)
                                        .fillMaxWidth()
                                        .background(Color(0xFF1A1A1A))
                                )
                                Box(
                                    modifier = Modifier
                                        .height(56.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterStart
                                )
                                {
                                    Text(
                                        text = "최근 본 다른 글",
                                        fontSize = 14.sp,
                                        color = Color(0xFF616FED)
                                    )

                                }
                            }
                            //todo 글쓴이의 최근 본 리스트중 다른글 띄우기
                            items(items = viewModel.getFilteredRecentEssayList()) { it -> //랜덤리스트 말고 수정할것. 그사람의 리스트로
                                RecentEssayListItem(
                                    item = it,
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }


                        }

                        //신고 옵션
                        AnimatedVisibility(
                            visible = viewModel.isOptionClicked,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 1000,
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 23.dp),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                ReportOption({
                                    viewModel.isReportClicked = !viewModel.isReportClicked
                                }, viewModel)
                            }
                        }

                    }

                    Box(
                        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
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
                            SequenceBottomBar(viewModel.readDetailEssay(), viewModel, navController)
                        }
                    }
                    if (viewModel.isReportClicked)
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.7f))
                            .clickable { viewModel.isReportClicked = false })

                }

            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecentEssayListItem(
    item: EssayApi.EssayItem,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    val color =
        Color.White
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)
        .clickable {
            viewModel.readDetailRecentEssay(item.id!!, navController, TYPE_RECOMMEND)
            //navigate
        }
        .height(140.dp)) {
        //타이틀
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(7f)
                    .padding(top = 0.dp, start = 20.dp, end = 20.dp)// Column 비율 조정
            ) {
                Row(
                    Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = item.title!!,
                        color = color,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(text = "   • ${formatElapsedTime(item.createdDate!!)}", fontSize = 10.sp, color = Color(0xFF686868))

                }


                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.content!!,
                    lineHeight = 27.2.sp,
                    maxLines = 2,
                    color = color,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.thumbnail != null) {
                GlideImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                        .weight(3f) // 이미지 비율 조정
                )
            }
        }


        Box(
            contentAlignment = Alignment.BottomStart, modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 10.dp)
        ) {
            Text(text = if (item.author?.nickname != null) item.author!!.nickname!! else "", fontSize = 10.sp, color = Color(0xFF686868))
        }
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalDivider(color = Color(0xFF686868))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){

        }


    }
}