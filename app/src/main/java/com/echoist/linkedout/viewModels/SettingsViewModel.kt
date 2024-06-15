package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.BadgeBoxItemWithTag
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.toBadgeBoxItem
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
) : ViewModel() {
    var isLevelUpSuccess by mutableStateOf(false)

    val detailEssay by mutableStateOf<List<EssayApi.EssayItem>>(emptyList())
    var isBadgeClicked by mutableStateOf(false)
    var badgeBoxItem : BadgeBoxItem? by mutableStateOf(null)

    var simpleBadgeList by mutableStateOf<List<BadgeBoxItem>>(emptyList())
    var detailBadgeList by mutableStateOf<List<BadgeBoxItemWithTag>>(emptyList())

    fun getUserInfo(){
        Log.d(TAG, "getUserInfo: ${exampleItems.myProfile}")
    }

    fun readSimpleBadgeList(navController: NavController) {
        //이게 작동되는지가 중요
        viewModelScope.launch {
            try {

                val response = userApi.readBadgeList( Token.accessToken)
                Log.d(TAG, "readSimpleBadgeList: ${response.body()!!.data.badges}")
                response.body()?.data?.badges!!.let{badges ->
                    simpleBadgeList = badges.map { it.toBadgeBoxItem() }
                }
                Log.d(TAG, "readSimpleBadgeList: $simpleBadgeList")

                Token.accessToken = (response.headers()["authorization"].toString())


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.localizedMessage}")

            }
        }
    }

    fun readDetailBadgeList(navController: NavController) {
        viewModelScope.launch {
            try {

                val response = userApi.readBadgeWithTagsList( Token.accessToken)
                response.body()?.data?.badges!!.let{badges ->
                    detailBadgeList = badges.map { it.toBadgeBoxItem() }
                }
                Log.d(TAG, "readdetailList: $detailBadgeList")

                Token.accessToken = (response.headers()["authorization"].toString())

                navController.navigate("BadgePage")

            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun requestBadgeLevelUp(badgeId : Int) {
        viewModelScope.launch {
            try {

                val response = userApi.requestBadgeLevelUp(Token.accessToken, badgeId)
                Log.d(TAG, "requestBadgeLevelUp: ${Token.accessToken}")

                Token.accessToken = (response.headers()["authorization"].toString())
                if (response.body()!!.success){
                    isLevelUpSuccess = true
                    delay(1000)
                    isLevelUpSuccess = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

}