package com.echoist.linkedout.components

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.formatDateTime
import com.echoist.linkedout.page.myLog.OptionItem
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLogTopAppBar(onClickSearch : ()->Unit,nickName : String,onClickNotification : ()->Unit){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "$nickName 님", fontWeight = FontWeight.Bold)
        },
        actions = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "",
                Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .clickable { onClickSearch() })
            Spacer(modifier = Modifier.width(13.dp))
            Icon(
                painter = painterResource(id = R.drawable.icon_noti),
                contentDescription = "",
                Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
                    .clickable { onClickNotification() }
            )

        }
    )
}
@Composable
fun EssayChips(pagerState: PagerState,viewModel: MyLogViewModel){
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(26.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 17.dp)) {
            Essaychip(
                text = "나만의 글 ${viewModel.myEssayList.size}",
                75.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)

                    }
                },
                color = if (pagerState.currentPage == 0) Color.White else Color.Gray
            )
            Spacer(modifier = Modifier.width(5.dp))
            Essaychip(
                text = "발행한 글 ${viewModel.publishedEssayList.size}",
                75.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                color = if (pagerState.currentPage == 1) Color.White else Color.Gray
            )
            Spacer(modifier = Modifier.width(5.dp))

            Essaychip(
                text = "스토리 ${viewModel.storyList.size}",
                75.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
                color = if (pagerState.currentPage == 2) Color.White else Color.Gray
            )

        }
    }
}

@Composable
fun Essaychip(
    text: String,
    dividerWidth: Dp,
    clickable: () -> Unit,
    color: Color
){

    Column(
        modifier = Modifier.padding(end = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 14.sp,
            text = text,
            color = color, // 색상을 먼저 적용합니다
            modifier = Modifier.clickable { clickable() } // Modifier.clickable을 마지막에 적용합니다
        )

        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(modifier = Modifier
            .width(dividerWidth),
            color = color,
            thickness = 2.dp)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EssayListItem(
    item: EssayApi.EssayItem,
    pagerState: PagerState,
    viewModel: MyLogViewModel,
    navController: NavController,
    writingViewModel: WritingViewModel
){
    val color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    var isOptionClicked by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            viewModel.readDetailEssay(item.id!!, navController)
            viewModel.detailEssayBackStack.push(item)
            Log.d(TAG, "pushpush: ${viewModel.detailEssayBackStack}")
        }
        .height(180.dp)){
        if (item.thumbnail != null){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                GlideImage(
                    model = item.thumbnail,
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                )
            }
        }
        //타이틀
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title!!,
                    color = color,
                    fontSize = 20.sp,

                    )
                if (pagerState.currentPage == 1){
                    Spacer(modifier = Modifier.width(10.dp))
                    OutBox()

                }

            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content!!,
                lineHeight = 27.2.sp,
                maxLines = 3,
                color = color,
                overflow = TextOverflow.Ellipsis
            )

        }
        Box(contentAlignment = Alignment.TopEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, top = 20.dp)) {
            Icon(painter = painterResource(id = R.drawable.more), tint = color, contentDescription = "more",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { isOptionClicked = !isOptionClicked }) //수정 box
        }



        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 20.dp)) {
            Text(text = formatDateTime(item.createdDate!!), fontSize = 10.sp, color = Color(0xFF686868))

        }

        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()) {
            HorizontalDivider(color = Color(0xFF333333))
        }
        if (isOptionClicked){
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp, top = 60.dp), contentAlignment = Alignment.TopEnd) {
                ModifyOrDeleteBox(viewModel,navController, writingViewModel = writingViewModel,item)
            }
        }

    }
}

@Composable
fun EssayListPage1(viewModel: MyLogViewModel, pagerState: PagerState, navController: NavController,writingViewModel: WritingViewModel){

        LazyColumn {
            items(viewModel.myEssayList){it->
                EssayListItem(item = it,pagerState,viewModel,navController,writingViewModel)
            }
        }
}

