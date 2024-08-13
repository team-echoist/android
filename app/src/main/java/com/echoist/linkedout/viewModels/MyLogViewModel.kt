package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.TYPE_PROFILE
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.StoryApi
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.api.toWritingEssayItem
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.RelatedEssay
import com.echoist.linkedout.data.RelatedEssayResponse
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MyLogViewModel @Inject constructor(
    private val storyApi: StoryApi,
    private val supportApi : SupportApi,
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems, bookMarkApi: BookMarkApi
) : CommunityViewModel(essayApi, exampleItems,bookMarkApi) {

    var storyEssayNumber: Int by mutableIntStateOf(0)
    val storyEssayTitle: String by mutableStateOf("")

    //스토리 생성할때 true값
    var isCreateStory by mutableStateOf(false)

    var isModifyStoryClicked by mutableStateOf(false)

    var myEssayList by mutableStateOf(exampleItems.myEssayList)
    var publishedEssayList by mutableStateOf(exampleItems.publishedEssayList)


    var isActionClicked by mutableStateOf(false)

    var storyTextFieldTitle by mutableStateOf("")
    override var storyList by mutableStateOf<List<Story>>(emptyList())


    var simpleBadgeList by mutableStateOf<List<BadgeBoxItem>>(emptyList())

    var createStoryEssayItems by mutableStateOf<List<RelatedEssay>>(emptyList())
    var modifyStoryEssayItems by mutableStateOf<List<RelatedEssay>>(emptyList())
    var essayListInStroy by mutableStateOf<List<EssayApi.EssayItem>>(emptyList())

    init {
        readMyEssay()
        readPublishEssay()
        readMyStory()
    }
    override fun refresh(){
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

    fun getUserInfo() : UserInfo{
        return exampleItems.myProfile
    }
    fun getSelectedStory():Story{
        return exampleItems.exampleStroy
    }
    fun setSelectStory(story: Story){
        exampleItems.exampleStroy = story
    }

    //에세이 option에서 스토리 확인하기
    fun findStoryInEssay() : Story?{
        storyList.forEach {
            if (detailEssay.story != null){
                if (it.id == detailEssay.story!!.id){
                    return it
                }

            }
        }
        return null
    }


    //스토리에서 들어갈수있는 에세이 목록 확인.
    fun findEssayInStory(): MutableList<RelatedEssay> {
        val relatedEssayList = mutableStateListOf<RelatedEssay>()

        if (isCreateStory){
            createStoryEssayItems.forEach {
                if (it.story == getSelectedStory().id) {
                    relatedEssayList.add(it)
                }
            }
        }
        else{
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
                val response = essayApi.readMyEssay(accessToken = Token.accessToken, published = true)
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())

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
            }
            finally {
                _isLoading.emit(false)

            }
        }
    }

 //todo 마지막 index로 가면 무한로딩걸림
    fun readMyEssay() {
        //myEssayList.clear()

        viewModelScope.launch {
            _isLoading.emit(true)

            try {
                val response = essayApi.readMyEssay(accessToken = Token.accessToken, published = false)
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    Log.d("TAG", "readMyEssay: ${response.body()!!.data.essays}")
                    Token.accessToken = (response.headers()["authorization"].toString())


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
            }
            finally {
                _isLoading.emit(false)
            }
        }
    }

    fun updateEssayToPublished(navController: NavController) {
        viewModelScope.launch {
            try {
                val item = exampleItems.detailEssay.toWritingEssayItem().copy(status = "published")
                val response = essayApi.modifyEssay(Token.accessToken,exampleItems.detailEssay.id!!,item)
                if (response.isSuccessful){
                    isActionClicked = false
                    Token.accessToken = (response.headers()["authorization"].toString())
                    navController.navigate("MYLOG/0")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updateEssayToLinkedOut(navController: NavController){
            viewModelScope.launch {
                try {
                    val item = exampleItems.detailEssay.toWritingEssayItem().copy(status = "linkedout")
                    val response = essayApi.modifyEssay(Token.accessToken,exampleItems.detailEssay.id!!,item)
                    if (response.isSuccessful){
                        isActionClicked = false
                        Token.accessToken = (response.headers()["authorization"].toString())
                        navController.navigate("MYLOG/0")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    fun deleteEssay(navController: NavController,id: Int) {
        viewModelScope.launch {
            try {
                val response = essayApi.deleteEssay(Token.accessToken, id)

                Log.d("writeEssayApiSuccess2", "writeEssayApiSuccess: ${response.isSuccessful}")
                Log.d("writeEssayApiFailed", "deleteEssaytoken: ${Token.accessToken}")


                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    isActionClicked = false
                    navController.navigate("MYLOG/0") {
                        popUpTo("MYLOG") {
                            inclusive = false
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun readMyStory() {
        viewModelScope.launch {
            try {
                val response = storyApi.readMyStories(accessToken = Token.accessToken)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    storyList = response.body()!!.data.stories.toMutableStateList()

                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                } else {
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun deleteMyStory(storyId: Int, navController: NavController) {
        viewModelScope.launch {
            try {
                val response = storyApi.deleteStory(Token.accessToken, storyId)
                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    navController.navigate("MYLOG/2")

                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                } else {
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun createStory(navController: NavController, essayidList: List<Int>){
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle,essayidList)

                val response = storyApi.createStory(Token.accessToken,storyData)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    navController.navigate("MYLOG/2")
                    storyTextFieldTitle = ""
                    essayIdList.clear()

                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                }
                else{
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun modifyStory(navController: NavController,essayidList : List<Int>){ //todo essaylist가 비어있을때는 동작x
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle,essayidList)

                val response = storyApi.modifyStory(Token.accessToken,getSelectedStory().id!!,storyData)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    Log.d("스토리 수정 성공", "스토리 이름 - ${getSelectedStory().name}")
                    Log.d("스토리 수정 성공", "수정할 스토리 제목 - $storyTextFieldTitle")
                    Log.d("스토리 수정 성공", "수정할 에세이 id 리스트 - $essayIdList")

                    storyTextFieldTitle = ""
                    essayIdList.clear()
                    navController.navigate("MYLOG/2")

                    Log.e("스토리 수정 성공", "${response.code()}")
                }
                else{
                    Log.e("스토리 수정 실패", "${response.errorBody()}")
                    Log.e("스토리 수정 실패", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("스토리 수정 실패", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun modifyEssayInStory(navController: NavController){
        viewModelScope.launch {
            try {

                val response = storyApi.modifyEssayInStory(Token.accessToken,detailEssay.id!!,getSelectedStory().id!!)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    isActionClicked = false
                    navController.navigate("MYLOG/0")

                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                }
                else{
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun deleteEssayInStory(navController: NavController){
        viewModelScope.launch {
            try {

                val response = storyApi.deleteEssayInStory(Token.accessToken,detailEssay.id!!)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    isActionClicked = false
                    navController.navigate("MYLOG/2")
                    storyTextFieldTitle = ""
                    essayIdList.clear()

                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                }
                else{
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    override fun readDetailEssay(id: Int, navController: NavController,type: String) {
        viewModelScope.launch {
            try {
                val response = essayApi.readDetailEssay(Token.accessToken,id,type = type , )
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(TAG, "readdetailEssay: 성공인데요${exampleItems.detailEssay.id}")

                if (response.body()!!.data.anotherEssays != null) {
                    exampleItems.previousEssayList = response.body()!!.data.anotherEssays!!.essays.toMutableStateList()
                    previousEssayList = exampleItems.previousEssayList
                }
                Log.d(TAG, "readDetailEssay ${exampleItems.detailEssay}")

                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.previousEssayList}")

                //여기서 차이를 둔다.
                navController.navigate("MyLogDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")

            }
        }
    }

    fun readDetailEssayInStory(id: Int, navController: NavController,number : Int) {
        viewModelScope.launch {
            try {
                val response = essayApi.readDetailEssay(Token.accessToken,id, TYPE_PROFILE)
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")

                storyEssayNumber = number

                //여기서 차이를 둔다.
                navController.navigate("DetailEssayInStoryPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")

            }
        }
    }

    //스토리 생성 // 스토리 수정 시 selectedEssay 받아오기
    fun readStoryEssayList(){
        viewModelScope.launch {
            try {
                var response : Response<RelatedEssayResponse>? = null
                Log.d(TAG, "readStoryEssayList: ${getSelectedStory().name},${getSelectedStory().id}")
                response = if (!isCreateStory) { //스토리 편집하기를 누른경우 selectedStory가 존재할것.
                    storyApi.readStoryEssayList(Token.accessToken, getSelectedStory().id)
                } else{
                    storyApi.readStoryEssayList(Token.accessToken)
                }

                if (response.isSuccessful) {

                    Token.accessToken = response.headers()["authorization"].toString()
                    Log.d(TAG, "readStoryEssayList: ${getSelectedStory()}")

                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                } else {
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }

                //스토리 생성을 누른경우
                if (isCreateStory){
                    createStoryEssayItems = response.body()!!.data.essays
                }
                else //스토리 수정을 누른경우
                    modifyStoryEssayItems = response.body()!!.data.essays
            } catch (e: Exception) {
                Log.e("writeEssayApiError", "An error occurred: ${e.message}")
            }

        }
    }

    fun readEssayListInStory(){
        viewModelScope.launch {
            try {
                var response = essayApi.readMyEssay(Token.accessToken, storyId = getSelectedStory().id!!)

                if (response.isSuccessful) {

                    Token.accessToken = response.headers()["authorization"].toString()
                    essayListInStroy = response.body()!!.data.essays.toMutableStateList()

                } else {
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("writeEssayApiError", "An error occurred: ${e.message}")
            }

        }
    }

    var isExistUnreadAlerts by mutableStateOf(false)
    fun requestUnreadAlerts(){

        viewModelScope.launch {
            try {
                val response = supportApi.readUnreadAlerts(Token.accessToken)
                if (response.isSuccessful){
                    isExistUnreadAlerts =
                        response.body()!!.data
                    Log.d(TAG, "안읽은 알림 여부: ${response.body()!!.data}")
                }
            } catch (e: Exception) {
                Log.e("안읽은 알림 여부","에러")
            }
        }
    }
}
