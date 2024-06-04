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

    val essayItems = listOf(
        EssayApi.EssayItem(title = "답하기 싫은 질문", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "해맑수록 내 땅", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "한강이 싫어졌다", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "독일에 가고 싶을 때", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "모기가 생기는 계절", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "돈을 쓰면 쓸수록", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "늦게 일어날 때 드는 기분", createdDate = "2024.02.22")
    )

}