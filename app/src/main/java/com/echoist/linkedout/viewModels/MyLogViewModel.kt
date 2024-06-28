package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.StoryApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.RelatedEssay
import com.echoist.linkedout.data.RelatedEssayResponse
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MyLogViewModel @Inject constructor(
    private val storyApi: StoryApi,
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems, bookMarkApi: BookMarkApi
) : CommunityViewModel(essayApi, exampleItems,bookMarkApi) {

    var myProfile by mutableStateOf(exampleItems.myProfile)

    //스토리 생성할때 true값
    var isCreateStory by mutableStateOf(false)

    var isModifyStoryClicked by mutableStateOf(false)

    var myEssayList by mutableStateOf(exampleItems.myEssayList)
    var publishedEssayList by mutableStateOf(exampleItems.publishedEssayList)


    var isActionClicked by mutableStateOf(false)

    var selectedStory by mutableStateOf(exampleItems.exampleStroy)


    var storyTextFieldTitle by mutableStateOf("")
    override var storyList by mutableStateOf<List<Story>>(emptyList())


    var simpleBadgeList by mutableStateOf<List<BadgeBoxItem>>(emptyList())

    var createStoryEssayItems by mutableStateOf<List<RelatedEssay>>(emptyList())
    var modifyStoryEssayItems by mutableStateOf<List<RelatedEssay>>(emptyList())

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
                if (it.story == selectedStory.id) {
                    relatedEssayList.add(it)
                }
            }
        }
        else{
            modifyStoryEssayItems.forEach {
                if (it.story == selectedStory.id) {
                    relatedEssayList.add(it)
                }
            }
        }

        return relatedEssayList
    }


    fun readPublishEssay() { //todo 얘를 한스텝 일찍 호출해야할듯
        Log.d(TAG, "detail : ${detailEssay.title}")
        Log.d(TAG, "detail: ${exampleItems.detailEssay.title}")

        myEssayList.clear()
        publishedEssayList.clear()

        viewModelScope.launch {
            try {
                val response =
                    essayApi.readMyEssay(accessToken = Token.accessToken, published = true)
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())


                    response.body()!!.data.essays.forEach {
                        publishedEssayList.add(it)
                    }
                    publishedEssayList.forEach {
                        Log.d("TAG", "readPublishedEssay: ${it.title}")
                        if (it.story !=null)
                        Log.d("TAG", "readPublishedEssay: ${it.story.name}")

                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("exception", e.localizedMessage?.toString() + Token.accessToken)
                Log.d("exception", e.message.toString() + Token.accessToken)
            }
        }
    }

    fun readMyEssay() {
        myEssayList.clear()
        publishedEssayList.clear()


        viewModelScope.launch {
            try {
                val response = essayApi.readMyEssay(accessToken = Token.accessToken)
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    Log.d("TAG", "readMyEssay: ${response.body()!!.data.essays}")
                    Token.accessToken = (response.headers()["authorization"].toString())

                    response.body()!!.data.essays.forEach {
                        myEssayList.add(it)

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
        }
    }


    fun deleteEssay(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = essayApi.deleteEssay(Token.accessToken, exampleItems.detailEssay.id!!)

                Log.d("writeEssayApiSuccess2", "writeEssayApiSuccess: ${response.isSuccessful}")
                Log.d("writeEssayApiFailed", "deleteEssaytoken: ${Token.accessToken}")
                Log.d("writeEssayApiFailed", "deleteEssayid: ${exampleItems.detailEssay.id}")

                Log.d("writeEssayApiFailed", "deleteEssay: ${response.errorBody()}")
                Log.d("writeEssayApiFailed", "deleteEssay: ${response.code()}")


                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    isActionClicked = false
                    navController.navigate("MYLOG") {
                        popUpTo("MYLOG") {
                            inclusive = false
                        }
                    }
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
                    navController.navigate("MYLOG")

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

    fun createStory(navController: NavController){
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle,essayIdList)

                val response = storyApi.createStory(Token.accessToken,storyData)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    selectedStory.name = ""
                    navController.navigate("MYLOG")

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

    fun modifyStory(navController: NavController){
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle,essayIdList)

                val response = storyApi.modifyStory(Token.accessToken,selectedStory.id!!,storyData)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    selectedStory.name = ""
                    navController.navigate("MYLOG")

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

    fun modifyEssayInStory(navController: NavController){
        viewModelScope.launch {
            try {

                val response = storyApi.modifyEssayInStory(Token.accessToken,detailEssay.id!!,selectedStory.id!!)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    selectedStory.name = ""
                    isActionClicked = false
                    navController.navigate("MYLOG")

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
                    selectedStory.name = ""
                    isActionClicked = false
                    navController.navigate("MYLOG")

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

    override fun readDetailEssay(id: Int, navController: NavController) {
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
                //여기서 차이를 둔다.
                navController.navigate("MyLogDetailPage")

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

    //스토리 생성 // 스토리 수정 시 selectedEssay 받아오기
    fun readStoryEssayList(){
        viewModelScope.launch {
            try {
                var response : Response<RelatedEssayResponse>? = null
                Log.d(TAG, "readStoryEssayList: ${selectedStory.name},${selectedStory.id}")
                response = if (!isCreateStory) { //스토리 편집하기를 누른경우 selectedStory가 존재할것.
                    storyApi.readStoryEssayList(Token.accessToken, selectedStory.id)
                } else{
                    storyApi.readStoryEssayList(Token.accessToken)
                }

                if (response.isSuccessful) {

                    Token.accessToken = response.headers()["authorization"].toString()

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
}
