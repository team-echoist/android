package com.echoist.linkedout.viewModels

import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.ExampleItems
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val exampleItems: ExampleItems
) : ViewModel() {
    val detailEssay = exampleItems.detailEssay
}