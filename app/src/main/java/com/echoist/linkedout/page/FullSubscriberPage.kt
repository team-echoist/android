package com.echoist.linkedout.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.components.CommuTopAppBar
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel


@Preview
@Composable
fun Prev3() {
    val viewModel : CommunityViewModel = viewModel()

    FullSubscriberPage(viewModel = viewModel, navController = rememberNavController())
}

@Composable
fun FullSubscriberPage(
    viewModel: CommunityViewModel,
    navController: NavController
) {

    LinkedOutTheme {
        Scaffold(
            topBar = {
                CommuTopAppBar(text = "전체 구독 목록", navController,viewModel)
            },
            bottomBar = { if (!viewModel.unSubscribeClicked) MyBottomNavigation(navController) },
            content = {

                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .padding(top = 30.dp)
                ) {
                    items(viewModel.subscribeUserList) { it -> //todo 랜덤리스트에서 구독자 에세이 리스트로 바꿔야함.
                        SubscriberSimpleItem(item = it, viewModel, navController)
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
                AnimatedVisibility(
                    visible = viewModel.unSubscribeClicked,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                        UnsubscribeAlert(viewModel)

                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SubscriberSimpleItem(
    item: UserInfo,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .clickable {
                if (!viewModel.unSubscribeClicked) {
                    viewModel.userItem = item
                    navController.navigate("SubscriberPage")
                }
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(20)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                GlideImage(
                    model = R.drawable.bottom_nav_3,
                    contentDescription = "profileimage",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(17.dp))
                Text(text = "${item.nickname!!} ", fontSize = 16.sp)
                Text(text = "아무개", fontSize = 16.sp, color = Color(0xFF656565))

            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp)
                .size(73.dp, 24.dp), contentAlignment = Alignment.CenterEnd
        ) {
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                Text(
                    text = "구독중",
                    color = Color(0xFF616FED),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                        .padding(start = 20.dp, top = 3.dp, end = 20.dp, bottom = 3.dp)
                        .clickable { viewModel.unSubscribeClicked = true } //todo 유저 정보 보내기
                )
            }
        }
    }

}

@Composable
fun UnsubscribeAlert(viewModel: CommunityViewModel) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.6f))
            .fillMaxSize()

    )
    Column(
        Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                .fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "구독을 취소하시겠습니까?")
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "구독 취소 시 업데이트 되는 글이 보이지 않습니다.", fontSize = 14.sp, modifier = Modifier.padding(start = 20.dp, end = 20.dp))
                Spacer(modifier = Modifier.height(15.dp))
                HorizontalDivider(color = Color(0xFF202020))
                Spacer(modifier = Modifier.height(18.dp))
                Text(text = "구독 취소", color = Color.Red) // todo 구독취소 기능구현
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { viewModel.unSubscribeClicked = false },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "돌아가기", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }

}