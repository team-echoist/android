package com.echoist.linkedout.presentation.userInfo.recentviewedessay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.community.EssayListItem

@Composable
fun TabletRecentEssayScreen(
    navController: NavController,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val items = viewModel.getRecentEssayList()
    Box(
        Modifier
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth(0.6f)
        ) {
            items(items) { it ->
                EssayListItem(
                    item = it,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "최근 본 글이 없습니다.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            }
        }
    }
}