@Composable
fun EssayListPage2(viewModel: MyLogViewModel, pagerState: PagerState, navController: NavController,writingViewModel: WritingViewModel){

        LazyColumn {
            items(viewModel.publishedEssayList) {
                EssayListItem(item = it, pagerState, viewModel, navController,writingViewModel)
            }
        }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryListPage(viewModel: MyLogViewModel, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            model = R.drawable.background_logo,
            contentDescription = "R.drawable.background_logo",
            modifier = Modifier.matchParentSize()
        )
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.isCreateStory = true
                    viewModel.essayIdList.clear()
                    navController.navigate("StoryPage")
                    viewModel.storyTextFieldTitle = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add story", modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(viewModel.storyList) {
                    StoryItem(it, viewModel, navController)
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}


@Composable
fun StoryItem(story: Story,viewModel: MyLogViewModel,navController: NavController){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            viewModel.setSelectStory(story)
            navController.navigate("StoryDetailPage")
        }
        .height(60.dp)){
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            StoryCountIcon(story.essaysCount!!)
            Spacer(modifier = Modifier.width(30.dp))
            Text(text = story.name, fontSize = 20.sp)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "more",
                modifier = Modifier
                    .clickable {
                        viewModel.isModifyStoryClicked = true
                        viewModel.isCreateStory = false
                        viewModel.setSelectStory(story)
                        Log.d(TAG, "StoryItem: ${viewModel.getSelectedStory().name}")
                        Log.d(TAG, "StoryItem: ${viewModel.getSelectedStory().name}")

                    }
                    .size(30.dp)
            )
        }
    }
}

@Composable
fun ModifyStoryBox(
    viewModel: MyLogViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable {
                viewModel.isModifyStoryClicked = false
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 20.dp)
                , verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(20.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "스토리 편집",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        viewModel.isModifyStoryClicked = false
                        viewModel.isCreateStory = false //createstory false면 modify로 취급
                        navController.navigate("StoryPage")

                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = Color(0xFF202020))
                Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "스토리 삭제",
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            viewModel.isModifyStoryClicked = false
                            viewModel.deleteMyStory(viewModel.getSelectedStory().id!!,navController)
                        }
                    )

            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(20.dp))
                    .clickable {
                        viewModel.isModifyStoryClicked = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "취소", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}



@Composable
fun EssayPager(pagerState: PagerState, viewModel: MyLogViewModel, navController: NavController,writingViewModel: WritingViewModel) {
    HorizontalPager(state = pagerState, modifier = Modifier.padding(top = 20.dp)) { page ->
        when (page) {
            0 -> EssayListPage1(viewModel,pagerState,navController,writingViewModel)
            1 -> EssayListPage2(viewModel,pagerState,navController,writingViewModel)
            2 -> StoryListPage(viewModel,navController)
        }
    }
}

@Composable
fun ModifyOrDeleteBox(
    viewModel: MyLogViewModel,
    navController: NavController,
    writingViewModel: WritingViewModel,
    essayItem: EssayApi.EssayItem
) {


            Surface(shape = RoundedCornerShape(20), modifier = Modifier.width(180.dp).background(Color(0xFF0E0E0E))) {
                Column(
                    modifier = Modifier.background(Color(0xFF0E0E0E)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OptionItem(text = "수정", Color.White,{
                        writingViewModel.title.value = TextFieldValue(essayItem.title!!)
                        writingViewModel.content = TextFieldValue(essayItem.content!!)
                        writingViewModel.imageUrl = essayItem.thumbnail
                        writingViewModel.isModifyClicked = true
                        writingViewModel.modifyEssayid = essayItem.id!!

                        navController.navigate("WritingPage")

                    },R.drawable.option_modify)
                    HorizontalDivider()
                    OptionItem(text = "삭제", Color.Red,{
                        viewModel.deleteEssay(navController = navController,essayItem.id!!)
                        Log.d(TAG, "ModifyOption: dd")
                    },R.drawable.option_trash)
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }



@Preview
@Composable
fun OutBox(){
    Box(modifier = Modifier
        .background(LinkedInColor, shape = RoundedCornerShape(40.dp))
        , contentAlignment = Alignment.Center){
        Text(
            text = "  OUT  ", fontSize = 12.sp,color=Color.Black
        )
    }
}


