package com.echoist.linkedout.components

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.TYPE_PROFILE
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel
import kotlinx.coroutines.delay

@Preview
@Composable
fun pre(){
    val viewModel : MyLogViewModel = viewModel()
    Column {

        LastEssayItem(item = viewModel.detailEssay,viewModel, rememberNavController())
        LastEssayPager(viewModel, rememberNavController())
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LastEssayItem(
    item: EssayApi.EssayItem,
    viewModel: MyLogViewModel,
    navController: NavController
) {
    val color = Color.White
    LinkedOutTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {

                viewModel.readDetailEssay(item.id!!, navController, TYPE_PROFILE)
                viewModel.detailEssayBackStack.push(item)
                Log.d(TAG, "pushpush: ${viewModel.detailEssayBackStack}")

            }
            .height(140.dp)) {
            //타이틀
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .weight(7f) // Column 비율 조정
                ) {
                    Row {
                        Text(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            text = item.title!!,
                            color = color,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 20.sp,
                        )
//                    if (item.id == viewModel.detailEssay.id){
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Surface(shape = RoundedCornerShape(60), color = Color.Magenta) {
//                            Text(text = "현재 글",Modifier.padding(start = 5.dp, end = 5.dp))
//                        }
//                    } todo 이전 글 파싱하는방법 찾아야할듯? 아이디를 기준으로 앞에걸 자른다던지

                    }


                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.content!!,
                        lineHeight = 27.2.sp,
                        maxLines = 2,
                        color = color,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                }

                if (item.thumbnail != null) {
                    GlideImage(
                        model = item.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(135.dp)
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .clip(RoundedCornerShape(10.dp)) // 둥근 모서리 적용
                    )
                }
            }


            Box(
                contentAlignment = Alignment.BottomStart, modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, bottom = 10.dp)
            ) {
                Text(text = item.createdDate!!, fontSize = 10.sp, color = Color(0xFF686868))
            }
            Box(
                contentAlignment = Alignment.BottomEnd, modifier = Modifier
                    .fillMaxSize()
            ) {
                HorizontalDivider(color = Color(0xFF686868))
            }

        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LastEssayPager(viewModel: MyLogViewModel, navController: NavController){
    var hasCalledApi by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        if (!hasCalledApi) {

            delay(100)

            hasCalledApi = true
        }
    }

    val pageCount = if (viewModel.detailEssay.status == "published") viewModel.publishedEssayList.size/4 +1 else viewModel.myEssayList.size/4 +1
    val pagerstate = rememberPagerState { pageCount }
    val lastEssayList = viewModel.previousEssayList
//        when (viewModel.readDetailEssay().status){
//        "published" -> viewModel.previousEssayList
//        "private" -> viewModel.previousEssayList
//        else -> { //todo 검토중에 대해선 다시 체크해봐야한다.
//            emptyList<EssayApi.EssayItem>()
//        }
//    }


    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(Color(0xFF1A1A1A)))
        Text(
            text = "이전 글",
            Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp),
            color = Color(0xFF616FED)
        )
        HorizontalDivider(color = Color(0xFF686868))

        //저장된 글 리스트가 비어있다면
        if (lastEssayList.isEmpty()){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(140.dp), contentAlignment = Alignment.Center){
                Text(text = "글이 존재하지 않습니다.", color = Color(0xFF424242), fontSize = 16.sp)
            }
        }
        //이전 글이 존재한다면
        else{
            Column {
                viewModel.previousEssayList.forEach {
                    LastEssayItem(
                        item = it,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

        //4개씩 글을 나누어 이전글을 보여주는 형식

//            HorizontalPager(state = pagerstate) { page ->
//                val startIndex = page * 4
//                val endIndex = minOf(
//                    startIndex + 4,
//                    lastEssayList.size
//                )
//                val pageItems = if (endIndex <= lastEssayList.size) {
//                    lastEssayList.subList(startIndex, endIndex)
//                } else {
//                    lastEssayList.subList(startIndex, lastEssayList.size)
//                }
//
//                Column {
//                    pageItems.forEach { essay ->
//                        LastEssayItem(
//                            item = essay,
//                            viewModel = viewModel,
//                            navController = navController
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(20.dp))
//            Box(
//                modifier = Modifier
//                    .fillMaxSize() /* 부모 만큼 */
//                    .padding(bottom = 60.dp), contentAlignment = Alignment.BottomCenter
//            ) {
//
//                Row(
//                    Modifier
//                        .padding(bottom = 10.dp), //box 안에 있어야하는거같기도?
//                    horizontalArrangement = Arrangement.Center
//                )
//                {
//                    repeat(pageCount) { iteration ->
//                        val color =
//                            if (pagerstate.currentPage == iteration) Color(0xFFE4A89E) else Color.White.copy(
//                                alpha = 0.5f
//                            )
//                        if (pagerstate.currentPage == iteration) {
//                            Box(
//                                modifier = Modifier
//                                    .padding(4.dp)
//                                    .clip(CircleShape)
//                                    .background(color)
//                                    .size(10.dp, 10.dp)
//
//                            )
//
//                        } else {
//                            Box(
//                                modifier = Modifier
//                                    .padding(4.dp)
//                                    .clip(CircleShape)
//                                    .background(color)
//                                    .size(10.dp, 10.dp)
//
//                            )
//                        }
//
//                    }
//                }
//            }
        }



    }
}