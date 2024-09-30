package com.echoist.linkedout.presentation.home.drawable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawableViewModel @Inject constructor(
        private val exampleItems: ExampleItems,
        private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _essayCount = MutableStateFlow(List(5) { 0 }) //초기값 0,0,0,0,0
    val essayCount: StateFlow<List<Int>> = _essayCount

    // 유저 주간 링크드아웃 지수
    fun requestUserGraphSummary() {
        viewModelScope.launch {
            homeRepository.requestUserGraphSummary { response ->
                val updatedCounts = response.data.weeklyEssayCounts!!.map { it.count }
                _essayCount.value = updatedCounts
            }
        }
    }

    fun getMyInfo(): UserInfo { // 함수 이름 변경
        return exampleItems.myProfile
    }
}
