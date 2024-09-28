package com.echoist.linkedout.presentation.home.drawable.support.notice

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.echoist.linkedout.data.dto.Notice
import com.echoist.linkedout.presentation.userInfo.account.SettingTopAppBar
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.formatDateTime
import com.echoist.linkedout.presentation.util.parseAndFormatDateTime

@Composable
fun NoticeScreen(
    navController: NavController,
    viewModel: NoticeViewModel = hiltViewModel()
) {
    val searchUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.requestNoticesList()
    }

    Scaffold(
        topBar = {
            SettingTopAppBar("공지사항", navController)
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            when (searchUiState) {
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    val noticeList = (searchUiState as UiState.Success).noticeList
                    if (noticeList.isEmpty()) {
                        Text(text = "공지사항이 없습니다.", color = Color.Gray)
                    } else {
                        noticeList.forEach { item ->
                            NoticeItem(item) {
                                navController.navigate("${Routes.NoticeDetailPage}/${item.id}")
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = "공지사항을 불러오는데 실패했습니다.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NoticeItem(notice: Notice, onClickItem: () -> Unit) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(Color(0xFF0E0E0E))
        .clickable { onClickItem() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = notice.title,
                modifier = Modifier.weight(4f),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = formatDateTime(notice.createdDate), fontSize = 10.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "arrowforward",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun NoticeDetailPage(
    navController: NavController,
    noticeId: Int,
    viewModel: NoticeViewModel = hiltViewModel()
) {
    val notice by viewModel.notice.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.requestDetailNotice(noticeId)
    }

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
            if (notice == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "공지사항이 없습니다.", color = Color.Gray)
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = notice!!.title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = notice!!.content!!, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = parseAndFormatDateTime(notice!!.createdDate),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}