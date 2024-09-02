package com.echoist.linkedout.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.echoist.linkedout.page.settings.BadgeItem
import com.echoist.linkedout.viewModels.SettingsViewModel

@Composable
fun TabletBadgeRoute(
    contentPadding: PaddingValues,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val hasCalledApi = remember { mutableStateOf(false) }
    val badgeBoxItems = viewModel.getDetailBadgeList()

    if (!hasCalledApi.value) {
        hasCalledApi.value = true
    }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 20.dp)
    ) {
        badgeBoxItems.forEach {
            BadgeItem(it, viewModel)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}