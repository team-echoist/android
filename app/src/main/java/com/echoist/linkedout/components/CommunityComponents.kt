package com.echoist.linkedout.components

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.SentenceInfo
import kotlinx.coroutines.launch
import me.saket.extendedspans.ExtendedSpans
import me.saket.extendedspans.RoundedCornerSpanPainter
import me.saket.extendedspans.drawBehind

@Composable
fun RandomSentences(viewModel: CommunityViewModel,navController: NavController) {
    val oneSentenceList by if (viewModel.sentenceInfo == SentenceInfo.First) viewModel.firstSentences.collectAsState() else viewModel.lastSentences.collectAsState()


    //한줄추천 받아온 리스트값이 비어있으면 안보여줌
    if (oneSentenceList.isNotEmpty()){

        val annotatedString = remember(viewModel.sentenceInfo, oneSentenceList) {
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            //background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[0].content}      ")
                        Log.d(TAG, "RandomSentences: ${oneSentenceList[0].content}")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            //background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[1].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence3")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            //background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[2].content}      ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            //background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[3].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            //background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append(" ${oneSentenceList[4].content} ")

                    }
                    pop()
                }
            }
        }
        Log.d(TAG, "RandomSentences: ${annotatedString.text}")



        val extendedSpans = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = 0.sp,
                    bottomMargin = 0.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }

        Column {
            Box(
                modifier = Modifier
                    .padding(start = 10.dp, end = 6.dp), contentAlignment = Alignment.Center

            ) {
                var layoutResult: TextLayoutResult? = remember { null }
                Text(
                    text = remember(annotatedString) {
                        extendedSpans.extend(annotatedString)
                    },
                    modifier = Modifier
                        .horizontalScroll(state = rememberScrollState())
                        // .fillMaxWidth()
                        .drawBehind(extendedSpans)
                        .pointerInput(Unit) {
                            detectTapGestures { offsetPosition ->
                                layoutResult?.let {
                                    val position = it.getOffsetForPosition(offsetPosition)
                                    annotatedString
                                        .getStringAnnotations(
                                            tag = "SentenceTag",
                                            start = position,
                                            end = position
                                        )
                                        .firstOrNull()
                                        ?.let { annotation ->
                                            when (annotation.item) {
                                                "Sentence1" -> viewModel.readDetailEssay(
                                                    oneSentenceList[0].id!!,
                                                    navController = navController
                                                )

                                                "Sentence2" -> viewModel.readDetailEssay(
                                                    oneSentenceList[1].id!!,
                                                    navController = navController
                                                )

                                                "Sentence3" -> viewModel.readDetailEssay(
                                                    oneSentenceList[2].id!!,
                                                    navController = navController
                                                )

                                                "Sentence4" -> viewModel.readDetailEssay(
                                                    oneSentenceList[3].id!!,
                                                    navController = navController
                                                )

                                                "Sentence5" -> viewModel.readDetailEssay(
                                                    oneSentenceList[4].id!!,
                                                    navController = navController
                                                )
                                            }
                                        }
                                }
                            }
                        },
                    onTextLayout = {
                        layoutResult = it
                        extendedSpans.onTextLayout(it)
                    }
                )


            }
        }
    }


}
@Composable
fun ChoiceBox(viewModel: CommunityViewModel) {
    val color = Color.Black
    Card(
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFCFCFCF)),
        modifier = Modifier.size(
            104.dp,
            70.dp
        )
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "첫 문장",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        viewModel.isClicked = false
                        viewModel.sentenceInfo = SentenceInfo.First
                    },
                fontSize = 12.sp,
                color = color
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(Modifier.fillMaxWidth(), color = Color(0xFFC5C5C5))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "마지막 문장",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        viewModel.isClicked = false
                        viewModel.sentenceInfo = SentenceInfo.Last

                    },
                fontSize = 12.sp,
                color = color
            )

        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SentenceChoice(viewModel: CommunityViewModel) {
    val color = Color.Black

    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
            .fillMaxWidth()
            .height(82.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "한 문장을 모아", fontWeight = FontWeight.SemiBold, color = color)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "글의 시작을 알리는 문장들을 만나보세요.", color = Color(0xFF696969), fontSize = 12.sp)

            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFFCFCFCF), // 배경색 설정
                        shape = RoundedCornerShape(10.dp) // 원하는 radius 값 설정
                    )
                    .clickable {
                        viewModel.isClicked = !viewModel.isClicked
                    }
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val arrow = if (viewModel.isClicked) R.drawable.arrowup else R.drawable.arrowdown


                AnimatedContent(
                    targetState = viewModel.sentenceInfo,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    }, label = ""
                ) { targetState ->
                    Text(
                        fontSize = 12.sp,
                        text = if (targetState == SentenceInfo.First) "첫 문장" else "마지막 문장",
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(23.dp))


                Crossfade(
                    targetState = arrow,
                    animationSpec = tween(durationMillis = 200), label = ""
                ) { currentArrow ->
                    Icon(
                        painter = painterResource(id = currentArrow),
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp),
                        tint = Color.Black
                    )
                }

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityTopAppBar(
    text: String,
    pagerState: PagerState,
    onSearchClick: () -> Unit,
    onClickBookMarked: () -> Unit
) {
    val color = if (pagerState.currentPage == 0) Color.Black else Color.White


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = text, fontWeight = FontWeight.Bold, color = color)
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .clickable { onSearchClick() },
                tint = color
            )
            Spacer(modifier = Modifier.width(13.dp))
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "",
                Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
                    .clickable { onClickBookMarked() },
                tint = color
            )


        }
    )
}


