package com.echoist.linkedout.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.page.home.UpdateHistoryList
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel

@Composable
fun TabletUpdateHistoryRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onCloseClick: () -> Unit
) {
    viewModel.readUpdateHistory()

    LinkedOutTheme {
        Scaffold(topBar = {
            TabletDrawableTopBar(title = "업데이트 기록") {
                onCloseClick()
            }
        }) {
            Column(
                Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                if (!viewModel.updateHistory.isEmpty()) {
                    UpdateHistoryList(viewModel.updateHistory)
                }
            }
            if (viewModel.updateHistory.isEmpty()) {
                if (viewModel.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "업데이트 기록이 없습니다.",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 25.6.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF888888),
                            )
                        )
                    }
                }
            }
        }
    }
}