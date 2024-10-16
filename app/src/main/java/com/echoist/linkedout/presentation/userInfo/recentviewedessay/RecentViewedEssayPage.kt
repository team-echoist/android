package com.echoist.linkedout.presentation.userInfo.recentviewedessay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.community.EssayListItem
import com.echoist.linkedout.presentation.userInfo.account.SettingTopAppBar
import com.echoist.linkedout.presentation.util.TYPE_RECOMMEND

@Composable
fun RecentViewedEssayPage(
    navController: NavController,
    viewModel: CommunityViewModel = hiltViewModel(),
    recentEssayViewModel: RecentEssayViewModel = hiltViewModel()
) {

    val recentEssayList by recentEssayViewModel.recentEssayList.collectAsState()

    Scaffold(
        topBar = {
            SettingTopAppBar("최근 본 글", navController)
        },
        content = {
            Column(
                modifier = Modifier.padding(it)
            ) {
                if (recentEssayList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        items(recentEssayList) { essay ->
                            EssayListItem(
                                item = essay,
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "최근 본 글이 없습니다.",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecentEssayListItemNoTime(
    item: EssayApi.EssayItem,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    val color =
        Color.White

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)
        .clickable {
            viewModel.readDetailRecentEssay(item.id!!, navController, TYPE_RECOMMEND)
            //navigate
        }
        .height(140.dp)) {
        //타이틀
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(7f)
                    .padding(top = 0.dp, start = 20.dp, end = 20.dp)// Column 비율 조정
            ) {
                Row(
                    Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = item.title!!,
                        color = color,
                        fontSize = 14.sp,

                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.content!!,
                    lineHeight = 27.2.sp,
                    fontSize = 14.sp,

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
            Text(
                text = if (item.author?.nickname != null) item.author!!.nickname!! else "닉 없음",
                fontSize = 10.sp,
                color = Color(0xFF686868)
            )
        }
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalDivider(color = Color(0xFF686868))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
        }
    }
}