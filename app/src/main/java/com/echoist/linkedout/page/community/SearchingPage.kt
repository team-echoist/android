package com.echoist.linkedout.page.community

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.TYPE_RECOMMEND
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.components.EssayListItem
import com.echoist.linkedout.formatElapsedTime
import com.echoist.linkedout.viewModels.SearchingViewModel
import com.echoist.linkedout.viewModels.UiState
import kotlinx.coroutines.launch

@Composable
fun SearchingPage(
    viewModel: SearchingViewModel = hiltViewModel(),
    drawerState: DrawerState, navController: NavController
) {
    val searchUiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            viewModel.clearUiState()
            searchQuery = ""
        }
    }

    Scaffold(
        topBar = {
            Column {
                SearchingBar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { searchQuery = it },
                    onSearchTriggered = {
                        viewModel.readSearchingEssays(it)
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    onClickBack = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
                SearchingChips(pagerState = pagerState)
            }
        },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            when (searchUiState) {
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    SearchList(items = (searchUiState as UiState.Success).essays) {
                        viewModel.readDetailEssay(it.id!!, navController, TYPE_RECOMMEND)
                        viewModel.detailEssayBackStack.push(it)
                    }
                }

                is UiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "알 수 없는 에러가 발생했습니다.\n 잠시 후 다시 시도해 주세요.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = {
                            viewModel.readSearchingEssays(searchQuery)
                        }) {
                            Text(text = "재시도")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchList(items: List<EssayApi.EssayItem>, onClickedItem: (EssayApi.EssayItem) -> Unit) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "검색 결과가 없습니다.")
        }
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(items = items) { item ->
                val color = Color.White

                val annotatedTitleString = buildAnnotatedString {
                    append(item.title ?: "")
                    addStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = color
                        ),
                        start = 0,
                        end = item.title?.length ?: 0
                    )

                    val bulletText = "   • ${formatElapsedTime(item.createdDate!!)}"
                    append(bulletText)
                    addStyle(
                        style = SpanStyle(
                            fontSize = 10.sp,
                            color = Color(0xFF686868)
                        ),
                        start = item.title?.length ?: 0,
                        end = item.title?.length?.plus(bulletText.length) ?: bulletText.length
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .clickable {
                        onClickedItem(item)
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
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (item.status == "linkedout") {
                                    Icon(
                                        painter = painterResource(id = R.drawable.option_linkedout),
                                        contentDescription = "linkedout icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))

                                }
                                Text(
                                    text = annotatedTitleString,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(30.dp))
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = item.content!!,
                                maxLines = 2,
                                fontSize = 14.sp,
                                color = color,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (item.thumbnail != null && item.thumbnail!!.startsWith("https")) { //널이 아니거나 Https로 시작해야됨
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
                        if (item.author != null) { //author이 널이거나
                            Text(
                                text = item.author!!.nickname ?: "알 수 없는 아무개",
                                fontSize = 10.sp,
                                color = Color(0xFF686868)
                            )
                        } else {
                            Text(
                                text = "알 수 없는 아무개",
                                fontSize = 10.sp,
                                color = Color(0xFF686868)
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.BottomEnd, modifier = Modifier
                            .fillMaxSize()
                    ) {
                        HorizontalDivider(color = Color(0xFF333333))
                    }
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {

                    }
                }
            }
        }
    }
}

@Composable
fun SearchingPager(
    items: List<EssayApi.EssayItem>,
    pagerState: PagerState,
    searchingViewModel: SearchingViewModel,
    navController: NavController
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> LazyColumn {
                items(items = items) {
                    EssayListItem(
                        item = it,
                        viewModel = searchingViewModel,
                        navController = navController
                    )
                }
            }

            1 -> LazyColumn {
                items(items = searchingViewModel.previousEssayList) {   //랜덤리스트 말고 수정할것. 그사람의 리스트로
                    EssayListItem(
                        item = it,
                        viewModel = searchingViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun SearchingBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onClickBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back",
            tint = Color.White,
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    keyboardController?.hide()
                    onClickBack()
                }
        )
        TextField(
            shape = RoundedCornerShape(42),
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            value = searchQuery,
            onValueChange = {
                onSearchQueryChanged(it)
            },
            placeholder = {
                Text(
                    text = "검색어를 입력하세요",
                    color = Color(0xFF686868),
                    fontSize = 16.sp
                )
            },
            singleLine = true,
            trailingIcon = {
                if (searchQuery.isNotEmpty()) Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "cancel",
                    modifier = Modifier.clickable {
                        onSearchQueryChanged("")
                    }
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { //검색 누를시 검색
                    onSearchTriggered(searchQuery)
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

@Composable
fun SearchingChips(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val color = Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp), thickness = 1.dp, color = Color(0xFF686868)
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
                    text = "글",// 색상을 먼저 적용합니다
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
                        thickness = 3.dp
                    )
                }
            }
            //todo 다음 업데이트에 사용자 파트추가
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
//                    text = "사용자",// 색상을 먼저 적용합니다
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