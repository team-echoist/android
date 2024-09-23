package com.echoist.linkedout.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import kotlinx.coroutines.delay

@Composable
fun TabletOnBoardingRoute(onStartClick: () -> Unit) {
    var isChanged by remember { mutableStateOf(true) }
    val pagerState = rememberPagerState { 4 }

    LaunchedEffect(key1 = true) {
        if (isChanged) {
            delay(3800)
            isChanged = false
        }
    }

    TabletOnBoardingScreen(
        isChanged = isChanged,
        pagerState = pagerState,
        onStartClick = { onStartClick() }
    )
}

@Composable
internal fun TabletOnBoardingScreen(
    isChanged: Boolean,
    pagerState: PagerState,
    onStartClick: () -> Unit
) {
    LinkedOutTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(state = pagerState) { page ->
                OnBoardingPageContent(page, isChanged)
            }

            if (!isChanged || pagerState.currentPage == 3) {
                OnBoardingFooter(
                    pagerState = pagerState,
                    onStartClick = onStartClick
                )
            }
        }
    }
}

@Composable
fun OnBoardingPageContent(page: Int, isChanged: Boolean) {
    when (page) {
        0 -> {
            if (isChanged) {
                SplashCompleteScreen()
            } else {
                OnBoardingPager(
                    mainText = "오직 나만을 위한 글쓰기",
                    subText = "모든 글쓰기 활동은 '필명'으로 진행됩니다.\n여러 관계에서 벗어나\n솔직한 나를 표현해보세요.",
                    resId = R.drawable.onboarding_final_1
                )
            }
        }

        1 -> OnBoardingPager(
            mainText = "글의 행방 결정하기",
            subText = "작성한 글은 3가지 방향으로 보낼 수 있어요.\n원하는 방법으로 감정을 해소하세요.",
            resId = R.drawable.onboarding_final_2
        )

        2 -> OnBoardingPager(
            mainText = "에세이 엮기",
            subText = "써두었던 글을 모아\n나만의 에세이 모음집을 만들 수 있어요.",
            resId = R.drawable.onboarding_final_3
        )

        3 -> OnBoardingPager(
            mainText = "다양한 감정 마주하기",
            subText = "타인의 솔직한 글을 읽는 경험을 할 수 있어요\n문장 속에 담긴 다양한 감정을 마주해보세요.",
            resId = R.drawable.onboarding_final_4
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SplashCompleteScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = R.drawable.splash_final,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun OnBoardingFooter(
    pagerState: PagerState,
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            repeat(4) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color(0xFF616FED) else Color.White.copy(
                        alpha = 0.5f
                    )
                PageIndicator(
                    isSelected = pagerState.currentPage == iteration,
                    color = color
                )
            }
        }

        if (pagerState.currentPage == 3) {
            StartButton(onStartClick)
        }
    }
}

@Composable
fun PageIndicator(isSelected: Boolean, color: Color) {
    val width = if (isSelected) 20.dp else 10.dp
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(CircleShape)
            .background(color)
            .size(width, 10.dp)
    )
}

@Composable
fun StartButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .navigationBarsPadding()
            .size(200.dp, 50.dp),
        onClick = onClick
    ) {
        Text(text = "시작하기", color = Color.Black)
    }
}
