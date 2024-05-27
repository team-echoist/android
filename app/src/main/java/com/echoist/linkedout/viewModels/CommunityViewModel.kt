package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

enum class SentenceInfo {
    First,Last
}
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems
) : ViewModel() {
    private var isApiFinished by mutableStateOf(false)
    var searchingText by mutableStateOf("")

    var isClicked by mutableStateOf(false)
    var sentenceInfo by mutableStateOf(SentenceInfo.First)
    var currentClickedUserId by mutableStateOf<Int?>(null) // Add this line
    var isOptionClicked by mutableStateOf(false)
    var detailEssayBackStack = Stack<EssayApi.EssayItem>()
    var unSubscribeClicked by mutableStateOf(false)

    var detailEssay by mutableStateOf(exampleItems.detailEssay)
    var randomList by mutableStateOf( exampleItems.randomList)
    var subscribeUserList by mutableStateOf( exampleItems.subscribeUserList)
    var userItem by mutableStateOf( exampleItems.userItem)
    var followingList by mutableStateOf( exampleItems.followingList)



    fun findUser() {
        currentClickedUserId?.let { userId ->
            val user = exampleItems.subscribeUserList.find { it.id == userId }
            if (user != null) {
                exampleItems.userItem = user
                Log.d(TAG, "User found: ${exampleItems.userItem.nickname!!}")
            } else {
                Log.d(TAG, "User not found with ID: $userId")
            }
        }
    }

    fun readRandomEssays(){
        viewModelScope.launch {
            try {
                val response = essayApi.readRandomEssays(Token.accessToken)
                exampleItems.randomList = response.body()!!.data.essays.toMutableStateList()
                Log.d(TAG, "readRandomEssays: 성공인데요${response.body()!!.data.essays.toMutableStateList()}")

                Log.d(TAG, "readRandomEssays: 성공입니다 아니면 예시 ${exampleItems.randomList}")
                randomList = exampleItems.randomList


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

    fun readFollowingEssays(){
        viewModelScope.launch {
            try {
                val response = essayApi.readFollowingEssays(Token.accessToken)
                exampleItems.followingList = response.body()!!.data.essays.toMutableStateList()
                Log.d(TAG, "readRandomEssaysfollow: ${response.body()!!.data.essays.toMutableStateList()}")

                Log.d(TAG, "readRandomEssaysfollow: 성공입니다 아니면 예시 ${exampleItems.randomList}")
                followingList = exampleItems.followingList


                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssaysfollow2: ${e.message}")
                Log.d(TAG, "readRandomEssaysfollow3: ${e.cause}")
                Log.d(TAG, "readRandomEssaysfollow4: ${e.localizedMessage}")

            } finally {
                isApiFinished = true
            }

        }

    }
}