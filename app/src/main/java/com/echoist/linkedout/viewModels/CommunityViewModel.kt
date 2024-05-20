package com.echoist.linkedout.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CommunityViewModel : ViewModel() {
    var isClicked by mutableStateOf(false)
}