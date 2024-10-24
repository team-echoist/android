package com.echoist.linkedout.presentation.myLog.mylog

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.Story
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.util.INSPECT_POPUP_URL
import com.echoist.linkedout.presentation.util.LINKEDOUT_POPUP_URL
import com.echoist.linkedout.presentation.util.PRIVATE_POPUP_URL
import com.echoist.linkedout.presentation.util.PUBLISHED_POPUP_URL
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TYPE_PRIVATE
import com.echoist.linkedout.presentation.util.TYPE_PUBLISHED
import com.echoist.linkedout.presentation.util.TYPE_STORY
import com.echoist.linkedout.presentation.util.formatDateTime
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import kotlinx.coroutines.delay


@Composable
fun MyLogDetailPage(
    navController: NavController,
    viewModel: MyLogViewModel,
    writingViewModel: WritingViewModel
) {
    val scrollState = rememberScrollState()
    var isClicked by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            DetailTopAppBar(navController = navController, viewModel)
        },
        content = {
            Box(
                Modifier
                    .clickable { isClicked = !isClicked }
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.TopCenter
            ) {

                Column(Modifier.verticalScroll(scrollState)) {
                    DetailEssay(viewModel = viewModel)
                    LastEssayPager(viewModel = viewModel, navController = navController)
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

                    ModifyOption(viewModel, navController = navController, writingViewModel)

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
                    SequenceBottomBar(viewModel.readDetailEssay(), viewModel, navController)
                }
            }
        }
    )
}

@Composable
fun ModifyOption(
    viewModel: MyLogViewModel,
    navController: NavController,
    writingViewModel: WritingViewModel
) {

    var isStoryClicked by remember { mutableStateOf(false) }

    if (isStoryClicked) {
        StoryModifyBox({
            isStoryClicked = false
            viewModel.isActionClicked = false
            viewModel.deleteEssayInStory()
        }, {
            isStoryClicked = false
            viewModel.isActionClicked = false
            viewModel.modifyEssayInStory()
        },
            {
                viewModel.isActionClicked = false
                isStoryClicked = false
            }, viewModel
        )
    }
    if (!isStoryClicked) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 23.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Surface(modifier = Modifier.size(180.dp, 280.dp), shape = RoundedCornerShape(10)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0E0E0E)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.background(Color(0xFF0E0E0E)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.text_minus),
                            contentDescription = "minus",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { viewModel.textSizeDown() }
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(text = "가", fontSize = 24.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.text_plus),
                            contentDescription = "minus",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { viewModel.textSizeUp() }

                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF1A1A1A))
                    OptionItem(text = "수정", Color.White, {
                        writingViewModel.title.value =
                            TextFieldValue(viewModel.readDetailEssay().title!!)
                        writingViewModel.content =
                            viewModel.readDetailEssay().content!!
                        writingViewModel.hashTagList =
                            viewModel.readDetailEssay().tags!!.map { it.name }.toMutableStateList()
                        writingViewModel.latitude = viewModel.readDetailEssay().latitude
                        writingViewModel.longitude = viewModel.readDetailEssay().longitude
                        writingViewModel.locationText = viewModel.readDetailEssay().location ?: ""
                        writingViewModel.imageUrl = viewModel.readDetailEssay().thumbnail
                        writingViewModel.isModifyClicked = true
                        writingViewModel.modifyEssayid = viewModel.readDetailEssay().id!!


                        navController.navigate("WritingPage")
                    }, R.drawable.option_modify)
                    HorizontalDivider(color = Color(0xFF1A1A1A))

                    OptionItem(
                        text = "발행",
                        Color.White,
                        { viewModel.updateEssayToPublished() },
                        R.drawable.option_link
                    )
                    HorizontalDivider(color = Color(0xFF1A1A1A))

                    OptionItem(
                        text = "Linked-out",
                        Color.White,
                        { viewModel.updateEssayToLinkedOut() },
                        R.drawable.option_linkedout
                    )
                    HorizontalDivider(color = Color(0xFF1A1A1A))
                    OptionItem(
                        text = "스토리 선택", Color(0xFF616FED),
                        {
                            isStoryClicked = true
                        }, R.drawable.option_check
                    )
                    HorizontalDivider(color = Color(0xFF1A1A1A))

                    OptionItem(text = "삭제", Color(0xFFE43446), {
                        viewModel.deleteEssay(
                            viewModel.readDetailEssay().id ?: 0
                        )
                        Log.d(TAG, "ModifyOption: dd")
                    }, R.drawable.option_trash)

                }
            }
        }
    }
}

