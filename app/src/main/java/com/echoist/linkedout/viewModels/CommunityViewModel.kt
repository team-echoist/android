package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.EssayItem
import dagger.hilt.android.lifecycle.HiltViewModel

enum class SentenceInfo {
    First,Last
}
@HiltViewModel
class CommunityViewModel : ViewModel() {
    var isClicked by mutableStateOf(false)
    var sentenceInfo by mutableStateOf(SentenceInfo.First)
    var currentClickedUserId by mutableStateOf<Int?>(null) // Add this line
    var isOptionClicked by mutableStateOf(false)


    var detailEssay by mutableStateOf(
        EssayItem(
            nickName = "구루브",
            content = "이 에세이는 예시입니다.",
            createdDate = "2024년 04월 28일 16:47",
            id = 1,
            linkedOut = true,
            linkedOutGauge = 5,
            published = true,
            thumbnail = "http 값 있어요~",
            title = "예시 에세이",
            updatedDate = "2024-05-15"
        )
    )
    var userItem by mutableStateOf(
        UserApi.UserInfo(
            id = 1,
            nickname = "구루브",
            profileImage = "http",
            password = "1234",
            gender = "male",
            birthDate = "0725"
        )
    )

    var subscribeUserList = mutableStateListOf( //구독유저 리스트 api통신해서 받을것.
        userItem.copy(id = 1, nickname = "꾸르륵"),
        userItem.copy(id = 2, nickname = "카프카"),
        userItem.copy(id = 3, nickname = "스물하나"),
        userItem.copy(id = 4, nickname = "호랑이"),
        userItem.copy(id = 5, nickname = "자두천사"),
        userItem.copy(id = 6, nickname = "무니"),
        userItem.copy(id = 7, nickname = "사자랑이")
    )

    var randomList by mutableStateOf(
        mutableStateListOf(
            detailEssay,
            detailEssay,
            detailEssay,
            detailEssay
        )
    )

    fun findUser() {
        currentClickedUserId?.let { userId ->
            val user = subscribeUserList.find { it.id == userId }
            if (user != null) {
                userItem = user
                Log.d(TAG, "User found: ${userItem.nickname}")
            } else {
                Log.d(TAG, "User not found with ID: $userId")
            }
        }
    }
}