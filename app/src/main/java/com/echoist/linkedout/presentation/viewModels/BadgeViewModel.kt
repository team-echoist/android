package com.echoist.linkedout.presentation.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.dto.BadgeBoxItemWithTag
import com.echoist.linkedout.data.dto.toBadgeBoxItem
import com.echoist.linkedout.presentation.mobile.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(
    private val userApi: UserApi
) : ViewModel() {

    private val _badgeList = MutableStateFlow<List<BadgeBoxItemWithTag>>(emptyList())
    val badgeList: StateFlow<List<BadgeBoxItemWithTag>> = _badgeList

    var isLevelUpSuccess by mutableStateOf(false)
    var levelUpBadgeItem: BadgeBoxItemWithTag? by mutableStateOf(null)

    init {
        loadBadgeList()
    }

    private fun loadBadgeList() {
        viewModelScope.launch {
            try {
                val response = userApi.readBadgeWithTagsList()
                response.body()?.data?.badges?.let { badges ->
                    _badgeList.value = badges.map { it.toBadgeBoxItem() }
                }
                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken
            } catch (e: Exception) {
                e.printStackTrace()
                // API 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun requestBadgeLevelUp(badgeItem: BadgeBoxItemWithTag) {
        viewModelScope.launch {
            try {
                val response = userApi.requestBadgeLevelUp(badgeItem.badgeId!!)
                Log.d(ContentValues.TAG, "requestBadgeLevelUp: ${Token.accessToken}")

                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken

                if (response.body()!!.success) {
                    isLevelUpSuccess = true
                    levelUpBadgeItem = badgeItem
                    delay(1000)
                    isLevelUpSuccess = false
                    levelUpBadgeItem = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }
}