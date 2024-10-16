package com.echoist.linkedout.presentation.essay.detail

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.community.EssayListItem
import com.echoist.linkedout.presentation.community.SubscriberSimpleItem
import com.echoist.linkedout.presentation.myLog.mylog.OptionItem
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TYPE_RECOMMEND
import com.echoist.linkedout.presentation.util.formatDateTime
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailPage(navController: NavController, viewModel: CommunityViewModel) {

    val scope = rememberCoroutineScope()

    val peekHeight = if (viewModel.isReportClicked) 310.dp else 0.dp

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    var isClicked by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    // 스크롤 상태 감시
    val isScrolling = listState.isScrollInProgress
    var color by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(isScrolling) {
        if (isScrolling) {
            // 스크롤이 진행 중일 때 수행할 작업
            color = Color(0xFF0E0E0E)
        } else {
            // 스크롤이 멈췄을 때 수행할 작업
            color = Color.Black
            Log.d("ScrollStateCheckExample", "스크롤이 멈췄습니다.")
        }
    }

    LaunchedEffect(viewModel.isReportClicked) {
        if (viewModel.isReportClicked) {
            bottomSheetState.partialExpand()
        }
        viewModel.isOptionClicked = false
    }

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

                CommunityTopAppBar(navController = navController, viewModel, color = color)

            },
            content = {
                Box(
                    Modifier
                        .clickable { isClicked = !isClicked }
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.TopCenter
                ) {
                    LazyColumn(state = listState) {
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
                                    text = "다른 글",
                                    fontSize = 14.sp,
                                    color = Color(0xFF616FED)
                                )

                            }
                        }
                        items(items = viewModel.readAnotherEssays()) { it -> //랜덤리스트 말고 수정할것. 그사람의 리스트로
                            EssayListItem(
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
                if (viewModel.isReportClicked)
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable { viewModel.isReportClicked = false })

            }

        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityTopAppBar(navController: NavController, viewModel: CommunityViewModel, color: Color) {
    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let {
        navController.previousBackStackEntry
    }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = color),
        title = { },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)

                    .clickable {


//searching은 그냥 뒤로가기 해야 서칭페이지로 나감
                        if (previousRoute == Routes.CommunityDetailPage) {
                            navController.navigate(Routes.Community)
                        } else {
                            navController.popBackStack()
                        }
//                        if (viewModel.detailEssayBackStack.isNotEmpty()) {
//                            viewModel.detailEssayBackStack.pop()
//                            Log.d(
//                                ContentValues.TAG,
//                                "pushpushpop: ${viewModel.detailEssayBackStack}"
//                            )
//                            if (viewModel.detailEssayBackStack.isNotEmpty()) {
//                                viewModel.detailEssay = viewModel.detailEssayBackStack.peek()
//                                viewModel.setBackDetailEssay(viewModel.detailEssayBackStack.peek()) //detailEssay값을 아예 수정
//                            }
//                        }
//                        navController.popBackStack()

                    } //뒤로가기
            )
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "more",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp)
                    .clickable {
                        viewModel.isOptionClicked = !viewModel.isOptionClicked
                    },
            )
        })
}

