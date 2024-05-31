package com.echoist.linkedout.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val userApi: UserApi
) : ViewModel() {
    val detailEssay = exampleItems.detailEssay
    var isBadgeClicked by mutableStateOf(false)
    var badgeBoxItem : BadgeBoxItem? by mutableStateOf(null)

    var badgeBoxList = exampleItems.badgeList

    var simpleBadgeList = exampleItems.simpleBadgeList
    var detailBadgeList = exampleItems.detailBadgeList

    fun readSimpleBadgeList(navController: NavController) {
        viewModelScope.launch {
            try {

                val response = userApi.readBadgeList( Token.accessToken)
                simpleBadgeList = response.body()!!.data.badges!!.toMutableStateList()

                Token.accessToken = (response.headers()["authorization"].toString())


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun readDetailBadgeList(navController: NavController) {
        viewModelScope.launch {
            try {

                val response = userApi.readBadgeWithTagsList( Token.accessToken)
                detailBadgeList = response.body()!!.data.badges.toMutableStateList()

                Token.accessToken = (response.headers()["authorization"].toString())

                navController.navigate("BadgePage")

            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

}