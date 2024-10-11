package com.echoist.linkedout.presentation.userInfo.badge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TabletBadgeRoute(
    viewModel: BadgeViewModel = hiltViewModel()
) {
    val badgeList by viewModel.badgeList.collectAsState()

    Box {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            badgeList.forEach { item ->
                BadgeItem(item) {
                    viewModel.requestBadgeLevelUp(item)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (viewModel.isLevelUpSuccess && viewModel.levelUpBadgeItem != null) {
            BadgeLevelUpSuccess(viewModel.levelUpBadgeItem!!)
        }
    }
}