@Composable
fun ReportOption(onClickReport: () -> Unit, viewModel: CommunityViewModel) {
    Surface(modifier = Modifier.size(180.dp, 110.dp), shape = RoundedCornerShape(10)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0E0E0E)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.background(Color(0xFF0E0E0E)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.text_minus),
                    contentDescription = "minus",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { viewModel.textSizeDown() }
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "가")
                Spacer(modifier = Modifier.width(30.dp))
                Icon(
                    painter = painterResource(id = R.drawable.text_plus),
                    contentDescription = "minus",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { viewModel.textSizeUp() }
                )
            }
            HorizontalDivider(color = Color(0xFF1A1A1A))
            OptionItem(
                text = "신고하기",
                Color(0xFFE43446),
                onClick = { onClickReport() },
                R.drawable.option_report
            )

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay(
    item: EssayApi.EssayItem,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    var isApiFinished by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        delay(200)
        isApiFinished = true
    }
    var isEssayBookMarked by remember { mutableStateOf(item.isBookmarked) }
    val iconImg =
        if (isEssayBookMarked) R.drawable.icon_bookmarkfill else R.drawable.icon_bookmarkborder

    if (isApiFinished) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Spacer(modifier = Modifier.height(40.dp))
                if (item.thumbnail != null && item.thumbnail!!.startsWith("https")) {
                    Column {
                        GlideImage(
                            model = item.thumbnail, contentDescription = "", modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = item.title!!,
                        fontSize = viewModel.titleTextSize,
                        modifier = Modifier
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {


                        Icon(
                            painter = painterResource(id = iconImg),
                            contentDescription = "iconImg",
                            Modifier
                                .size(30.dp)
                                .clickable {
                                    isEssayBookMarked = !isEssayBookMarked

                                    viewModel.viewModelScope.launch {
                                        if (isEssayBookMarked) viewModel.addBookMark(item.id!!)
                                        else viewModel.deleteBookMark(
                                            item.id!!
                                        )
                                    }

                                },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
//                Text(
//                    text = item.content!!,
//                    fontSize = viewModel.contentTextSize,
//                    lineHeight = 27.2.sp,
//                    modifier = Modifier.padding(horizontal = 20.dp),
//                    color = Color(0xFFB4B4B4)
//                )
                RichText(
                    state = rememberRichTextState().setHtml(item.content!!),
                    fontSize = viewModel.contentTextSize,
                    lineHeight = 27.2.sp,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFB4B4B4)
                )

                Spacer(modifier = Modifier.height(46.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp), contentAlignment = Alignment.BottomEnd
                ) {
                    Column {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            (if (item.author != null) item.author!!.nickname else "알 수 없는")?.let {
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
                                text = formatDateTime(item.createdDate ?: ""),
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF686868)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (item.linkedOutGauge != null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row {
                                    repeat(item.linkedOutGauge!!) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ring),
                                            contentDescription = "ring",
                                            modifier = Modifier.size(14.dp),
                                            colorFilter = ColorFilter.tint(Color(0xFF686868))
                                        )
                                        if (it != item.linkedOutGauge!! - 1) Spacer(
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
                if (item.tags != null) {
                    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                        repeat(item.tags!!.size) {
                            SuggestionChip(
                                onClick = { },
                                label = { Text(item.tags!![it].name) },
                                shape = RoundedCornerShape(50)
                            )
                            if (it != item.tags!!.size - 1) Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun SequenceBottomBar(
    item: EssayApi.EssayItem,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let { navController.previousBackStackEntry }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route

    var isEssayBookMarked by remember { mutableStateOf(item.isBookmarked) }
    val iconImg =
        if (isEssayBookMarked) R.drawable.icon_bookmarkfill else R.drawable.icon_bookmarkborder

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
                    text = "이전에 조회한 글이 없습니다.",
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {

                Icon(painter = painterResource(id = iconImg), contentDescription = "bookMark",
                    Modifier
                        .padding(start = 20.dp)
                        .clickable {
                            isEssayBookMarked = !isEssayBookMarked

                            viewModel.viewModelScope.launch {
                                if (isEssayBookMarked) viewModel.addBookMark(item.id!!)
                                else viewModel.deleteBookMark(
                                    item.id!!
                                )
                            }

                        }
                        .size(35.dp))
            }
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
                                    // 커뮤니티 홈에서 들어온경우를 제외하고 back stack한다
                                    if (previousRoute != Routes.Community) {
                                        if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                            viewModel.detailEssayBackStack.pop()
                                            Log.d(
                                                ContentValues.TAG,
                                                "pushpushpop: ${viewModel.detailEssayBackStack}"
                                            )
                                            if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                                viewModel.detailEssay =
                                                    viewModel.detailEssayBackStack.peek()
                                                viewModel.setBackDetailEssay(viewModel.detailEssayBackStack.peek()) //detailEssay값을 아예 수정
                                            }
                                        }
                                        navController.popBackStack()
                                    } else {
                                        noExistPreviousStack = true
                                    }

                                }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "봤던 글", fontSize = 12.sp, color = Color.White)
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
                                    viewModel.readDetailEssay(
                                        viewModel.previousEssayList[0].id!!,
                                        navController = navController,
                                        TYPE_RECOMMEND
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "랜덤 글", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ReportMenuBottomSheet(viewModel: CommunityViewModel) {
    val reportOptions = listOf(
        "스팸/도배글", "음란물", "욕설/혐오 표현 또는 상징", "거짓 정보",
        "청소년에게 유해한 내용", "불쾌한 표현", "불법 정보", "지식재산권 침해",
        "개인 정보 노출", "기타 문제"
    )

    var selectedItem by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .height(715.dp)
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Column {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "신고", fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.height(13.dp))
                Text(text = "이 글을 신고하는 이유를 선택해주세요.", color = Color.White)
                Text(text = "한 글당 한번의 신고만 가능합니다.", color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(10.dp))
            }
            SingleSelectReportList(
                items = reportOptions,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.reportEssay(viewModel.detailEssay.id!!, selectedItem)
                },
                modifier = Modifier
                    .fillMaxWidth() // TODO: report API 연동 필요
                    .height(61.dp)
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(20)
            ) {
                Text(text = "신고하기", color = Color.Black)
            }
        }
    }
}

@Composable
fun ReportItem(
    reportItem: String,
    isSelected: Boolean,
    selectedColor: Color,
    onItemSelected: (Boolean) -> Unit
) {
    val color = if (isSelected) selectedColor else Color(0xFF252525)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onItemSelected(!isSelected) }) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    tint = color,
                    contentDescription = null
                )
            }
            Text(text = reportItem, color = Color.White)
        }
        if (reportItem == "기타 문제" && isSelected) {
            ReportTextField("신고 내용을 작성해주세요", selectedColor)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SingleSelectReportList(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    LazyColumn(Modifier.height(460.dp)) {
        items(items) { item ->
            val isSelected = item == selectedItem
            val borderColor = if (isSelected) LinkedInColor else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, shape = RoundedCornerShape(8))
                    .clickable {
                        onItemSelected(if (selectedItem == item) "" else item)
                    }
            ) {
                Column {
                    ReportItem(
                        reportItem = item,
                        isSelected = isSelected,
                        selectedColor = LinkedInColor,
                        onItemSelected = { selected ->
                            onItemSelected(if (selected) item else "")
                        }
                    )
                    HorizontalDivider(color = Color(0xFF202020))
                }
            }
        }
    }
}


