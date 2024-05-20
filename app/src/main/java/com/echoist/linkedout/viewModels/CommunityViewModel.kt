package com.echoist.linkedout.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.EssayItem
import dagger.hilt.android.lifecycle.HiltViewModel

enum class SentenceInfo {
    First,Last
}
@HiltViewModel
class CommunityViewModel : ViewModel() {
    var isClicked by mutableStateOf(false)
    var sentenceInfo by mutableStateOf(SentenceInfo.First)

    var detailEssay by mutableStateOf(
        EssayItem(
            nickName = "구루브",
            content = "이 에세이는 예시입니다.",
            createdDate = "2024-05-15",
            id = 1,
            linkedOut = true,
            linkedOutGauge = 5,
            published = true,
            thumbnail = null,
            title = "예시 에세이",
            updatedDate = "2024-05-15"
        )
    )

    var randomList by mutableStateOf(mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay))
    var subscribeList by mutableStateOf(mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay,detailEssay,detailEssay,detailEssay))


}