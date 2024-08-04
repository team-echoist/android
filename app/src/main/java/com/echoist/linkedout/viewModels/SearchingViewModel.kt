package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems,bookMarkApi: BookMarkApi
) : CommunityViewModel(essayApi, exampleItems,bookMarkApi){

    var searchingEssayList by mutableStateOf(mutableStateListOf<EssayApi.EssayItem>())

    fun readSearchingEssays(keyword : String){
        viewModelScope.launch {

            try {
                val response = essayApi.readSearchingEssays(Token.accessToken,keyword, limit = 30)
                searchingEssayList = response.data.essays.toMutableStateList()
                Log.d(ContentValues.TAG, "SearchingBar: $searchingText")


            } catch (e: Exception) {
                e.printStackTrace()
                TODO("Not yet implemented")
            }

        }
    }
}
