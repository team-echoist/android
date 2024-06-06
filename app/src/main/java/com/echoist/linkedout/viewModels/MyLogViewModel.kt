package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.StoryApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject


@HiltViewModel
class MyLogViewModel @Inject constructor(
    private val storyApi: StoryApi,
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems
) : ViewModel() {

    var isCreateStory by mutableStateOf(false)

    var isModifyStoryClicked by mutableStateOf(false)

    var myEssayList = exampleItems.myEssayList
    var publishedEssayList = exampleItems.publishedEssayList

    var detailEssayBackStack = Stack<EssayApi.EssayItem>()

    var isActionClicked by mutableStateOf(false)

    var detailEssay by mutableStateOf(exampleItems.detailEssay)

    var selectedStory by mutableStateOf(exampleItems.exampleStroy)
    var storyTextFieldTitle by mutableStateOf("")

    var storyList by mutableStateOf<List<Story>>(emptyList())


    //todo 수정필요
    val createStoryEssayItems = exampleItems.exampleEssayList.toList()
    val modifyStoryEssayItems = exampleItems.exampleEssayList.toList()

    val essayIdList by mutableStateOf(mutableStateListOf<Int>())

    fun findEssayInStory() : Story?{
        storyList.forEach { it->
            if (detailEssay.story != null){
                if (it.id == detailEssay.story!!.id){
                    return it
                }

            }
        }

        return null
    }
    fun readPublishEssay() {
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
                Log.d("exception", e.localizedMessage.toString() + Token.accessToken)
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

    fun readDetailEssay(id: Int, navController: NavController) {
        viewModelScope.launch {
            try {
                val response = essayApi.readDetailEssay(Token.accessToken,id)
                exampleItems.detailEssay = response.body()!!.data.essay
                detailEssay = exampleItems.detailEssay
                Log.d(ContentValues.TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(ContentValues.TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

//                if (response.body()!!.data.previous != null) {
//                    exampleItems.previousEssayList = response.body()!!.data.previous!!.toMutableStateList()
//                    previousEssayList = exampleItems.previousEssayList
//                }
                Log.d(ContentValues.TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

                Log.d(ContentValues.TAG, "readDetailEssay: previouse ${exampleItems.previousEssayList}")
                navController.navigate("MyLogDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(ContentValues.TAG, "readRandomEssays: ${e.message}")
                Log.d(ContentValues.TAG, "readRandomEssays: ${e.cause}")
                Log.d(ContentValues.TAG, "readRandomEssays: ${e.localizedMessage}")

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

    fun readStoryEssayList(){

    }
}
