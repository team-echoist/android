package com.echoist.linkedout.presentation.myLog.mylog

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.BookMarkApi
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.api.StoryApi
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.api.toWritingEssayItem
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.RelatedEssay
import com.echoist.linkedout.data.dto.RelatedEssayResponse
import com.echoist.linkedout.data.dto.Story
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.essay.write.Token
import com.echoist.linkedout.presentation.util.TYPE_PRIVATE
import com.echoist.linkedout.presentation.util.TYPE_STORY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyLogViewModel @Inject constructor(
    private val userApi: UserApi,
    private val storyApi: StoryApi,
    private val supportApi: SupportApi,
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems, bookMarkApi: BookMarkApi
) : CommunityViewModel(essayApi, exampleItems, bookMarkApi) {

    var storyEssayNumber: Int by mutableIntStateOf(0)

    //스토리 생성할때 true값
    var isCreateStory by mutableStateOf(false)

    var isModifyStoryClicked by mutableStateOf(false)

    var myEssayList by mutableStateOf(exampleItems.myEssayList)
    var publishedEssayList by mutableStateOf(exampleItems.publishedEssayList)

    var isActionClicked by mutableStateOf(false)
    var isExistUnreadAlerts by mutableStateOf(false)

    var storyTextFieldTitle by mutableStateOf("")
    override var storyList by mutableStateOf<List<Story>>(emptyList())

    var createStoryEssayItems by mutableStateOf<List<RelatedEssay>>(emptyList())
    var modifyStoryEssayItems by mutableStateOf<List<RelatedEssay>>(emptyList())
    var essayListInStroy by mutableStateOf<List<EssayApi.EssayItem>>(emptyList())

    private val _navigateToMyLog0 = MutableStateFlow(false)
    val navigateToMyLog0: StateFlow<Boolean> = _navigateToMyLog0

    private val _navigateToMyLog2 = MutableStateFlow(false)
    val navigateToMyLog2: StateFlow<Boolean> = _navigateToMyLog2

    init {
        readMyEssay()
        readPublishEssay()
        readMyStory()
        requestMyInfo()
    }

    fun onNavigatedInit() {
        _navigateToMyLog0.value = false
        _navigateToMyLog2.value = false
    }

    override fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            _isLoading.emit(true)
            myEssayList.clear()
            publishedEssayList.clear()
            readMyEssay()
            readPublishEssay()
            _isRefreshing.emit(false)
            _isLoading.emit(false)
        }
    }

    fun getUserInfo(): UserInfo {
        return exampleItems.myProfile
    }

    fun getSelectedStory(): Story {
        return exampleItems.exampleStroy
    }

    fun setSelectStory(story: Story) {
        exampleItems.exampleStroy = story
    }

    //에세이 option에서 스토리 확인하기
    fun findStoryInEssay(): Story? {
        storyList.forEach {
            if (detailEssay.story != null) {
                if (it.id == detailEssay.story!!.id) {
                    return it
                }
            }
        }
        return null
    }

    //스토리에서 들어갈수있는 에세이 목록 확인.
    fun findEssayInStory(): MutableList<RelatedEssay> {
        val relatedEssayList = mutableStateListOf<RelatedEssay>()
        if (isCreateStory) {
            createStoryEssayItems.forEach {
                if (it.story == getSelectedStory().id) {
                    relatedEssayList.add(it)
                }
            }
        } else {
            modifyStoryEssayItems.forEach {
                if (it.story == getSelectedStory().id) {
                    relatedEssayList.add(it)
                }
            }
        }
        return relatedEssayList
    }


    fun readPublishEssay() {
        Log.d(TAG, "detail : ${detailEssay.title}")
        Log.d(TAG, "detail: ${exampleItems.detailEssay.title}")
        // publishedEssayList.clear()

        viewModelScope.launch {
            _isLoading.emit(true)

            try {
                val response = essayApi.readMyEssay(
                    pageType = "public"
                )
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken

                    response.body()!!.data.essays.forEach { essay ->
                        // publishedEssayList에 이미 해당 essay가 존재하는지 확인
                        if (publishedEssayList.none { it.id == essay.id }) {
                            publishedEssayList.add(essay)
                            Log.d("TAG", "Added to publishedEssayList: ${essay.title}")
                        } else {
                            Log.d("TAG", "Duplicate essay skipped: ${essay.title}")
                        }
                    }

                    // 확인을 위해 리스트를 출력
                    publishedEssayList.forEach {
                        Log.d("TAG", "readPublishedEssay: ${it.title}")
                        if (it.story != null)
                            Log.d("TAG", "readPublishedEssay: ${it.story!!.name}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("exception", e.localizedMessage?.toString() + Token.accessToken)
                Log.d("exception", e.message.toString() + Token.accessToken)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun readMyEssay() {
        viewModelScope.launch {
            _isLoading.emit(true)

            try {
                val response = essayApi.readMyEssay(
                    pageType = TYPE_PRIVATE
                )
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    Log.d("TAG", "readMyEssay: ${response.body()!!.data.essays}")
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken

                    response.body()!!.data.essays.forEach { essay ->
                        // publishedEssayList에 이미 해당 essay가 존재하는지 확인
                        if (myEssayList.none { it.id == essay.id }) {
                            myEssayList.add(essay)
                            Log.d("TAG", "Added to publishedEssayList: ${essay.title}")
                        } else {
                            Log.d("TAG", "Duplicate essay skipped: ${essay.title}")
                        }
                    }
                }
                myEssayList.forEach {
                    Log.d("TAG", "readMyEssay: ${it.title}")
                }
                Log.d("TAG", "readMyEssay: ${myEssayList.size}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("exception", (e.localizedMessage?.toString() ?: "") + Token.accessToken)
                Log.d("exception", e.message.toString() + Token.accessToken)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun readNextEssay(
        currentEssayId: Int,
        pageType: String,
        storyId: Int
    ) {
        viewModelScope.launch {
            try {
                val response = essayApi.readNextEssay(
                    currentEssayId,
                    pageType = pageType,
                    storyId = if (pageType == TYPE_STORY) storyId else null
                )

                if (response.body()!!.data != null) {
                    exampleItems.detailEssay = response.body()!!.data!!.essay
                    detailEssay = exampleItems.detailEssay
                    Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")

                    if (response.body()!!.data!!.anotherEssays != null) {
                        exampleItems.previousEssayList =
                            response.body()!!.data!!.anotherEssays!!.essays.toMutableStateList()
                        previousEssayList = exampleItems.previousEssayList
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun requestMyInfo() {
        viewModelScope.launch {
            try {
                val response = userApi.getMyInfo()

                Log.d(TAG, "readMyInfo: suc1")
                exampleItems.myProfile = response.data.user
                exampleItems.myProfile.essayStats = response.data.essayStats
                Log.i(TAG, "readMyInfo: ${exampleItems.myProfile}")
            } catch (e: Exception) {
                Log.d(TAG, "readMyInfo: error err")
                e.printStackTrace()
                Log.d(TAG, e.message.toString())
                Log.d(TAG, e.cause.toString())
            }
        }
    }

    fun updateEssayToPublished() {
        viewModelScope.launch {
            try {
                val item = exampleItems.detailEssay.toWritingEssayItem().copy(status = "published")
                val response = essayApi.modifyEssay(
                    exampleItems.detailEssay.id!!,
                    item
                )
                if (response.isSuccessful) {
                    isActionClicked = false
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    _navigateToMyLog0.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateEssayToLinkedOut() {
        viewModelScope.launch {
            try {
                val item = exampleItems.detailEssay.toWritingEssayItem().copy(status = "linkedout")
                val response = essayApi.modifyEssay(
                    exampleItems.detailEssay.id!!,
                    item
                )
                if (response.isSuccessful) {
                    isActionClicked = false
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    _navigateToMyLog0.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteEssay(id: Int) {
        viewModelScope.launch {
            try {
                val response = essayApi.deleteEssay(id)
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    isActionClicked = false
                    myEssayList.removeIf { it.id == id }
                    publishedEssayList.removeIf { it.id == id }
                    _navigateToMyLog0.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun readMyStory() {
        viewModelScope.launch {
            try {
                val response =
                    storyApi.readMyStories()

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    storyList = response.body()!!.data.stories.toMutableStateList()
                } else {
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun deleteMyStory(storyId: Int) {
        viewModelScope.launch {
            try {
                val response = storyApi.deleteStory(storyId)
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    storyList = storyList.filter { it.id != storyId }
                    _navigateToMyLog2.value = true
                } else {
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun createStory(essayidList: List<Int>) {
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle, essayidList)
                val response =
                    storyApi.createStory(storyData)

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    _navigateToMyLog2.value = true
                    storyTextFieldTitle = ""
                    essayIdList.clear()
                } else {
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun modifyStory(
        essayidList: List<Int>
    ) {
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle, essayidList)

                val response = storyApi.modifyStory(
                    getSelectedStory().id!!,
                    storyData
                )
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    storyTextFieldTitle = ""
                    essayIdList.clear()
                    _navigateToMyLog2.value = true
                    Log.e("스토리 수정 성공", "${response.code()}")
                } else {
                    Log.e("스토리 수정 실패", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("스토리 수정 실패", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun modifyEssayInStory() {
        viewModelScope.launch {
            try {
                val response = storyApi.modifyEssayInStory(
                    detailEssay.id!!,
                    getSelectedStory().id!!
                )

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    isActionClicked = false
                    _navigateToMyLog0.value = true
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                } else {
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun deleteEssayInStory() {
        viewModelScope.launch {
            try {
                val response = storyApi.deleteEssayInStory(
                    detailEssay.id!!
                )

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    isActionClicked = false
                    _navigateToMyLog2.value = true
                    storyTextFieldTitle = ""
                    essayIdList.clear()
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                } else {
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun readDetailEssay(id: Int, type: String) {
        viewModelScope.launch {
            try {
                val response =
                    essayApi.readDetailEssay(id, type = type)
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d(TAG, "디테일 에세이 타입 : ${detailEssay.status}")
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

                if (response.body()!!.data.anotherEssays != null) {
                    exampleItems.previousEssayList =
                        response.body()!!.data.anotherEssays!!.essays.toMutableStateList()
                    previousEssayList = exampleItems.previousEssayList
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
            }
        }
    }

    fun readDetailEssayInStory(
        id: Int,
        number: Int,
        type: String,
        storyId: Int
    ) {
        viewModelScope.launch {
            try {
                val response = essayApi.readDetailEssay(
                    id,
                    type,
                    storyId
                )
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")

                storyEssayNumber = number
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
            }
        }
    }

    //스토리 생성 // 스토리 수정 시 selectedEssay 받아오기
    private var limit = 40
    fun readStoryEssayList() {
        modifyStoryEssayItems = listOf()
        createStoryEssayItems = listOf()

        viewModelScope.launch {
            try {
                var response: Response<RelatedEssayResponse>? = null
                Log.d(
                    TAG,
                    "readStoryEssayList: ${getSelectedStory().name},${getSelectedStory().id}"
                )
                response = if (!isCreateStory) { //스토리 편집하기를 누른경우 selectedStory가 존재할것.
                    storyApi.readStoryEssayList(
                        getSelectedStory().id,
                        limit = limit
                    )
                } else {
                    storyApi.readStoryEssayList(
                        limit = limit
                    )
                }

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    Log.d(TAG, "readStoryEssayList: ${getSelectedStory()}")
                    if (isCreateStory) {
                        createStoryEssayItems = response.body()!!.data.essays
                        if (createStoryEssayItems.size >= limit)
                            limit += 20
                    } else //스토리 수정을 누른경우
                    {
                        modifyStoryEssayItems = response.body()!!.data.essays
                        if (modifyStoryEssayItems.size >= limit)
                            limit += 20
                    }
                } else {
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("writeEssayApiError", "An error occurred: ${e.message}")
            }
        }
    }

    fun readEssayListInStory() {
        viewModelScope.launch {
            try {
                val response =
                    essayApi.readMyEssay(pageType = TYPE_STORY, storyId = getSelectedStory().id!!)

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    essayListInStroy = response.body()!!.data.essays.toMutableStateList()
                } else {
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("writeEssayApiError", "An error occurred: ${e.message}")
            }
        }
    }

    fun requestUnreadAlerts() {
        viewModelScope.launch {
            try {
                val response = supportApi.readUnreadAlerts()
                if (response.isSuccessful) {
                    isExistUnreadAlerts =
                        response.body()!!.data
                    Log.d(TAG, "안읽은 알림 여부: ${response.body()!!.data}")
                }
            } catch (e: Exception) {
                Log.e("안읽은 알림 여부", "에러")
            }
        }
    }
}