package com.echoist.linkedout.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.data.Notice
import com.echoist.linkedout.formatDateTime
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.parseAndFormatDateTime
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SupportViewModel

@Composable
fun NoticePage(
    navController: NavController,
    viewModel: SupportViewModel
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.requestNoticesList()
    }

    // 테마 적용
    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("공지사항", navController)
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
            ) {

                if (!viewModel.noticeList.isEmpty()) {
                    viewModel.noticeList.forEach {notice->
                        NoticeItem(notice){viewModel.requestDetailNotice(notice.id,navController)}
                    }
                }


            }
            if (viewModel.noticeList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "공지사항이 없습니다.", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun NoticeItem(notice: Notice,onClickItem :()->Unit){

    LinkedOutTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF0E0E0E))
            .clickable {onClickItem() }
            ){
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = notice.title,
                    modifier = Modifier.weight(4f),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
                Text(text = formatDateTime(notice.createdDate) , fontSize = 10.sp,color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp), contentAlignment = Alignment.CenterEnd){
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "arrowforward", modifier = Modifier.size(20.dp))

            }
        }

    }
}

@Composable
fun NoticeDetailPage(navController: NavController, notice : Notice){
    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("공지사항", navController)
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
            ) {

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = notice.title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = notice.content ?: "내용이 없습니다. 하지만 없다면 서버오류입니다.", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = parseAndFormatDateTime(notice.createdDate) , fontSize = 12.sp,color = Color.Gray)


            }
        }
    }
}