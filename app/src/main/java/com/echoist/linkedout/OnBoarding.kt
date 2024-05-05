package com.echoist.linkedout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingPager(navController: NavController){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)){
        val pagerstate = rememberPagerState { 4 }

        HorizontalPager(state = pagerstate) { page->
            when(page){
                0 -> OnBoardingPage(
                    mainText = "오직 나만을 위한 글쓰기",
                    subText = "모든 글쓰기 활동은 '필명'으로 진행됩니다.\n여러 관계에서 벗어나\n솔직한 나를 표현해보세요."
                )
                1 -> OnBoardingPage(
                    mainText = "글의 행방 결정하기",
                    subText = "작성한 글은 3가지 방향으로 보낼 수 있어요.\n원하는 방법으로 감정을 해소하세요."
                )
                2 -> OnBoardingPage(
                    mainText = "에세이 엮기",
                    subText = "써두었던 글을 모아\n나만의 에세이 모음집을 만들 수 있어요."
                )
                3 -> OnBoardingLastPage(
                    mainText = "다양한 감정 마주하기",
                    subText = "타인의 솔직한 글을 읽는 경험을 할 수 있어요\n문장 속에 담긴 다양한 감정을 마주해보세요.",
                    navController
                )
            }
        }
        Row( //todo 위치 수정하기 그냥 column안에 넣어서 사용하는게 좋을듯 아니면 그냥 스캐폴드?
            Modifier
                .height(50.dp)
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        )
        {
            repeat(4) { iteration ->
                val color = if (pagerstate.currentPage == iteration) Color(0xFFE4A89E) else Color.White.copy(alpha = 0.5f)
                if (pagerstate.currentPage == iteration){
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(20.dp, 10.dp)

                    )
                }
                else{
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)

                    )
                }

            }
        }
    }

}

@Composable
fun OnBoardingPage(mainText : String, subText : String){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //todo 3d 그래픽 넣을것~~

        Text(text = mainText, color = Color.White, textAlign = TextAlign.Center, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = subText, color = Color(0xFF686868), textAlign = TextAlign.Center)

    }
}

@Composable
fun OnBoardingLastPage(mainText : String, subText : String,navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //todo 3d 그래픽 넣을것~~

        Text(text = mainText, color = Color.White, textAlign = TextAlign.Center, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = subText, color = Color(0xFF686868), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(90.dp))
        Button(
            modifier = Modifier.size(200.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE4A89E)),
            onClick = {
                navController.navigate("HOME")
            }
        ) {
            Text(text = "시작하기")
        }

    }
}
