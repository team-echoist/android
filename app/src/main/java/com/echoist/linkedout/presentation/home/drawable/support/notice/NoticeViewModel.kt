package com.echoist.linkedout.presentation.home.drawable.support.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.dto.Notice
import com.echoist.linkedout.presentation.essay.write.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val supportApi: SupportApi
) : ViewModel() {

    val uiState = MutableStateFlow<UiState>(UiState.Idle)

    var isLoading = false
    private val _notice = MutableStateFlow<Notice?>(null)
    val notice: StateFlow<Notice?> = _notice

    fun requestNoticesList() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = supportApi.readNotices()
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    uiState.value = UiState.Success(response.body()!!.data.Notices)
                } else {
                    uiState.value = UiState.Error("공지사항을 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.value = UiState.Error("공지사항을 불러오는데 실패했습니다.")
            }
        }
    }

    fun requestDetailNotice(noticeId: Int) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = supportApi.readNoticeDetail(noticeId)
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    _notice.value = response.body()!!.data
                } else {
                    _notice.value = null
                }
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val noticeList: List<Notice>) : UiState()
    data class Error(val message: String) : UiState()
}