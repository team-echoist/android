package com.echoist.linkedout.presentation.home.drawable.support

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.dto.Alert
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.Inquiry
import com.echoist.linkedout.data.dto.Notice
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.essay.write.Token
import com.echoist.linkedout.presentation.util.TYPE_RECOMMEND
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val essayApi: EssayApi,
    private val supportApi: SupportApi
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var alertList: SnapshotStateList<Alert> = mutableStateListOf()

    private val _inquiryList = MutableStateFlow<List<Inquiry>>(emptyList())
    val inquiryList: StateFlow<List<Inquiry>>
        get() = _inquiryList.asStateFlow()

    private val _navigateToCommunityDetail = MutableStateFlow(false)
    val navigateToCommunityDetail: StateFlow<Boolean> get() = _navigateToCommunityDetail.asStateFlow()

    private val _navigateToLinkedOutSupport = MutableStateFlow(false)
    val navigateToLinkedOutSupport: StateFlow<Boolean> get() = _navigateToLinkedOutSupport.asStateFlow()

    fun onNavigated() {
        _navigateToCommunityDetail.value = false
        _navigateToLinkedOutSupport.value = false
    }

    fun readMyProfile(): UserInfo {
        return exampleItems.myProfile
    }

    fun readAlertsList() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readAlertsList()
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    alertList = response.body()!!.data.alerts.toMutableStateList()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false

            }
        }
    }

    fun readAlert(alertId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readAlert(alertId)
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun readDetailEssay(id: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = essayApi.readDetailEssay(
                    id,
                    TYPE_RECOMMEND
                )
                exampleItems.detailEssay = response.body()!!.data.essay
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

                if (response.body()!!.data.anotherEssays != null) {
                    exampleItems.previousEssayList =
                        response.body()!!.data.anotherEssays!!.essays.toMutableStateList()
                }
                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

                Log.d(TAG, "readDetailEssay: anotherEssays ${exampleItems.previousEssayList}")
                _navigateToCommunityDetail.value = true

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {
                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun writeInquiry(title: String, content: String, type: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = Inquiry(title = title, content = content, type = type)
                val response = supportApi.writeInquiry(body)
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    _navigateToLinkedOutSupport.value = true

                    Log.d("문의 작성 성공", "${response.code()}")
                } else {
                    Log.e("문의 작성 에러", "실패: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("문의 작성 에러", "${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun readInquiryList() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readInquiries()
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    //todo alert 성공!
                    _inquiryList.emit(response.body()!!.data)
                    Log.d(TAG, "readInquiryList: $inquiryList")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun readDetailInquiry(inquiryId: Int): Inquiry? {
        isLoading = true
        return try {
            val response =
                supportApi.readInquiryDetail(inquiryId)
            if (response.isSuccessful) {
                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken

                response.body()?.data // 성공 시 response.body()?.data를 반환
            } else {
                null // 실패 시 null 반환
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // 예외 발생 시 null 반환
        } finally {
            isLoading = false
        }
    }
}