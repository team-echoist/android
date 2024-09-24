package com.echoist.linkedout.presentation.home.drawable.linkedoutsupport

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.home.drawable.support.SupportViewModel
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun TabletLinkedOutSupportRoute(
    viewModel: SupportViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onClickInquiry: () -> Unit
) {
    viewModel.readInquiryList()
    val inquiryList by viewModel.inquiryList.collectAsState()
    val commonPadding = Modifier.padding(horizontal = 20.dp)

    LinkedOutTheme {
        Scaffold(
            topBar = {
                TabletDrawableTopBar(
                    title = "링크드아웃 고객센터",
                    isBack = true
                ) {
                    onBackPressed()
                }
            }
        ) { paddingValues ->
            Box(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // 메인 컨텐츠
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "1:1 문의 내역",
                        modifier = commonPadding,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    if (inquiryList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.height(400.dp),
                        ) {
                            items(inquiryList) { inquiry ->
                                InquiryBox(inquiry, viewModel)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "문의 내역이 없습니다.",
                                color = Color(0xFF888888),
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp)
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        shape = RoundedCornerShape(10),
                        modifier = commonPadding
                            .fillMaxWidth()
                            .height(61.dp),
                        onClick = { onClickInquiry() }
                    ) {
                        Text(
                            text = "1:1 문의하기",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}