@Composable
fun OptionItem(
    text: String,
    color: Color,
    onClick: () -> Unit,
    iconResource: Int
) {
    Box(
        Modifier
            .background(Color(0xFF0E0E0E), shape = RoundedCornerShape(20))
            .size(180.dp, 44.dp)
            .clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ), contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text, fontSize = 16.sp, color = color)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                tint = color,
                painter = painterResource(id = iconResource),
                contentDescription = "img",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(navController: NavController, viewModel: MyLogViewModel) {

    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let {
        navController.previousBackStackEntry
    }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = { },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                tint = Color(0xFF727070),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .size(30.dp)

                    .clickable {
                        Log.d(TAG, "DetailTopAppBar: $previousRoute")
                        when (previousRoute) {
                            Routes.WritingCompletePage -> navigateWithClearBackStack(
                                navController,
                                "${Routes.Home}/200"
                            )

                            "${Routes.MyLog}/{page}" -> navigateWithClearBackStack(
                                navController,
                                "${Routes.MyLog}/0"
                            )

                            else -> {
                                navController.popBackStack()
                                viewModel.isActionClicked = false

                                if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                    viewModel.detailEssayBackStack.pop()
                                    Log.d(TAG, "pushpushpop: ${viewModel.detailEssayBackStack}")
                                    if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                        viewModel.detailEssay =
                                            viewModel.detailEssayBackStack.peek()
                                    }
                                }
                            }
                        }
                    } //뒤로가기
            )
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "img",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .clickable {
                        viewModel.isActionClicked = !viewModel.isActionClicked
                    },
            )
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay(viewModel: MyLogViewModel) {
    val essay = viewModel.detailEssay
    Log.d(TAG, "DetailEssay: ${essay.status}")
    Box {
        Column {
            if (essay.thumbnail != null && essay.thumbnail!!.startsWith("https")) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp), contentAlignment = Alignment.Center
                ) {
                    GlideImage(
                        model = essay.thumbnail,
                        contentDescription = "essay Thumbnail",
                        contentScale = ContentScale.FillHeight
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = essay.title!!, fontSize = viewModel.titleTextSize, modifier = Modifier)
                Spacer(modifier = Modifier.height(40.dp))

                RichText(
                    state = rememberRichTextState().setHtml(essay.content!!),
                    fontSize = viewModel.contentTextSize,
                    lineHeight = 27.2.sp,
                    color = Color(0xFFB4B4B4)
                )

                Spacer(modifier = Modifier.height(46.dp))
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                    Column {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            (if (essay.author != null) essay.author!!.nickname else "")?.let {
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    color = Color(0xFF686868)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = formatDateTime(essay.createdDate ?: ""),
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF686868)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (essay.linkedOutGauge != null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row {
                                    repeat(essay.linkedOutGauge!!) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ring),
                                            contentDescription = "ring",
                                            modifier = Modifier.size(14.dp),
                                            colorFilter = ColorFilter.tint(Color(0xFF686868))
                                        )
                                        if (it != essay.linkedOutGauge!! - 1) Spacer(
                                            modifier = Modifier.width(
                                                4.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                if (essay.tags != null) {
                    Row {
                        repeat(essay.tags!!.size) {
                            SuggestionChip(
                                onClick = { },
                                label = { Text(essay.tags!![it].name) },
                                shape = RoundedCornerShape(50)
                            )
                            if (it != essay.tags!!.size - 1) Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoryModifyBox(
    isDeleteClicked: () -> Unit,
    isModifyClicked: () -> Unit,
    isBackgroundClicked: () -> Unit,
    viewModel: MyLogViewModel
) {
    val items = viewModel.storyList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isBackgroundClicked() }
            .background(Color.Black.copy(0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(Color(0xFF121212), shape = RoundedCornerShape(10))
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "이 글을 어떤 스토리로 추가/변경 할까요?",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                SingleSelectableList(items, viewModel)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { isDeleteClicked() },
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(
                            text = "스토리에서 삭제",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { isModifyClicked() },
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(start = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616FED)),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "추가/변경", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SingleSelectableList(items: List<Story>, viewModel: MyLogViewModel) {
    // 선택된 항목을 추적하기 위한 상태 변수
    var selectedItem by remember { mutableStateOf(viewModel.findStoryInEssay()) }

    LazyColumn(Modifier.height(250.dp)) {
        items(items) { item ->
            val isSelected = item == selectedItem
            val backgroundColor = Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        // 같은 항목을 클릭하면 선택을 해제, 그렇지 않으면 항목을 선택
                        selectedItem = if (selectedItem == item) {
                            null
                        } else item
                        if (selectedItem != null) {
                            viewModel.setSelectStory(selectedItem!!)
                        }
                    }
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {

                Text(text = item.name)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                    if (isSelected) Icon(
                        painter = painterResource(id = R.drawable.option_check),
                        tint = LinkedInColor,
                        contentDescription = "img"
                    )
                }
            }
        }
    }
}

@Composable
fun CompletedEssayPage(
    navController: NavController,
    viewModel: MyLogViewModel,
    writingViewModel: WritingViewModel
) {
    var hasCalledApi by remember { mutableStateOf(false) }

    // API 호출 및 상태 업데이트
    LaunchedEffect(key1 = Unit) {
        if (!hasCalledApi) {
            viewModel.readMyEssay()
            viewModel.readPublishEssay()
            delay(100)
            hasCalledApi = true
        }
    }

    // 뒤로가기 버튼 처리
    BackHandler(onBack = {
        navigateWithClearBackStack(navController, "${Routes.Home}/200")
    })

    val scrollState = rememberScrollState()

    // 상태에 따라 UI를 분기
    if (hasCalledApi) {
        Scaffold(
            topBar = {
                DetailTopAppBar(navController = navController, viewModel)
            },
            content = {
                Box(
                    Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxSize()
                    ) {
                        DetailEssay2(viewModel = viewModel)
                        LastEssayPager(viewModel = viewModel, navController = navController)
                    }

                    // 수정 옵션
                    AnimatedVisibility(
                        visible = viewModel.isActionClicked,
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
                        ModifyOption(
                            viewModel,
                            navController = navController,
                            writingViewModel = writingViewModel
                        )
                    }

                    // 상태에 따라 텍스트 표시
                    val text = when (viewModel.readDetailEssay().status) {
                        "private" -> "저장"
                        "published" -> "발행"
                        "linkedout" -> "링크드아웃"
                        else -> "검토중"
                    }
                    WriteCompleteBox(type = text)
                }
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay2(viewModel: MyLogViewModel) {
    val essay = viewModel.readDetailEssay()
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (essay.thumbnail != null && essay.thumbnail!!.startsWith("https")) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp), contentAlignment = Alignment.Center
                ) {
                    GlideImage(
                        model = essay.thumbnail,
                        contentDescription = "essay Thumbnail",
                        contentScale = ContentScale.FillHeight
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = essay.title!!, fontSize = viewModel.titleTextSize, modifier = Modifier)
                Spacer(modifier = Modifier.height(40.dp))

                RichText(
                    state = rememberRichTextState().setHtml(essay.content!!),
                    fontSize = viewModel.contentTextSize,
                    modifier = Modifier,
                    color = Color(0xFFB4B4B4)
                )

                Spacer(modifier = Modifier.height(46.dp))
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                    Column {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            (if (essay.author != null) essay.author!!.nickname else "")?.let {
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    color = Color(0xFF686868)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = formatDateTime(essay.createdDate ?: ""),
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF686868)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (essay.linkedOutGauge != null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row {
                                    repeat(essay.linkedOutGauge!!) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ring),
                                            contentDescription = "ring",
                                            modifier = Modifier.size(14.dp),
                                            colorFilter = ColorFilter.tint(Color(0xFF686868))
                                        )
                                        if (it != essay.linkedOutGauge!! - 1) Spacer(
                                            modifier = Modifier.width(
                                                4.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                if (essay.tags != null) {
                    Row {
                        repeat(essay.tags!!.size) {
                            SuggestionChip(
                                onClick = { },
                                label = { Text(essay.tags!![it].name) },
                                shape = RoundedCornerShape(50)
                            )
                            if (it != essay.tags!!.size - 1) Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WriteCompleteBox(type: String) {
    var isVisible by remember {
        mutableStateOf(true)
    }
    val text = when (type) {
        "저장" -> "아무개님의 새 글이\n" +
                "'나만의 글'에 저장됐어요!"

        "발행" -> "아무개님의 새 글이\n" +
                "숨바꼭질을 시작했어요!"

        "링크드아웃" -> "아무개님의 새 글이\n숨바꼭질을 시작했어요!"
        "검토중" -> "아무개님의 새 글을\n꼼꼼하게 검토중이에요"
        else -> {
            ""
        }
    }

    val imageUrl = when (type) {
        "저장" -> PRIVATE_POPUP_URL
        "발행" -> PUBLISHED_POPUP_URL
        "링크드아웃" -> LINKEDOUT_POPUP_URL
        "검토중" -> INSPECT_POPUP_URL
        else -> {
            ""
        }
    }

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f))
        ) {
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(300.dp, 286.dp)) {
                GlideImage(
                    model = R.drawable.box_complete,
                    contentDescription = "completeBox",
                    modifier = Modifier.fillMaxSize()
                )
                Row(
                    Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 50.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GlideImage(
                        model = imageUrl, contentDescription = "", modifier = Modifier
                            .weight(2f)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(4f)
                    ) {
                        Row {
                            Text(
                                text = "$type ",
                                color = LinkedInColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(text = "완료", fontSize = 24.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Button(
                        onClick = {
                            isVisible = false
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(containerColor = LinkedInColor)
                    ) {
                        Text(text = "닫기", fontSize = 14.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun SequenceBottomBar(
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
                                    val type = when (item.status) {
                                        "private" -> TYPE_PRIVATE
                                        "published" -> TYPE_PUBLISHED
                                        else -> TYPE_STORY
                                    }
                                    viewModel.readNextEssay(
                                        item.id!!,
                                        type,
                                        if (item.status == TYPE_STORY) viewModel.getSelectedStory().id!! else 0
                                    )
                                    if (type == TYPE_STORY) navController.navigate(Routes.DetailEssayInStoryPage)
                                    else navController.navigate(Routes.MyLogDetailPage)
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