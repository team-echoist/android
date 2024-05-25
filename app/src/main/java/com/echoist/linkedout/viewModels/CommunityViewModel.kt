package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

enum class SentenceInfo {
    First,Last
}
@HiltViewModel
class CommunityViewModel @Inject constructor(val essayApi: EssayApi) : ViewModel() {
    var isApiFinished by mutableStateOf(false)
    var searchingText by mutableStateOf("")

    var isClicked by mutableStateOf(false)
    var sentenceInfo by mutableStateOf(SentenceInfo.First)
    var currentClickedUserId by mutableStateOf<Int?>(null) // Add this line
    var isOptionClicked by mutableStateOf(false)
    var detailEssayBackStack = Stack<EssayApi.EssayItem>()
    var unSubscribeClicked by mutableStateOf(false)


    var detailEssay by mutableStateOf(
        EssayApi.EssayItem(
            author = UserInfo(1,"groove"),
            content = "이 에세이는 예시입니다.",
            createdDate = "2024년 04월 28일 16:47",
            id = 1,
            linkedOutGauge = 5,
            status = "published",
            thumbnail = "http 값 있어요~",
            title = "예시 에세이",
            updatedDate = "2024-05-15",
            tags = listOf(EssayApi.Tag(1,"tag"),EssayApi.Tag(1,"tag"),EssayApi.Tag(1,"tag"))
        )
    )
    var userItem by mutableStateOf(
        UserInfo(
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
            detailEssay.copy(
                id = 2,
                title = "예시 에세이 2",
                content = "이 에세이는 예시입니다. 두 번째 에세이입니다.",
                thumbnail = "http://example.com/image2.jpg"
            ),
            detailEssay.copy(
                id = 3,
                title = "예시 에세이 3",
                content = "이 에세이는 예시입니다. 세 번째 에세이입니다.",
                thumbnail = "http://example.com/image3.jpg"
            ),
            detailEssay.copy(
                id = 4,
                title = "예시 에세이 4",
                content = "이 에세이는 예시입니다. 네 번째 에세이입니다.",
                thumbnail = "http://example.com/image4.jpg"
            ),
            detailEssay.copy(
                id = 5,
                title = "예시 에세이 5",
                content = "이 에세이는 예시입니다. 다섯 번째 에세이입니다.",
                thumbnail = "http://example.com/image5.jpg"
            )
        )
    )


    fun findUser() {
        currentClickedUserId?.let { userId ->
            val user = subscribeUserList.find { it.id == userId }
            if (user != null) {
                userItem = user
                Log.d(TAG, "User found: ${userItem.nickname!!}")
            } else {
                Log.d(TAG, "User not found with ID: $userId")
            }
        }
    }

    fun readRandomEssays(){
        viewModelScope.launch {
            try {
                val response = essayApi.readRandomEssays(Token.accessToken, 100)
                randomList = response.body()!!.data.essays.toMutableStateList()
                Log.d(TAG, "readRandomEssays: ${response.body()!!.data.essays.toMutableStateList()}")

                Log.d(TAG, "readRandomEssays: 성공입니다 $randomList")


                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
                Log.d(TAG, "readRandomEssays: ${e.cause}")
                Log.d(TAG, "readRandomEssays: ${e.localizedMessage}")

            } finally {
                isApiFinished = true
            }

        }

    }
}