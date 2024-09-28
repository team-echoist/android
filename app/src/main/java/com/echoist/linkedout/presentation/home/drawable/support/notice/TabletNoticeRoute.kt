package com.echoist.linkedout.presentation.home.drawable.support.notice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.util.parseAndFormatDateTime

@Composable
fun TabletNoticeRoute(
    onBackPressed: () -> Unit,
    onClickNotice: (Int) -> Unit,
    viewModel: NoticeViewModel = hiltViewModel()
) {
    val searchUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.requestNoticesList()
    }

    Scaffold(
        topBar = {
            TabletDrawableTopBar(
                title = "공지사항",
                isBack = true
            ) { onBackPressed() }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
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
                                onClickNotice(item.id)
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
fun TabletNoticeDetailRoute(
    noticeId: Int,
    viewModel: NoticeViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val notice by viewModel.notice.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.requestDetailNotice(noticeId)
    }

    Scaffold(
        topBar = {
            TabletDrawableTopBar(
                title = "공지사항",
                isBack = true
            ) { onBackPressed() }
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