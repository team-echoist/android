package com.echoist.linkedout.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.StoryApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val storyApi: StoryApi,
    private val exampleItems: ExampleItems
) : ViewModel(){

    var essayList by mutableStateOf<List<EssayApi.EssayItem>>(emptyList())
    var storyTextFieldTitle by mutableStateOf("")

    //todo 수정필요
    val essayItems = exampleItems.exampleEssayList.toList()
    val essayIdList by mutableStateOf(mutableStateListOf<Int>())


    fun createStory(navController: NavController){
        viewModelScope.launch {
            try {
                val storyData = StoryApi.StoryData(storyTextFieldTitle,essayIdList)

                val response = storyApi.createStory(Token.accessToken,storyData)

                if (response.isSuccessful) {
                    Token.accessToken = (response.headers()["authorization"].toString())
                    storyTextFieldTitle = ""
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

}