package com.echoist.linkedout.presentation.essay.write

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.handleEssayAction
import com.echoist.linkedout.ui.theme.LinkedInColor
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun WritingCompletePage(
    navController: NavController,
    viewModel: WritingViewModel
) {
    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scrollState = rememberScrollState()
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    //네비게이션바 높이
    val navigationBarHeightPx =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val navigationBarHeightDp = with(LocalDensity.current) { navigationBarHeightPx }


    // 페이지가 로드될 때 BottomSheet를 확장 상태로 설정하기 위한 코드
    LaunchedEffect(Unit) {
        bottomSheetState.expand()
    }

    Log.d(TAG, "WritingCompletePage: ${viewModel.readDetailEssay()}")


    BottomSheetScaffold(
        sheetContainerColor = Color(0xFF191919),
        scaffoldState = scaffoldState,
        sheetContent = {
            WritingCompletePager(viewModel = viewModel, navController = navController)
        },
        sheetPeekHeight = 56.dp + navigationBarHeightDp
    ) {
        Scaffold(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        viewModel.isDeleteClicked.value = false
                    })
                }
                .fillMaxSize()
                .background(Color.Black),
            topBar = { CompleteAppBar(navController = navController, viewModel) },
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(bottom = 67.dp)
                ) {

                    Box(contentAlignment = Alignment.Center) {
                        GlideImage(
                            model = viewModel.imageUri ?: viewModel.imageUrl ?: "",
                            contentDescription = "uri",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    CompleteTitle(viewModel = viewModel)
                    CompleteContents(viewModel = viewModel)
                    CompleteNickName(viewModel.myProfile.nickname ?: "?")
                    CompleteDate(viewModel = viewModel)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 67.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (viewModel.locationList.isNotEmpty() && viewModel.longitude != null) {
                        Spacer(modifier = Modifier.height(50.dp))
                        LocationGroup(viewModel = viewModel)
                    }
                    if (viewModel.hashTagList.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        HashTagGroup(viewModel = viewModel)
                    }
                }
            }
            if (bottomSheetState.currentValue == SheetValue.Expanded || viewModel.isDeleteClicked.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                )
            }
            //바텀시트 올라와있으면 패딩값 추가
            val expandDp =
                if (bottomSheetState.currentValue == SheetValue.Expanded) 310.dp else 60.dp
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = expandDp),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = viewModel.isDeleteClicked.value,
                    enter = slideInVertically(
                        initialOffsetY = { 2000 },
                        animationSpec = tween(durationMillis = 500)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { 2000 },
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {
                    WritingDeleteCard(viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteAppBar(navController: NavController, viewModel: WritingViewModel) {

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
                    .clickable {
                        navController.popBackStack()
                        Log.d("asdfadsf", "adsfadsf")
                    } //뒤로가기
            )
        },
        actions = {
            Text(
                text = "삭제",
                color = Color(0xFFE43446),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable {
                        viewModel.isDeleteClicked.value = true
                    },
                fontSize = 16.sp
            )
        })
}


@Composable
fun CompleteTitle(viewModel: WritingViewModel) {

    Text(
        text = viewModel.title.value.text,
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(start = 25.dp, bottom = 20.dp)
    )
}


@Composable
fun CompleteContents(viewModel: WritingViewModel) {
    MarkdownText(
        markdown = viewModel.content,
        fontSize = 16.sp,
        color = Color(0xFFB4B4B4),
        modifier = Modifier.padding(start = 25.dp, bottom = 42.dp, end = 25.dp)
    )
}

@Composable
fun CompleteNickName(nickName: String) {

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
        Text(
            text = nickName,
            fontSize = 12.sp,
            color = Color(0xFF686868),
            modifier = Modifier.padding(end = 25.dp, bottom = 8.dp)
        )
    }
}


