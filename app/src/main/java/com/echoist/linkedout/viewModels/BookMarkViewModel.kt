package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BookMarkViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems,
    private val bookMarkApi: BookMarkApi
) : CommunityViewModel(essayApi,exampleItems) {

    var bookMarkEssayList by mutableStateOf(exampleItems.exampleEmptyEssayList)

    fun readMyBookMarks() {
        viewModelScope.launch {
            try {
                val response = bookMarkApi.readMyBookMark(Token.accessToken)
                if(response.success){
                    //bookMarkEssayList = response.data.essays.toMutableStateList()
                }

                Log.d(ContentValues.TAG, "bookMarkEssayList: 성공입니다 아니면 예시 ${exampleItems.randomList}")

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
}