@Composable
fun CommunityChips(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val color = if (pagerState.currentPage == 0) Color.Black else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp), thickness = 1.dp, color = Color(0xFFAEAEAE)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 17.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp, 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = color,
                    fontSize = 14.sp,
                    text = "랜덤",// 색상을 먼저 적용합니다
                    modifier = Modifier.clickable {

                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }

                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (pagerState.currentPage == 0) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(65.dp),
                        color = color,
                        thickness = 2.dp
                    )
                }

            }
            //todo 구독페이지 다음버전 출시예정 pagerstate 2로 수정하고 주석해제.
//            Column(
//                modifier = Modifier
//                    .padding(end = 12.dp)
//                    .size(60.dp, 40.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    color = color,
//                    fontSize = 14.sp,
//                    text = "구독",// 색상을 먼저 적용합니다
//                    modifier = Modifier.clickable {
//
//                        coroutineScope.launch {
//                            pagerState.animateScrollToPage(1)
//                        }
//
//                    } // Modifier.clickable을 마지막에 적용합니다
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//                if (pagerState.currentPage == 1) {
//                    HorizontalDivider(
//                        modifier = Modifier
//                            .width(65.dp),
//                        color = color,
//                        thickness = 3.dp
//                    )
//                }
//            }

        }

    }
}

@Preview
@Composable
fun TodaysLogTitle() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(95.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "오늘의 글",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF616FED)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "오늘 쓰여진 다양하고 솔직한 글들을 읽어보세요", fontSize = 12.sp, color = Color(0xFF696969))
            Spacer(modifier = Modifier.height(22.dp))


        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EssayListItem(
    item: EssayApi.EssayItem,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    val color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)
        .clickable {
            Log.d(TAG, "EssayListItem: ${item.id}")
            viewModel.readDetailEssay(item.id!!, navController)
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
                    if (item.status == "linkedout"){
                        Icon(painter = painterResource(id = R.drawable.option_linkedout), contentDescription = "linkedout icon", modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(6.dp))

                    }

                    Text(
                        modifier = Modifier,
                        text = item.title!!,
                        color = color,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(text = "   • 10 분", fontSize = 10.sp, color = Color(0xFF686868))

                }


                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.content!!,
                    maxLines = 2,
                    lineHeight = 27.2.sp,
                    color = color,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.thumbnail != null) {
                GlideImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(135.dp)
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        //.weight(3f) // 이미지 비율 조정
                        .clip(RoundedCornerShape(10.dp)) // 둥근 모서리 적용
                )
            }
        }


        Box(
            contentAlignment = Alignment.BottomStart, modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 10.dp)
        ) {
            Text(
                text = if (item.author?.nickname != null) item.author!!.nickname!! else "",
                fontSize = 10.sp,
                color = Color(0xFF686868)
            )
        }
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalDivider(color = Color(0xFF333333))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){

        }


    }
}

