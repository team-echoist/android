package com.echoist.linkedout.viewModels

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

    var isModifyStoryClicked by mutableStateOf(false)

     var myEssayList by mutableStateOf(mutableStateListOf<EssayApi.EssayItem>())
     var publishedEssayList by mutableStateOf(mutableStateListOf<EssayApi.EssayItem>())
     var detailEssayBackStack = Stack<EssayApi.EssayItem>()

     var accessToken: String = "token"
     var isActionClicked by mutableStateOf(false)

    var detailEssay by mutableStateOf(exampleItems.detailEssay)

    var storyList = exampleItems.storyList

    fun readPublishEssay() {
        myEssayList.clear()
        publishedEssayList.clear()

        viewModelScope.launch {
            try {
                val response = essayApi.readMyEssay(accessToken = Token.accessToken, published = true)
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    accessToken = (response.headers()["authorization"].toString())

                    Token.accessToken = accessToken

                        response.body()!!.data.essays.forEach {
                            publishedEssayList.add(it)
                        }
                    publishedEssayList.forEach {
                        Log.d("TAG", "readPublishedEssay: ${it.title}")
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
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken

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
                val response = essayApi.deleteEssay(accessToken,exampleItems.detailEssay.id!!)

                Log.d("writeEssayApiSuccess2", "writeEssayApiSuccess: ${response.isSuccessful}")
                Log.d("writeEssayApiFailed", "deleteEssaytoken: ${Token.accessToken}")
                Log.d("writeEssayApiFailed", "deleteEssayid: ${exampleItems.detailEssay.id}")

                Log.d("writeEssayApiFailed", "deleteEssay: ${response.errorBody()}")
                Log.d("writeEssayApiFailed", "deleteEssay: ${response.code()}")


                if (response.isSuccessful) {
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    isActionClicked = false
                    navController.navigate("MYLOG") {
                        popUpTo("MYLOG") {
                            inclusive = false
                        }
                    }
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

    fun readMyStory(){
        viewModelScope.launch {
            try {
                val response = storyApi.readMyStories(accessToken = Token.accessToken)

                if (response.isSuccessful) {
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    storyList = response.body()!!.data.stories.toMutableStateList()

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
}
