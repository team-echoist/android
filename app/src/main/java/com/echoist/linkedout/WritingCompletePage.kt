package com.echoist.linkedout

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun PreviewWritingPage2() {
    WritingCompletePage(navController = rememberNavController(), WritingViewModel())
}

@Composable
fun WritingCompletePage(navController: NavController, viewModel: WritingViewModel) {

    val scrollState = rememberScrollState()
    val isBottomSheetOpen = remember {
        mutableStateOf(true)
    }

    LinkedOutTheme {
        Scaffold(topBar = { CompleteAppBar(navController = navController) }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(scrollState)
            )
            {

                // todo 이미지 있으면 올리고 없으면 무시하는형태.

                CompleteTitle(viewModel = viewModel)
                CompleteContents(viewModel = viewModel)
                CompleteNickName()
                CompleteDate(viewModel = viewModel)
                if (isBottomSheetOpen.value) {
                    BottomSheet(
                        closeSheet = {
                            isBottomSheetOpen.value = false
                            viewModel.ringTouchedTime.value = 5
                        },

                        viewModel = viewModel,
                        navController = navController
                    )
                }
                Button(onClick = { isBottomSheetOpen.value = true }) {
                    Text(text = "bottom sheet open")
                }

            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteAppBar(navController: NavController) {
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
                        /* todo 삭제 구현 필요합니다. */
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
        modifier = Modifier.padding(start = 25.dp, bottom = 42.dp)
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

//꼭 버튼으로만 다 닫고 올리고 해야하는가?
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    viewModel: WritingViewModel,
    closeSheet: () -> Unit,
    navController: NavController
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = Color.White,
        onDismissRequest = { closeSheet() },
        sheetState = sheetState
    ) {
        WritingCompletePager(viewModel = viewModel, navController = navController)
    }


}

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

    Image(
        painter = painterResource(id = ringImage),
        contentDescription = null,
        modifier = Modifier
            .height(45.dp)
            .clickable {
                if (viewModel.ringTouchedTime.value != 1) {
                    viewModel.ringTouchedTime.value -= 1
                }
            }
    )


}

@Composable
fun SingleRing(viewModel: WritingViewModel) {
    Row {
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
fun WritingCompletePager(viewModel: WritingViewModel,navController: NavController) {
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
                                contentDescription = "nextpage"
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(51.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "#깨달음") //todo 해시태그 어떻게 만들지 구현필요합니다.
                        Spacer(modifier = Modifier.width(22.dp))
                        Text(text = "#깨달음")
                        Spacer(modifier = Modifier.width(22.dp))
                        Text(text = "#깨달음")
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
                            onClick = { /*TODO 저장할래요 기능 구현필요*/
                                      viewModel.writeEssay(navController = navController)},
                            modifier = Modifier.padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1D))
                        ) {
                            Text(text = "저장할래요", color = Color.White)
                        }
                        Button(
                            onClick = { /*TODO 나눠볼래요 기능 구현필요*/ },
                            modifier = Modifier.padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1D))

                        ) {
                            Text(text = "나눠볼래요", color = Color.White)
                        }
                        Button(
                            onClick = { /*TODO 놓아줄래요 기능 구현필요*/ },
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
















