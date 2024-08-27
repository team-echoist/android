package com.echoist.linkedout.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.formatDateTime
import com.echoist.linkedout.viewModels.CommunityViewModel
import kotlinx.coroutines.launch

//@Preview
//@Composable
//fun PreviewTest() {
//    Column {
//        CommuTopAppBar("test", NavController(LocalContext.current))
//        SubscriberPage(CommunityViewModel(), NavController(LocalContext.current))
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommuTopAppBar(
    text: String,
    navController: NavController,
    viewModel: CommunityViewModel,
    onClick: () -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Row {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ArrowBack",
                    Modifier
                        .size(30.dp)


                        .clickable {
                            navController.popBackStack()
                            viewModel.unSubscribeClicked = false
                        }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = text, fontWeight = FontWeight.Bold)

            }
        },
        actions = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "",
                Modifier
                    .size(30.dp)
                    .padding(start = 10.dp)

                    .clickable {
                        onClick()
                    })
            Spacer(modifier = Modifier.width(13.dp))
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "Bookmark",
                Modifier
                    .size(30.dp)
                    .padding(end = 10.dp),
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Profile(userInfo: UserInfo) {

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF616FED),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("${userInfo.nickname!!} ")
            }
            append("아무개")
        }.toAnnotatedString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        GlideImage(
            model = R.drawable.bottom_nav_3,
            contentDescription = "profileImage",
            Modifier.size(108.dp)
        ) //todo 프로필이미지로 변경 viewmodel.userItem.profileImage
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = annotatedString, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //todo profile page 에서 searchingpage 구현하기
            Row(
                modifier = Modifier.background(Color(0xFF0D0D0D), shape = RoundedCornerShape(20)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    Text(text = "쓴글", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "21", fontSize = 18.sp, color = Color(0xFF616161))
                }
                VerticalDivider(Modifier.height(41.dp), color = Color(0xFF191919))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "발행", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "5", fontSize = 18.sp, color = Color(0xFF616161))
                }
                VerticalDivider(Modifier.height(41.dp), color = Color(0xFF191919))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "링크드아웃", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "12", fontSize = 18.sp, color = Color(0xFF616161))
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                .height(40.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "구독중", fontSize = 14.sp, color = Color(0xFF616161))
        }
        Spacer(modifier = Modifier.height(30.dp))

    }
}


@Composable
fun ProfilePage(viewModel: CommunityViewModel, navController: NavController) {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState { 2 }

    Column {
        Profile(userInfo = viewModel.userItem) //todo 여기서 유저아이템 바꿔야됨
        ProfileChips(pagerState = pagerState)
        Spacer(modifier = Modifier.height(20.dp))

        ProfilePager(
            pagerState = pagerState,
            communityViewModel = viewModel,
            navController = navController
        )

    }


}

@Composable
fun ProfilePager(
    pagerState: PagerState,
    communityViewModel: CommunityViewModel,
    navController: NavController
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> LazyColumn {
                items(items = communityViewModel.previousEssayList) {   //랜덤리스트 말고 수정할것. 그사람의 리스트로
                    EssayListItem(
                        item = it,
                        viewModel = communityViewModel,
                        navController = navController
                    )
                }
            }

            1 -> LazyColumn {
                if (communityViewModel.storyList.isNotEmpty()) {
                    items(items = communityViewModel.storyList) {   //랜덤리스트 말고 수정할것. 그사람의 리스트로
                        ProfileStoryItem(it)
                    }
                }

            }
        }
    }
}


@Composable
fun ProfileChips(pagerState: PagerState) {
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
                    text = "스토리",// 색상을 먼저 적용합니다
                    modifier = Modifier.clickable {

                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }

                    } // Modifier.clickable을 마지막에 적용합니다
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (pagerState.currentPage == 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(65.dp),
                        color = color,
                        thickness = 3.dp
                    )
                }
            }

        }

    }
}

@Composable
fun ProfileStoryItem(story: Story) {
    Box(modifier = Modifier
        .padding(horizontal = 20.dp)
        .fillMaxWidth()
        .clickable { }
        .height(60.dp)) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            StoryCountIcon(story.essaysCount!!)
            Spacer(modifier = Modifier.width(30.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = story.name, fontSize = 20.sp)
                Text(
                    text = formatDateTime(story.createdDate),
                    fontSize = 10.sp,
                    color = Color(0xFF686868)
                )
            }
        }

    }

}
