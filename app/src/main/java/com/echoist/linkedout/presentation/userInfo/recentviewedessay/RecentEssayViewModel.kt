package com.echoist.linkedout.presentation.userInfo.recentviewedessay

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.presentation.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentEssayViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems
) : ViewModel() {

    private val _recentEssayList = MutableStateFlow<List<EssayApi.EssayItem>>(emptyList())
    val recentEssayList: StateFlow<List<EssayApi.EssayItem>> = _recentEssayList

    init {
        loadRecentEssayList()
    }

    private fun loadRecentEssayList() {
        viewModelScope.launch {
            try {
                val response = essayApi.readRecentEssays()
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    _recentEssayList.value = response.body()!!.data.essays
                    exampleItems.recentViewedEssayList =
                        response.body()!!.data.essays.toMutableStateList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            } finally {
            }
        }
    }
}