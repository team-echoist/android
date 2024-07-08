package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.data.Alert
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val essayApi: EssayApi,
    private val supportApi: SupportApi
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var alertList: SnapshotStateList<Alert> =  mutableStateListOf()

    fun readAlertsList(){
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readAlertsList(Token.accessToken)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    alertList = response.body()!!.data.alerts.toMutableStateList()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isLoading = false

            }
        }
    }

    fun readAlert(alertId :Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readAlert(Token.accessToken,alertId)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isLoading = false
            }
        }
    }

    fun readDetailEssay(id: Int, navController: NavController) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = essayApi.readDetailEssay(Token.accessToken,id)
                exampleItems.detailEssay = response.body()!!.data.essay
                Log.d(ContentValues.TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(ContentValues.TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

                if (response.body()!!.data.previous != null) {
                    exampleItems.previousEssayList = response.body()!!.data.previous!!.toMutableStateList()
                }
                Log.d(ContentValues.TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

                Log.d(ContentValues.TAG, "readDetailEssay: previouse ${exampleItems.previousEssayList}")
                navController.navigate("CommunityDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(ContentValues.TAG, "readRandomEssays: ${e.message}")
                Log.d(ContentValues.TAG, "readRandomEssays: ${e.cause}")
                Log.d(ContentValues.TAG, "readRandomEssays: ${e.localizedMessage}")

            }
            finally {
                isLoading = false
            }
        }
    }
}