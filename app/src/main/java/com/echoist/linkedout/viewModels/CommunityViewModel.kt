package com.echoist.linkedout.viewModels

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
import com.echoist.linkedout.TYPE_COMMUNITY
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    val _isRefreshing = MutableStateFlow(false)
    open val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _randomList = MutableStateFlow<List<EssayApi.EssayItem>>(emptyList())
    val randomList: StateFlow<List<EssayApi.EssayItem>>
    get() = _randomList.asStateFlow()

    var randomEssayList by mutableStateOf(mutableStateListOf<EssayApi.EssayItem>())


    open var _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private var _firstSentences = MutableStateFlow<List<EssayApi.EssayItem>>(emptyList())
    val firstSentences : StateFlow<List<EssayApi.EssayItem>>
        get() = _firstSentences.asStateFlow()

    private var _lastSentences  = MutableStateFlow<List<EssayApi.EssayItem>>(emptyList())
    val lastSentences : StateFlow<List<EssayApi.EssayItem>>
        get() = _lastSentences.asStateFlow()

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
    var isReportClicked by mutableStateOf(false)

    var detailEssayBackStack by mutableStateOf(exampleItems.detailEssayBackStack)
    var unSubscribeClicked by mutableStateOf(false)

    var detailEssay by mutableStateOf(exampleItems.detailEssay)
    var subscribeUserList = exampleItems.subscribeUserList
    var userItem = exampleItems.userItem
    var followingList = exampleItems.followingList


    var previousEssayList by mutableStateOf(exampleItems.exampleEmptyEssayList)

    init {
        readRandomEssays()
        readFollowingEssays()
        readOneSentences("first")
        readOneSentences("last")
    }
    open fun refresh(){
        viewModelScope.launch {

            _isRefreshing.emit(true)
            _isLoading.emit(true)
            randomEssayList.clear()
            readRandomEssays()
            readFollowingEssays()
            readOneSentences("first")
            readOneSentences("last")
            _isRefreshing.emit(false)
            _isLoading.emit(false)


        }
    }
    fun textSizeUp(){
        titleTextSize = if (titleTextSize.value <= MAX_TITLE_SIZE) titleTextSize.value.plus(1).sp else titleTextSize
        contentTextSize = if (contentTextSize.value <= MAX_CONTENT_SIZE) contentTextSize.value.plus(1).sp else contentTextSize

    }

    fun textSizeDown(){
        titleTextSize = if (titleTextSize.value >= MIN_TITLE_SIZE) titleTextSize.value.minus(1).sp else titleTextSize
        contentTextSize = if (contentTextSize.value >= MIN_CONTENT_SIZE) contentTextSize.value.minus(1).sp else contentTextSize

    }

    fun getFilteredRecentEssayList() : List<EssayApi.EssayItem>{ // detail essay를 뺀 리스트를 보여줘야할것.
        return exampleItems.recentViewedEssayList.filter { it.id != exampleItems.detailEssay.id }
    }

    fun getFilteredRandomEssayList() : List<EssayApi.EssayItem>{ // detail essay를 뺀 다른 글 리스트를 보여줘야할것.
        return exampleItems.randomList.filter { it.id != exampleItems.detailEssay.id }
    }

    fun getRecentEssayList() : List<EssayApi.EssayItem>{
        return exampleItems.recentViewedEssayList
    }

    fun readRandomEssayList() : List<EssayApi.EssayItem>{
        return exampleItems.randomList
    }

    fun readDetailEssay(): EssayApi.EssayItem {
        Log.d(TAG, "readDetailEssay123123: ${exampleItems.detailEssay.author}")
        return exampleItems.detailEssay
    }
    fun setBackDetailEssay(essay: EssayApi.EssayItem) {
        exampleItems.detailEssay = essay
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

    fun readRandomEssays(limit : Int = 20) {
        viewModelScope.launch {
            try {
                _isLoading.emit(true)
                val response = essayApi.readRandomEssays(Token.accessToken, limit = limit)

                if (response.isSuccessful){
                    exampleItems.randomList = response.body()!!.data.essays.toMutableStateList()
                    _randomList.emit(response.body()!!.data.essays.toMutableStateList())
                    //_randomList.emit(response.body()!!.data.essays.toMutableStateList())
                    response.body()!!.data.essays.forEach{it->
                        randomEssayList.add(it)
                    }

                    Log.d(TAG, "readRandomEssays: 성공인데요${response.body()!!.data.essays.toMutableStateList()}")
                    Log.d(TAG, "readRandomEssays: 성공입니다 아니면 예시 ${exampleItems.randomList}")
                }



                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
                Log.d(TAG, "readRandomEssays: ${e.cause}")
                Log.d(TAG, "readRandomEssays: ${e.localizedMessage}")

            }
            finally {
                _isLoading.emit(false)
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

    var isApifinished by mutableStateOf(false)
    fun readOneSentences(type: String) {
        viewModelScope.launch {
            try {
                val response = essayApi.readOneSentences(Token.accessToken, type = type)

                if (type == "first") exampleItems.firstSentences =
                    response.body()!!.data.essays.toMutableStateList()
                else exampleItems.lastSentences = response.body()!!.data.essays.toMutableStateList()

                Log.d(TAG, "첫문장 request api: 첫문장 개수${response.body()!!.data.essays.size} \n 마지막문장 ${exampleItems.lastSentences}")

                _firstSentences.emit(exampleItems.firstSentences)
                _lastSentences.emit(exampleItems.lastSentences)


            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isApifinished = true
            }
        }
    }

    open fun readDetailEssay(id: Int, navController: NavController,type: String = TYPE_COMMUNITY) {
        viewModelScope.launch {
            try {
                _isLoading.emit(true)

                val response = essayApi.readDetailEssay(Token.accessToken, id,type = type, )
                Log.d("상세 조회 성공", "readDetailEssay: 성공 ${response.body()!!}")
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d("상세 조회 성공", "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

                exampleItems.previousEssayList = response.body()!!.data.anotherEssays!!.essays.toMutableStateList()
                previousEssayList = exampleItems.previousEssayList
                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.previousEssayList}")
                navController.navigate("CommunityDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.e(TAG, "readRandomEssays: ${e.message}")
                Log.e(TAG, "readRandomEssays: ${e.cause}")
                Log.e(TAG, "readRandomEssays: ${e.localizedMessage}")

            }
            finally {
                _isLoading.emit(false)


            }
        }
    }
    fun readDetailRecentEssay(id: Int, navController: NavController,type: String) {
        viewModelScope.launch {
            try {
                val response = essayApi.readDetailEssay(Token.accessToken,id,type)
                exampleItems.detailEssay = response.body()!!.data.essay

                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")
                exampleItems.detailEssayBackStack.push(exampleItems.detailEssay)


                navController.navigate("RecentEssayDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")

            }

        }
    }
    fun readMyBookMarks(navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.emit(true)

                Log.d(TAG, "readMyBookMarks: $isLoading")
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

            }
            finally {
                _isLoading.emit(false)

                Log.d(TAG, "readMyBookMarks: $isLoading")

            }
        }
    }

    fun addBookMark(essayId: Int){
        viewModelScope.launch {
            try {
                bookMarkApi.addBookMark(Token.accessToken,essayId)

                Log.d(TAG, "bookMarkEssayList: 성공입니다  ${detailEssay.title}")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "bookMarkEssayList: ${e.message}")

            }
        }

    }

    fun deleteBookMark(essayId : Int){
        viewModelScope.launch {
            try {
                val deleteEssayId = listOf(essayId)
                val response = bookMarkApi.deleteBookMarks(Token.accessToken,BookMarkApi.RequestDeleteBookMarks(deleteEssayId))
                if (response.isSuccessful){
                    Log.d(TAG, "bookMarkEssayList: 단일 북마크삭제 성공 ${detailEssay.title}")
                    Log.d(TAG, "bookMarkEssayList: 북마크삭제 성공 $deleteEssayId")
                }


                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "bookMarkEssayList: ${e.message}")

            }
            finally {

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

                val response = bookMarkApi.deleteBookMarks(Token.accessToken,BookMarkApi.RequestDeleteBookMarks(deleteEssayId))
                if (response.isSuccessful){
                    //삭제하고 다시 북마크 로딩
                    readMyBookMarks(navController = navController)
                }
                Log.d(TAG, "bookMarkEssayList: 북마크삭제 성공 $deleteEssayId")
                Log.d(TAG, "bookMarkEssayList: 북마크삭제 성공 $deleteEssayId")


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

    var isReportCleared by mutableStateOf(false)
    fun reportEssay(essayId: Int, reportReason : String){
        viewModelScope.launch {
            try {


                val response = essayApi.reportEssay(Token.accessToken, essayId, EssayApi.ReportRequest(reportReason))
                if (response.isSuccessful){
                    Log.d(TAG, "reportEssay: 성공입니다 아니면 예시 ${detailEssay.title}")
                    isReportCleared = true
                }

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "reportEssay: ${e.message}")
                Log.d(TAG, "reportEssay: ${e.cause}")
                Log.d(TAG, "reportEssay: ${e.localizedMessage}")

            }
        }
    }
}
