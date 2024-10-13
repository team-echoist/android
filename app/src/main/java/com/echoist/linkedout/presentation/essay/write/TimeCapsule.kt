package com.echoist.linkedout.presentation.essay.write

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.ui.theme.LinkedInColor

@Composable
@Preview
fun TimeCapsulePager() {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState {
        3
    }
    Box(
        modifier = Modifier
            .size(280.dp, 480.dp)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0E0E0E)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(101.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .background(LinkedInColor)
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "[땅에 묻기] 기능을 사용해보세요",
                    fontSize = 16.sp,
                    color = Color.White, fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "특별한 장소에서의 기록을 더 특별하게!",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> TimeCapsulePagerStep1()
                    1 -> TimeCapsulePagerStep2()
                    2 -> TimeCapsulePagerStep3()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    val isSelected = pagerState.currentPage == index
                    val size = if (isSelected) Pair(20.dp, 10.dp) else Pair(10.dp, 10.dp)
                    Box(
                        modifier = Modifier
                            .size(width = size.first, height = size.second)
                            .background(
                                color = if (isSelected) LinkedInColor else Color(0xFF313131),
                                shape = RoundedCornerShape(50) // 동그란 인디케이터
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun TimeCapsulePagerStep1() {
    Box(
        modifier = Modifier
            .size(280.dp, 390.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(20.dp))
                TimeCapsuleStepChip("Step 1")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "위치 기능 사용하기", color = LinkedInColor)
            }
            Spacer(modifier = Modifier.height(45.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center  // 가로 중앙 정렬
            ) {
                GlideImage(
                    model = R.drawable.capsule1,
                    contentDescription = "capsuleImg1",
                    modifier = Modifier.size(224.dp, 195.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun TimeCapsulePagerStep2() {
    Box(
        modifier = Modifier
            .size(280.dp, 390.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            GlideImage(
                model = R.drawable.capsule2,
                contentDescription = "capsuleImg2",
                modifier = Modifier.size(196.5.dp, 285.03.dp)
            )
        }
        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Row {
                Spacer(modifier = Modifier.width(20.dp))
                TimeCapsuleStepChip("Step 2")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "작성 완료 후 뜨는 모달에서\n" +
                            "[땅에 묻기] 누르기", color = LinkedInColor
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier.padding(start = 88.dp),
                text = "*땅에 묻은 글은 다른 장소에서 \n보이지 않아요!",
                fontSize = 12.sp,
                color = Color(0xFFEDBC61)
            )

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun TimeCapsulePagerStep3() {
    Box(
        modifier = Modifier
            .size(280.dp, 390.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(20.dp))
                TimeCapsuleStepChip("Step 3")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "글을 썼던 장소 다시 찾아오기", color = LinkedInColor)
            }
            GlideImage(
                model = R.drawable.capsule3,
                contentDescription = "capsuleImg3",
                contentScale = ContentScale.None
            )
        }
    }
}


@Composable
fun TimeCapsuleStepChip(text: String) {
    Box(
        modifier = Modifier
            .size(60.dp, 25.dp)
            .border(
                1.dp, LinkedInColor,
                RoundedCornerShape(20.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 14.sp, color = LinkedInColor)
    }
}