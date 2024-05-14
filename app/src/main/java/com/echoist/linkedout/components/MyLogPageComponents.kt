package com.echoist.linkedout.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.data.EssayItem
import com.echoist.linkedout.viewModels.MyLogViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyLogTopAppBar(){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "구루브 님")
        },
        actions = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "", Modifier.size(30.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "",
                Modifier.size(30.dp)
            )

        }
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EssayChips(pagerState: PagerState){
    val coroutineScope = rememberCoroutineScope()
    var color = Color(0xFF686868)

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(26.dp)){
//        HorizontalDivider(modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 25.dp)
//            , thickness = 1.dp
//            , color = Color(0xFF686868))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 17.dp)) {
            Essaychip(
                text = "나만의 글 3",
                65.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)

                    }
                },
                color = if (pagerState.currentPage == 0) Color.White else Color.Gray
            )
            Essaychip(
                text = "발행한 글 3",
                65.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                color = if (pagerState.currentPage == 1) Color.White else Color.Gray
            )
            Essaychip(
                text = "에세이 모음 3",
                78.dp,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Essaychip(
    text: String,
    dividerWidth: Dp,
    clickable: () -> Unit,
    color: Color
    // todo pager number값을넣고 그에따른 색 변화, 터쳐블 변화 주기
){

    Column(modifier = Modifier.padding(end = 12.dp)) {
        Text(
            fontSize = 14.sp,
            text = text,
            color = color, // 색상을 먼저 적용합니다
            modifier = Modifier.clickable { clickable() } // Modifier.clickable을 마지막에 적용합니다
        )

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(modifier = Modifier
            .width(dividerWidth),
            color = color,
            thickness = 2.dp)
    }
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun prevessay(){
    EssayListItem(item = EssayItem("content","2024",1,false,3,false,"","title","2424"))
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EssayListItem(item: EssayItem){
    val color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)){
        if (item.thumbnail != null){
            Box(modifier = Modifier.fillMaxSize()){
                GlideImage(model = item.thumbnail, contentDescription = "")

            }
        }
        //타이틀
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
            Text(
                text = item.title,
                color = color,
                fontSize = 20.sp,

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content,
                maxLines = 3,
                color = color,
                overflow = TextOverflow.Ellipsis
            )

        }
        Box(contentAlignment = Alignment.TopEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, top = 20.dp)) {
            Icon(imageVector = Icons.Default.Commit, contentDescription = "", tint = color,)
        }

        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 20.dp)) {
            Text(text = item.createdDate, fontSize = 10.sp, color = Color(0xFF686868))
        }
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()) {
            HorizontalDivider(color = Color(0xFF686868))
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EssayListItem2(item: EssayItem){
    val color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)){
        if (item.thumbnail != null){
            Box(modifier = Modifier.fillMaxSize()){
                GlideImage(model = item.thumbnail, contentDescription = "")
            }
        }
        //타이틀
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    color = color,
                    fontSize = 20.sp,

                    )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(color = Color(0xFFFFBB36), modifier = Modifier.size(45.dp,18.dp), shape = CircleShape) {

                    Text(text = "Out", textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 1.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content,
                maxLines = 3,
                color = color,
                overflow = TextOverflow.Ellipsis
            )

        }
        Box(contentAlignment = Alignment.TopEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, top = 20.dp)) {
            Icon(imageVector = Icons.Default.Commit, contentDescription = "", tint = color,)
        }

        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 20.dp)) {
            Text(text = item.createdDate, fontSize = 10.sp, color = Color(0xFF686868))
        }
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()) {
            HorizontalDivider(color = Color(0xFF686868))
        }

    }
}

@Composable
fun EssayListPage1(viewModel: MyLogViewModel){
    viewModel.readEssay(false)
    val item1 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item2 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item3 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item4 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item5 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item6 = EssayItem("content","2024",1,false,3,false,"","title","2424")


    val essayList = mutableListOf(item1,item2,item3,item4,item5,item6)

    LazyColumn {
        items(essayList){it->
            EssayListItem(item = it)
        }
    }

}

@Composable
fun EssayListPage2(viewModel: MyLogViewModel){
    viewModel.readEssay(true)
    val item1 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item2 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item3 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item4 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item5 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val item6 = EssayItem("content","2024",1,false,3,false,"","title","2424")
    val essayList = mutableListOf(item1,item2,item3,item4,item5,item6)

    LazyColumn {
        items(essayList){
            EssayListItem2(item = it)
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EssayPager(pagerState: PagerState,viewModel: MyLogViewModel) {
    HorizontalPager(state = pagerState, modifier = Modifier.padding(top = 20.dp)) { page ->
        when (page) {
            0 -> EssayListPage1(viewModel)
            1 -> EssayListPage2(viewModel)
        }
    }
}