@Composable
fun ReportTextField(hint: String, selectedColor: Color) {


    var isFocused by remember { mutableStateOf(false) }
    var borderColor = if (isFocused) selectedColor else Color(0xFF202020)

    var text by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 15.dp)
    ) {
        TextField(
            placeholder = { Text(text = hint) },
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { isFocused = it.isFocused }
                .border(1.dp, color = borderColor, shape = RoundedCornerShape(6)),
            value = text,
            onValueChange = {
                text = it
            },


            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorTextColor = Color.Red,
                errorContainerColor = Color(0xFF252525),
                errorIndicatorColor = Color.Transparent
            ),
        )
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReportComplete(isCompleteClicked: () -> Unit) {
    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(
                    color = LinkedInColor,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("아무개들")
            }
            append("이 보다 ")
            withStyle(
                style = SpanStyle(
                    color = LinkedInColor,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("양질의 글")
            }
            append("을 읽을 수 있게 됐어요\n신고해주셔서 감사합니다!")
        }.toAnnotatedString()
    }

    Box(
        modifier = Modifier
            .height(715.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 55.dp)
    ) {
        Column(Modifier.padding(horizontal = 20.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "수상한 글 검거 완료", fontSize = 20.sp, color = LinkedInColor)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = annotatedString,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(54.dp))

            }
            Text(text = "신고 접수", fontSize = 16.sp, color = Color.White)
            Text(
                text = "아무개님의 신고는 링크드아웃의 서비스를 개선하고 유지하는데 큰 도움이 돼요.",
                color = Color(0xFF6B6B6B),
                fontSize = 14.sp
            )
            GlideImage(model = R.drawable.more, contentDescription = "more")
            Spacer(modifier = Modifier.height(7.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "검토 대기", fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                GlideImage(model = R.drawable.chip_ongoing, contentDescription = "onGoingChip")
//                SuggestionChip(
//                    modifier = Modifier.height(15.dp),
//                    onClick = { Log.d("Suggestion chip", "hello world") },
//                    label = { Text("진행중", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Bold) },
//                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFFFBB36)),
//                )

            }
            Text(
                text = "링크드아웃 팀에서는 수상한 글을 꼼꼼하게 심문해 규정을 어긴 글을 최대한 빠르게 체포하고 있어요.",
                color = Color(0xFF6B6B6B),
                fontSize = 14.sp
            )
            GlideImage(model = R.drawable.more, contentDescription = "more")
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = "검토 완료", fontSize = 16.sp, color = Color.White)
            Text(
                text = "심문이 끝난 후 결과를 알려드려요. 나쁜 글이 확실하다면 글을 공식적으로 체포했다는 알림을 보내드릴게요!",
                color = Color(0xFF6B6B6B),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(72.dp))

            Button(
                onClick = { isCompleteClicked() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(61.dp), shape = RoundedCornerShape(20)
            ) {
                Text(text = "확인", color = Color.Black)
            }
        }
    }
}



