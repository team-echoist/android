package com.echoist.linkedout.page

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.R
import com.echoist.linkedout.components.HashTagGroup
import com.echoist.linkedout.components.LocationGroup
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun PreviewWritingPage2() {
    WritingCompletePage(
        navController = rememberNavController(), WritingViewModel(), "token"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingCompletePage(
    navController: NavController,
    viewModel: WritingViewModel,
    accessToken: String
) {
    viewModel.accessToken = accessToken
    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    val scrollState = rememberScrollState()
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val bitmap: Bitmap? = viewModel.imageBitmap.value
    val imageBitmap: ImageBitmap? = bitmap?.asImageBitmap()

    LinkedOutTheme {
        BottomSheetScaffold(
            sheetContainerColor = Color.White,
            scaffoldState = scaffoldState,
            sheetContent = {
                WritingCompletePager(viewModel = viewModel, navController = navController)},

            sheetPeekHeight = 56.dp
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
                Box(modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .padding(bottom = 67.dp)
                    ) {
                        if (imageBitmap != null){
                            Box(contentAlignment = Alignment.Center) {
                                Image(bitmap = imageBitmap, contentDescription = "image")
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        CompleteTitle(viewModel = viewModel)
                        CompleteContents(viewModel = viewModel)
                        CompleteNickName()
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
                        if (viewModel.locationList.isNotEmpty() || viewModel.longitude.toString().isNotEmpty()){
                            Spacer(modifier = Modifier.height(50.dp))
                            LocationGroup(viewModel = viewModel)
                        }
                        if (viewModel.hashTagList.isNotEmpty()){
                            Spacer(modifier = Modifier.height(10.dp))
                            HashTagGroup(viewModel = viewModel)
                        }

                    }
                }
                //바텀시트 올라와있으면 패딩값 추가
                val expandDp =  if (bottomSheetState.currentValue == SheetValue.Expanded) 350.dp else 40.dp
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = expandDp),
                    contentAlignment = Alignment.BottomCenter
                ){
                    AnimatedVisibility(
                        visible = viewModel.isDeleteClicked.value,
                        exit = fadeOut()
                    ) {
                        WritingDeleteCard(viewModel = viewModel, navController = navController)
                    }
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
                tint = if (isSystemInDarkTheme()) Color(0xFF727070) else Color.Gray,
                modifier = Modifier

                    .padding(start = 20.dp)
                    .clickable {
                        navController.popBackStack()
                        Log.d("asdfadsf", "adsfadsf")
                    } //뒤로가기
            )
        },
        actions = {
            Text(
                text = "삭제",
                color = Color.Red,
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
        markdown = viewModel.content.value.text,
        fontSize = 16.sp,
        color = Color(0xFFB4B4B4),
        modifier = Modifier.padding(start = 25.dp, bottom = 42.dp, end = 25.dp)
    )
}

@Composable
fun CompleteNickName() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
        Text(
            text = "구루브",  /*todo 닉네임 구현 필요합니다. */
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
        when (viewModel.ringTouchedTime.value) {
            1 -> R.drawable.ring
            2 -> R.drawable.ring2
            3 -> R.drawable.ring3
            4 -> R.drawable.ring4
            5 -> R.drawable.ring5
            else -> R.drawable.pw_eye_off
        }

    CompositionLocalProvider(LocalRippleConfiguration provides  null) {
        Image(
            painter = painterResource(id = ringImage),
            contentDescription = null,
            modifier = Modifier
                .height(45.dp)
                .clickable() {
                    if (viewModel.ringTouchedTime.value != 1) {
                        viewModel.ringTouchedTime.value -= 1
                    }
                }
        )
    }



}

@Composable
fun SingleRing(viewModel: WritingViewModel) {
    Row(modifier = Modifier.animateContentSize()){
        when (viewModel.ringTouchedTime.value) {

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
    Image(
        painter = painterResource(id = R.drawable.ring),
        contentDescription = null,
        modifier = Modifier
            .height(45.dp)
            .clickable { viewModel.ringTouchedTime.value += 1 }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WritingCompletePager(viewModel: WritingViewModel, navController: NavController) {
    val scrollState = rememberScrollState()
    val pagerstate = rememberPagerState {
        2
    }
    HorizontalPager(state = pagerstate) {
        when (it) {
            0 -> {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(279.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(211.dp, 48.dp),
                        painter = painterResource(id = R.drawable.text_bottomsheet),
                        contentDescription = "고리를 풀어"
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
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "nextpage",
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
                        viewModel.hashTagList.forEach {it->
                            Text(text = "#$it", color = Color(0xFF686868))
                            Spacer(modifier = Modifier.width(22.dp))
                        }
                    }

                }

            }

            1 -> {
                Box {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .height(279.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "이 글을 어떻게 할까요?",
                            modifier = Modifier.padding(bottom = 25.dp),
                            color = Color.Black
                        )
                        Button(
                            onClick = {
                                viewModel.writeEssay(navController = navController, status = "private")
                            },
                            modifier = Modifier.padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1D))
                        ) {
                            Text(text = "저장할래요", color = Color.White)
                        }
                        Button(
                            onClick = {
                                viewModel.writeEssay(navController, status = "published")
                            },
                            modifier = Modifier.padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1D))

                        ) {
                            Text(text = "나눠볼래요", color = Color.White)
                        }
                        Button(
                            onClick = {
                                viewModel.writeEssay(navController, status = "linkedout")
                            },
                            modifier = Modifier.padding(bottom = 30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1D))

                        ) {
                            Text(text = "놓아줄래요", color = Color.White)
                        }

                    }
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(start = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "arrow back"
                        )
                    }
                }


            }
        }
    }
}
//todo page 이동시 뷰모델 초기화하는 함수 따로 작성하기.

@Composable
fun WritingDeleteCard(viewModel: WritingViewModel, navController: NavController) {

    if (viewModel.isDeleteClicked.value) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
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
                        modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
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
                                navController.navigate("HOME/${viewModel.accessToken}")
                                viewModel.initialize()
                            },
                        fontSize = 16.sp,
                        text = "삭제하기",
                        textAlign = TextAlign.Center,
                        color = Color.Red
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
