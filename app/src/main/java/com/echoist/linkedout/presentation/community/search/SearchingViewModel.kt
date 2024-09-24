package com.echoist.linkedout.presentation.community.search

import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.BookMarkApi
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.presentation.community.communitymain.CommunityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems,
    bookMarkApi: BookMarkApi
) : CommunityViewModel(essayApi, exampleItems, bookMarkApi) {

    val uiState = MutableStateFlow<UiState>(UiState.Idle)

    fun readSearchingEssays(keyword: String) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                val response = essayApi.readSearchingEssays(keyword, limit = 30)
                uiState.value = UiState.Success(response.data.essays)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.value = UiState.Error("Failed to load essays")
            }
        }
    }

    fun clearUiState() {
        uiState.value = UiState.Idle
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val essays: List<EssayApi.EssayItem>) : UiState()
    data class Error(val message: String) : UiState()
}


