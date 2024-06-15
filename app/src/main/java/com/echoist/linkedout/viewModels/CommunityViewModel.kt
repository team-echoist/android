package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.MAX_CONTENT_SIZE
import com.echoist.linkedout.MAX_TITLE_SIZE
import com.echoist.linkedout.MIN_CONTENT_SIZE
import com.echoist.linkedout.MIN_TITLE_SIZE
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

enum class SentenceInfo {
    First, Last
}

@HiltViewModel
open class CommunityViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems,
    private val bookMarkApi: BookMarkApi
) : ViewModel() {

    open var bookMarkEssayList by mutableStateOf(exampleItems.exampleEmptyEssayList)

    open var searchingText by mutableStateOf("")

    open var storyList by mutableStateOf<List<Story>>(exampleItems.storyList)

    open var titleTextSize by mutableStateOf(24.sp)
    open var contentTextSize by mutableStateOf(16.sp)

    open var essayIdList by mutableStateOf(mutableStateListOf<Int>())


    var isClicked by mutableStateOf(false)
    var isSavedEssaysModifyClicked by mutableStateOf(false)
    var sentenceInfo by mutableStateOf(SentenceInfo.First)
    var currentClickedUserId by mutableStateOf<Int?>(null) // Add this line
    var isOptionClicked by mutableStateOf(false)
    var detailEssayBackStack = Stack<EssayApi.EssayItem>()
    var unSubscribeClicked by mutableStateOf(false)

    var detailEssay by mutableStateOf(exampleItems.detailEssay)
    var randomList = exampleItems.randomList
    var subscribeUserList = exampleItems.subscribeUserList
    var userItem = exampleItems.userItem
    var followingList = exampleItems.followingList
    var firstSentences = exampleItems.firstSentences
    var lastSentences = exampleItems.lastSentences
    var previousEssayList by mutableStateOf(exampleItems.exampleEmptyEssayList)

    fun textSizeUp(){
        titleTextSize = if (titleTextSize.value <= MAX_TITLE_SIZE) titleTextSize.value.plus(1).sp else titleTextSize
        contentTextSize = if (contentTextSize.value <= MAX_CONTENT_SIZE) contentTextSize.value.plus(1).sp else contentTextSize

    }

    fun textSizeDown(){
        titleTextSize = if (titleTextSize.value >= MIN_TITLE_SIZE) titleTextSize.value.minus(1).sp else titleTextSize
        contentTextSize = if (contentTextSize.value >= MIN_CONTENT_SIZE) contentTextSize.value.minus(1).sp else contentTextSize

    }
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

    fun readRandomEssays() {
        viewModelScope.launch {
            try {
                val response = essayApi.readRandomEssays(Token.accessToken)
                exampleItems.randomList = response.body()!!.data.essays.toMutableStateList()
                Log.d(
                    TAG,
                    "readRandomEssays: 성공인데요${response.body()!!.data.essays.toMutableStateList()}"
                )

                Log.d(TAG, "readRandomEssays: 성공입니다 아니면 예시 ${exampleItems.randomList}")
                randomList = exampleItems.randomList


                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
                Log.d(TAG, "readRandomEssays: ${e.cause}")
                Log.d(TAG, "readRandomEssays: ${e.localizedMessage}")

            }

        }

    }

    fun readFollowingEssays() {
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

            }

        }

    }

    fun readOneSentences(type: String) {
        viewModelScope.launch {
            try {
                val response = essayApi.readOneSentences(Token.accessToken, type = type)

                if (type == "first") exampleItems.firstSentences =
                    response.body()!!.data.essays.toMutableStateList()
                else exampleItems.lastSentences = response.body()!!.data.essays.toMutableStateList()

                firstSentences = exampleItems.firstSentences
                lastSentences = exampleItems.lastSentences

                Log.d(TAG, "readOneSentences: ${firstSentences[0].title}")
                Log.d(TAG, "readOneSentences: ${lastSentences[0].title}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    open fun readDetailEssay(id: Int, navController: NavController) {
        viewModelScope.launch {
            try {
                val response = essayApi.readDetailEssay(Token.accessToken,id)
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

                if (response.body()!!.data.previous != null) {
                    exampleItems.previousEssayList = response.body()!!.data.previous!!.toMutableStateList()
                    previousEssayList = exampleItems.previousEssayList
                }
                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.previousEssayList}")
                navController.navigate("CommunityDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
                Log.d(TAG, "readRandomEssays: ${e.cause}")
                Log.d(TAG, "readRandomEssays: ${e.localizedMessage}")

            }
        }
    }
    fun readMyBookMarks(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = bookMarkApi.readMyBookMark(Token.accessToken)
                if(response.success){
                    bookMarkEssayList = response.data.essays.toMutableStateList()
                    navController.navigate("CommunitySavedEssayPage")
                }

                Log.d(TAG, "bookMarkEssayList: 성공입니다 아니면 예시 ${exampleItems.randomList}")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "bookMarkEssayList: ${e.message}")
                Log.d(TAG, "bookMarkEssayList: ${e.cause}")
                Log.d(TAG, "bookMarkEssayList: ${e.localizedMessage}")

            }
        }
    }

    fun addBookMark(essayId: Int){
        viewModelScope.launch {
            try {
                bookMarkApi.addBookMark(Token.accessToken,essayId)

                Log.d(ContentValues.TAG, "bookMarkEssayList: 성공입니다  ${detailEssay.title}")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(ContentValues.TAG, "bookMarkEssayList: ${e.message}")
                Log.d(ContentValues.TAG, "bookMarkEssayList: ${e.cause}")
                Log.d(ContentValues.TAG, "bookMarkEssayList: ${e.localizedMessage}")

            }
        }

    }

    fun deleteBookMark(essayId : Int,navController: NavController){
        viewModelScope.launch {
            try {
                val deleteEssayId = listOf(essayId)
                val response = bookMarkApi.deleteBookMarks(Token.accessToken,deleteEssayId)
                if (response.isSuccessful){
                    readMyBookMarks(navController = navController)
                }

                Log.d(TAG, "bookMarkEssayList: 성공입니다 아니면 예시 ${detailEssay.title}")


                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "bookMarkEssayList: ${e.message}")
                Log.d(TAG, "bookMarkEssayList: ${e.cause}")
                Log.d(TAG, "bookMarkEssayList: ${e.localizedMessage}")

            }
        }

    }

    fun deleteBookMarks(deleteItems : List<EssayApi.EssayItem>, navController: NavController){
        viewModelScope.launch {
            try {
                val deleteEssayId : MutableList<Int> = mutableListOf()

                deleteItems.forEach {
                    deleteEssayId.add(it.id!!)
                }

                val response = bookMarkApi.deleteBookMarks(Token.accessToken,deleteEssayId)
                if (response.isSuccessful){
                    //삭제하고 다시 북마크 로딩
                    readMyBookMarks(navController = navController)
                }

                Log.d(TAG, "bookMarkEssayList: 성공입니다 아니면 예시 ${detailEssay.title}")


                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "bookMarkEssayList: ${e.message}")
                Log.d(TAG, "bookMarkEssayList: ${e.cause}")
                Log.d(TAG, "bookMarkEssayList: ${e.localizedMessage}")

            }
        }

    }



}