@Composable
fun RandomCommunityPage(viewModel: CommunityViewModel, navController: NavController) {
    val randomList by viewModel.randomList.collectAsState()

    val listState = rememberLazyListState()


    LaunchedEffect(listState) {
        // 스크롤 상태를 감지하는 LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                // 리스트의 마지막 아이템에 도달하면
                if (lastVisibleItem?.index == viewModel.randomEssayList.size - 1) {
                    viewModel.readRandomEssays()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .background(Color(0xFFD9D9D9))
            .padding(top = 20.dp)
    ) {
        item {
            Column {
                SentenceChoice(viewModel)
                RandomSentences(viewModel,navController)
                Spacer(modifier = Modifier.height(20.dp))

                TodaysLogTitle()

            }
        }
        items(viewModel.randomEssayList) { it ->
            EssayListItem(item = it, viewModel = viewModel, navController = navController)
        }


    }

}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SubscribeUserItem(item: UserInfo, viewModel: CommunityViewModel) {
    val background by animateColorAsState(
        if (viewModel.currentClickedUserId == item.id) Color(
            0xFF222222
        ) else Color.Transparent, label = ""
    )
    val backgroundTrans by animateColorAsState(
        if (viewModel.currentClickedUserId == item.id) Color.White else Color.White.copy(alpha = 0.4f),
        label = ""  //todo white값을 unspecified.0.4f로 맞추면될듯
    )

    CompositionLocalProvider(LocalRippleConfiguration provides null) {

        Box(
            modifier = Modifier
                .width(70.dp)
                .background(background, shape = RoundedCornerShape(25))
                .padding(top = 20.dp)
                .clickable {
                    if (viewModel.currentClickedUserId == item.id) {
                        viewModel.currentClickedUserId = null
                    } else {
                        viewModel.currentClickedUserId = item.id
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp),
                    color = backgroundTrans // Unspecified
                ) {
                    GlideImage(
                        model = item.profileImage,
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
                Text(text = item.nickname!!, fontSize = 12.sp, color = backgroundTrans)
            }
        }
    }
}

@Composable
fun SubscribeUserList(viewModel: CommunityViewModel,navController: NavController) {
    Row(
        modifier = Modifier.background(Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LazyRow(Modifier.weight(9f)) { // todo subscribeList 받아와야할것
            items(viewModel.subscribeUserList) { it ->
                SubscribeUserItem(item = it, viewModel = viewModel)
            }
        }
        Text(text = "전체", modifier = Modifier
            .weight(1f)
            .clickable {
                navController.navigate("FullSubscriberPage")
            })
    }


}

@Preview
@Composable
fun prev() {
    val viewModel : CommunityViewModel = viewModel()
    Column {
        SubscribeUserItem(item = viewModel.userItem, viewModel = viewModel)
        EssayListItem(
            item = viewModel.detailEssay,
            viewModel = viewModel,
            navController = rememberNavController()
        )
        SubscribeUserList(viewModel = viewModel, navController = rememberNavController())
    }
}

@Composable
fun SubscribePage(
    viewModel: CommunityViewModel,
    navController: NavController,
    pagerstate: PagerState
) {
    val color = if (pagerstate.currentPage == 0) Color(0xFFD9D9D9) else Color.Black

    LazyColumn(
        Modifier
            .background(color)
            .padding(top = 40.dp)
    ) {
        item {
            Column {
                SubscribeUserList(viewModel,navController)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, end = 20.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    androidx.compose.animation.AnimatedVisibility(visible = viewModel.currentClickedUserId != null) {
                        Box(modifier = Modifier
                            .clickable {
                                viewModel.findUser()
                                navController.navigate("SubscriberPage")
                            }
                            .background(Color(0xFF191919), shape = RoundedCornerShape(20))) {
                            Text(
                                text = "프로필 보기",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
                            )
                        }
                    }
                }

            }

        }
        items(viewModel.followingList) { it ->
            EssayListItem(item = it, viewModel = viewModel, navController = navController)
        }


    }
}

@Composable
fun CommunityPager(
    pagerState: PagerState,
    viewModel: CommunityViewModel,
    navController: NavController
) {

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> RandomCommunityPage(viewModel, navController)
            1 -> SubscribePage(viewModel, navController, pagerState)
        }
    }
}







