package com.echoist.linkedout.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.data.EssayItem
import com.echoist.linkedout.viewModels.FakeMyLogViewModel
import com.echoist.linkedout.viewModels.MyLogView1Model

@Preview
@Composable
fun pre(){
    val viewModel = FakeMyLogViewModel
    Column {

        LastEssayItem(item = FakeMyLogViewModel.detailEssay,viewModel, rememberNavController())
        LastEssayPager(viewModel, rememberNavController())
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LastEssayItem(
    item: EssayItem,
    viewModel: MyLogView1Model,
    navController: NavController
) {
    val color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            viewModel.detailEssay = item
            navController.navigate("MyLogDetailPage")
        }
        .height(140.dp)) {
        //타이틀
        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .weight(7f) // Column 비율 조정
            ) {
                Text(
                    text = item.title,
                    color = color,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.content,
                    maxLines = 2,
                    color = color,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.thumbnail != null) {
                GlideImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                        .weight(3f) // 이미지 비율 조정
                )
            }
        }


        Box(
            contentAlignment = Alignment.BottomStart, modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 10.dp)
        ) {
            Text(text = item.createdDate, fontSize = 10.sp, color = Color(0xFF686868))
        }
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalDivider(color = Color(0xFF686868))
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LastEssayPager(viewModel: MyLogView1Model, navController: NavController){

    val pageCount = if (viewModel.detailEssay.published) viewModel.publishedEssayList.size/4 +1 else viewModel.myEssayList.size/4 +1
    val pagerstate = rememberPagerState { pageCount }

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(Color(0xFF1A1A1A)))
        Text(
            text = "이전 글",
            Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp),
            color = Color(0xFFE4A89E)
        )
        HorizontalDivider(color = Color(0xFF686868))

        HorizontalPager(state = pagerstate) { page ->
            val startIndex = page * 4
            val endIndex = minOf(
                startIndex + 5,
                if (viewModel.detailEssay.published) viewModel.publishedEssayList.size else viewModel.myEssayList.size
            )
            val essayList =
                if (viewModel.detailEssay.published) viewModel.publishedEssayList else viewModel.myEssayList
            val pageItems = essayList.subList(startIndex, endIndex).filter { it.id != viewModel.detailEssay.id }
            Column {
                pageItems.forEach { essay ->
                    LastEssayItem(
                        item = essay,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxSize() /* 부모 만큼 */
                .padding(bottom = 60.dp), contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                Modifier
                    .padding(bottom = 10.dp), //box 안에 있어야하는거같기도?
                horizontalArrangement = Arrangement.Center
            )
            {
                repeat(pageCount) { iteration ->
                    val color =
                        if (pagerstate.currentPage == iteration) Color(0xFFE4A89E) else Color.White.copy(
                            alpha = 0.5f
                        )
                    if (pagerstate.currentPage == iteration) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(10.dp, 10.dp)

                        )

                    } else {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(10.dp, 10.dp)

                        )
                    }

                }
            }
        }


    }
}