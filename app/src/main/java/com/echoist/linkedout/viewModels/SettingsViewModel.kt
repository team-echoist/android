package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.BadgeBoxItemWithTag
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.data.toBadgeBoxItem
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
) : ViewModel() {
    var isClickedModifyImage by mutableStateOf(false)

    var newProfile by mutableStateOf(UserInfo())

    var isLevelUpSuccess by mutableStateOf(false)

    var isBadgeClicked by mutableStateOf(false)
    var badgeBoxItem : BadgeBoxItem? by mutableStateOf(null)

    var isLoading by mutableStateOf(false)


    fun getRecentViewedEssayList() : List<EssayApi.EssayItem>{
        return exampleItems.recentViewedEssayList
    }


    fun getMyInfo() : UserInfo{
        Log.d(TAG, "readUserInfo: ${exampleItems.myProfile}")
        return exampleItems.myProfile
    }

    fun readSimpleBadgeList() {
        //이게 작동되는지가 중요
        viewModelScope.launch {
            try {

                val response = userApi.readBadgeList( Token.accessToken)
                Log.d(TAG, "readSimpleBadgeList: ${response.body()!!.data.badges}")
                response.body()?.data?.badges!!.let{badges ->
                    exampleItems.simpleBadgeList = badges.map { it.toBadgeBoxItem() }.toMutableStateList()
                }
                Log.d(TAG, "readSimpleBadgeList: ${exampleItems.simpleBadgeList}")

                Token.accessToken = (response.headers()["authorization"].toString())


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.localizedMessage}")

            }
        }
    }

    fun getSimpleBadgeList() : List<BadgeBoxItem>{
        return exampleItems.simpleBadgeList.toList()
    }

    fun readDetailBadgeList(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = userApi.readBadgeWithTagsList(Token.accessToken)
                response.body()?.data?.badges?.let { badges ->
                    // badges 리스트를 SnapshotStateList로 변환
                    exampleItems.detailBadgeList = badges.map { it.toBadgeBoxItem() }.toMutableStateList() as SnapshotStateList<BadgeBoxItemWithTag>
                }
                Log.d(TAG, "readdetailList: ${exampleItems.detailBadgeList}")

                Token.accessToken = response.headers()["authorization"].toString()

                navController.navigate("BadgePage")
            } catch (e: Exception) {
                e.printStackTrace()
                // API 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun getDetailBadgeList() : List<BadgeBoxItemWithTag>{
        return exampleItems.detailBadgeList.toList()
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

    fun updateMyInfo(userInfo: UserInfo,navController: NavController) {
        viewModelScope.launch {
            isLoading = true
            try {

                val response = userApi.userUpdate(Token.accessToken, userInfo)
                Log.d(TAG, "updateMyInfo: ${userInfo.profileImage}")
                Log.d(TAG, "updateMyInfo: ${newProfile.profileImage}")

                Log.d(TAG, "requestBadgeLevelUp: ${Token.accessToken}")
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())

                    //todo api가 성공한다는 시점에서 바로 바뀌게 만듦.
                    exampleItems.myProfile.profileImage = newProfile.profileImage
                    exampleItems.myProfile.nickname = newProfile.nickname

                    readSimpleBadgeList()
                    getMyInfo()
                    navController.navigate("SETTINGS")
                    newProfile = UserInfo()

                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    fun readRecentEssays(){
        viewModelScope.launch {
            isLoading = true

            try {
                val response = essayApi.readRecentEssays(Token.accessToken)

                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    exampleItems.recentViewedEssayList = response.body()!!.data.essays.toMutableStateList()

                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    fun requestWithdrawal(reasons : List<String>, navController: NavController){
        viewModelScope.launch {
            isLoading = true
            try {

                val body = UserApi.RequestDeactivate(reasons)
                val response = userApi.requestDeactivate(Token.accessToken,body)
                Log.d(TAG, "requestWithdrawal: $reasons")

                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    navController.navigate("LoginPage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("ApiFailed", "Failed to write essay: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

}