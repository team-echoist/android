package com.echoist.linkedout.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.echoist.linkedout.api.EssayApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
) : ViewModel(){

    var essayList by mutableStateOf<List<EssayApi.EssayItem>>(emptyList())
    var storyTextFieldTitle by mutableStateOf("")

}