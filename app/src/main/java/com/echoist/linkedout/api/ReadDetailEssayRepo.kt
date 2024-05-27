package com.echoist.linkedout.api

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.Token
import javax.inject.Inject

class ReadDetailEssayRepo @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems
){
    suspend fun readDetailEssay(essayId: Int) {
        try {
            val response = essayApi.readDetailEssay(Token.accessToken,essayId)
            exampleItems.detailEssay = response.body()!!.data.essay
            Log.d(ContentValues.TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
            Log.d(ContentValues.TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

            if (response.body()!!.data.previous != null) {
                exampleItems.previousEssayList = response.body()!!.data.previous!!.toMutableStateList()
            }
            Log.d(TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

            Log.d(TAG, "readDetailEssay: previouse ${exampleItems.previousEssayList}")

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
