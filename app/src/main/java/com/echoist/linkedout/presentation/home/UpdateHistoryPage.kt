package com.echoist.linkedout.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.data.dto.Release
import com.echoist.linkedout.presentation.home.home.HomeViewModel
import com.echoist.linkedout.presentation.userInfo.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor

fun groupHistoriesByDate(histories: List<Release>): Map<String, List<Release>> {
    return histories.groupBy { it.createdDate.substring(0, 10) }
}

@Composable
fun UpdateHistoryPage(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    viewModel.requestUpdatedHistory()


    Scaffold(topBar = {
        SettingTopAppBar("업데이트 기록", navController)
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

@Composable
fun UpdateTitleBox(date: String) {
    val formattedDate = date.replace("-", ".")

    Box(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(LinkedInColor, shape = RoundedCornerShape(10))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 23.dp), contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "업데이트 기록", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp), contentAlignment = Alignment.CenterEnd
        ) {
            Text(text = formattedDate, fontSize = 12.sp)
        }
    }
}

@Composable
fun UpdateHistoryBox(date: String, histories: List<Release>) {
    Column(
        Modifier
            .background(Color(0xFF0E0E0E), shape = RoundedCornerShape(4))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        UpdateTitleBox(date)
        Spacer(modifier = Modifier.height(20.dp))
        Column {
            histories.forEach {
                Text(
                    text = "•  ${it.history}",
                    lineHeight = 22.4.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(start = 23.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UpdateHistoryList(histories: List<Release>) {
    val groupedHistories = groupHistoriesByDate(histories)

    Column {
        groupedHistories.forEach { (date, historiesForDate) ->
            UpdateHistoryBox(date, historiesForDate)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
