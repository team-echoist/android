package com.echoist.linkedout.page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SupportViewModel

@Composable
fun NoticePage(
    navController: NavController,
    viewModel: SupportViewModel = hiltViewModel()
) {
    // viewModel을 사용해 데이터를 가져오기
    viewModel.readInquiryList()
    val inquiryList by viewModel.inquiryList.collectAsState()

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


            }
        }
    }
}

@Preview
@Composable
fun NoticeItem(){

}