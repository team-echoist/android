package com.echoist.linkedout.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.ExampleItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exampleItems: ExampleItems
) : ViewModel() {
    val detailEssay = exampleItems.detailEssay
    var isBadgeClicked by mutableStateOf(false)
    var badgeBoxItem : BadgeBoxItem? by mutableStateOf(null)
}