@Composable
fun CompleteDate(viewModel: WritingViewModel) {


    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")
    val formattedDateTime = currentDateTime.format(formatter)

    viewModel.date.value = formattedDateTime

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = formattedDateTime,
            fontSize = 12.sp,
            color = Color(0xFF686868),
            modifier = Modifier.padding(end = 25.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupRingImg(viewModel: WritingViewModel) {

    val ringImage =
        when (viewModel.ringTouchedTime) {
            1 -> R.drawable.ring
            2 -> R.drawable.ring2
            3 -> R.drawable.ring3
            4 -> R.drawable.ring4
            5 -> R.drawable.ring5
            else -> R.drawable.pw_eye_off
        }


    Icon(painter = painterResource(id = ringImage),
        tint = LinkedInColor,
        contentDescription = "ring",
        modifier = Modifier
            .height(45.dp)
            .clickable {
                if (viewModel.ringTouchedTime != 1) {
                    viewModel.ringTouchedTime -= 1
                }
            }
    )
}


@Composable
fun SingleRing(viewModel: WritingViewModel) {
    Row(modifier = Modifier.animateContentSize()) {
        when (viewModel.ringTouchedTime) {

            4 -> RingImg(viewModel)

            3 -> repeat(2) {
                RingImg(viewModel)
            }

            2 -> repeat(3) {
                RingImg(viewModel)
            }

            1 -> repeat(4) {
                RingImg(viewModel)
            }

        }

    }
}

@Composable
fun RingImg(viewModel: WritingViewModel) {
    Icon(
        painter = painterResource(id = R.drawable.ring),
        tint = LinkedInColor,
        contentDescription = null,
        modifier = Modifier
            .height(45.dp)
            .clickable { viewModel.ringTouchedTime += 1 }
    )

}

@Composable
fun WritingCompletePager(viewModel: WritingViewModel, navController: NavController) {
    val scrollState = rememberScrollState()
    val pagerstate = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            append("이 ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("글을 쓰면서 풀어낸 마음 ")
            }
            append("만큼\n고리를 풀어주세요.")
        }.toAnnotatedString()
    }


    HorizontalPager(state = pagerstate) {
        when (it) {
            0 -> {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = annotatedString,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))

                    Box {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            GroupRingImg(viewModel = viewModel)
                            Spacer(modifier = Modifier.height(22.dp))
                            SingleRing(viewModel = viewModel)

                        }
                        Box(
                            modifier = Modifier.matchParentSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                tint = LinkedInColor,
                                contentDescription = "nextpage",
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        pagerstate.animateScrollToPage(1)
                                    }
                                }
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(51.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        viewModel.hashTagList.forEachIndexed { index, tag ->
                            Text(text = "#$tag", color = Color.White)
                            if (index != viewModel.hashTagList.size - 1) {
                                Spacer(modifier = Modifier.width(22.dp))
                            }

                        }
                    }
                }
            }

            1 -> {
                Box {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .height(279.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "이 글을 어떻게 할까요?",
                            modifier = Modifier.padding(bottom = 25.dp),
                            color = Color.White
                        )

                        // 버튼들을 리스트로 처리
                        val buttons = listOf(
                            Triple("저장", R.drawable.btn_privated, "private"),
                            Triple("발행", R.drawable.btn_published, "published"),
                            Triple("linked-out", R.drawable.btn_linkedout, "linkedout")
                        )

                        buttons.forEach { (label, drawable, status) ->
                            Writing_Btn(label, drawable) {
                                handleEssayAction(status,viewModel,navController)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(start = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            tint = LinkedInColor,
                            contentDescription = "previousPage",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        pagerstate.animateScrollToPage(0)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WritingDeleteCard(viewModel: WritingViewModel, navController: NavController) {

    if (viewModel.isDeleteClicked.value) {

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF191919))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 28.dp, bottom = 28.dp),
                        text = "삭제된 글은 복구할 수 없습니다. 삭제하시겠습니까?",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFF202020)
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .clickable {
                                navController.popBackStack(
                                    "OnBoarding",
                                    false
                                ) //onboarding까지 전부 삭제.
                                navController.navigate("${Routes.Home}/200")
                                viewModel.initialize()
                            },
                        fontSize = 16.sp,
                        text = "삭제하기",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFE43446)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.clickable { viewModel.isDeleteClicked.value = false },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF191919))
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = "취소",

                        textAlign = TextAlign.Center,
                        color = Color.White
                    )

                }
            }
            Spacer(modifier = Modifier.height(35.dp))

        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Writing_Btn(text: String, imageResource: Int, isBtnClicked: () -> Unit) {
    Box(modifier = Modifier
        .clickable { isBtnClicked() }
        .fillMaxWidth()
        .padding(bottom = 16.dp, start = 50.dp, end = 50.dp)
        .height(44.dp)) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            GlideImage(model = imageResource, contentDescription = "")
            Text(text = text, color = Color.White, fontSize = 16.sp)
        }
    }
}

