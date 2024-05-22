package com.echoist.linkedout.page

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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.components.EssayListItem
import com.echoist.linkedout.data.EssayItem
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel

@Preview
@Composable
fun CommunityDetailPagePreview() {
    CommunityDetailPage(rememberNavController(), CommunityViewModel())
}


@Composable
fun CommunityDetailPage(navController: NavController, viewModel: CommunityViewModel) {

    LinkedOutTheme {
        Scaffold(
            topBar = {
                CommunityTopAppBar(navController = navController, viewModel)
            },
            content = { it ->
                Box(
                    Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.TopCenter
                ) {
                    LazyColumn {
                        item {
                            DetailEssay(item = viewModel.detailEssay)
                            Spacer(modifier = Modifier.height(28.dp))
                            SubscriberSimpleItem(
                                item = viewModel.userItem,
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
                                    .padding(start = 20.dp, end = 20.dp),
                                contentAlignment = Alignment.CenterStart
                            )
                            {
                                Text(
                                    text = "'${viewModel.userItem.nickname} 아무개'의 이전 글",
                                    fontSize = 14.sp,
                                    color = Color(0xFF616FED)
                                )

                            }

                        }
                        items(items = viewModel.randomList) { it -> //랜덤리스트 말고 수정할것. 그사람의 리스트로
                            EssayListItem(
                                item = it,
                                viewModel = viewModel,
                                navController = navController
                            )
                        }


                    }

                    //수정 옵션
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
                            ReportOption(viewModel = viewModel)
                        }
                    }

                }
            }
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityTopAppBar(navController: NavController, viewModel: CommunityViewModel) {

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
                        if (viewModel.detailEssayBackStack.isNotEmpty()) {
                            viewModel.detailEssayBackStack.pop()
                            Log.d(ContentValues.TAG, "pushpushpop: ${viewModel.detailEssayBackStack}")
                            if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                viewModel.detailEssay = viewModel.detailEssayBackStack.peek()
                            }
                        }
                        navController.popBackStack()
                    } //뒤로가기
            )
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .clickable {
                        viewModel.isOptionClicked = !viewModel.isOptionClicked
                    },
            )
        })
}

@Composable
fun ReportOption(viewModel: CommunityViewModel) {
    Surface(modifier = Modifier.size(180.dp, 250.dp), shape = RoundedCornerShape(2)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.text_minus),
                    contentDescription = "minus"
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "가")
                Spacer(modifier = Modifier.width(30.dp))
                Icon(
                    painter = painterResource(id = R.drawable.text_plus),
                    contentDescription = "plus"
                )
            }
            HorizontalDivider()
            OptionItem(text = "신고하기", Color.Red, {}, R.drawable.option_report)

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay(item: EssayItem) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            if (item.thumbnail != null) {
                GlideImage(
                    model = item.thumbnail, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
            Row {
                Text(text = item.title, fontSize = 24.sp, modifier = Modifier)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "",
                        Modifier.size(30.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = item.content,
                fontSize = 16.sp,
                modifier = Modifier,
                color = Color(0xFFB4B4B4)
            )

            Spacer(modifier = Modifier.height(46.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Column {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = item.nickName,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            color = Color(0xFF686868)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = item.createdDate,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            color = Color(0xFF686868)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        Row {
                            repeat(item.linkedOutGauge) {
                                Image(
                                    painter = painterResource(id = R.drawable.ring),
                                    contentDescription = "ring",
                                    modifier = Modifier.size(14.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF686868))
                                )
                                if (it != item.linkedOutGauge - 1) Spacer(
                                    modifier = Modifier.width(
                                        4.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            Row {
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text("깨달음") },
                    shape = RoundedCornerShape(50)
                )
                Spacer(modifier = Modifier.width(10.dp))
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text("놀라움") },
                    shape = RoundedCornerShape(50)
                )
                Spacer(modifier = Modifier.width(10.dp))
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text("새로움") },
                    shape = RoundedCornerShape(50)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

        }

    }
}