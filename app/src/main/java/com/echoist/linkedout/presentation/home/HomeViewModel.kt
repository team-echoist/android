package com.echoist.linkedout.presentation.home

import android.content.ContentValues.TAG
import android.content.Context
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
import com.echoist.linkedout.data.api.SignUpApiImpl
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.api.apiCall
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.Release
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.data.repository.HomeRepository
import com.echoist.linkedout.data.repository.TokenRepository
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.presentation.util.getFCMToken
import com.echoist.linkedout.presentation.util.getSSAID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val userDataRepository: UserDataRepository,
    private val userApi: UserApi,
    private val supportApi: SupportApi,
    private val homeRepository: HomeRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var myProfile by mutableStateOf(exampleItems.myProfile)

    var isLoading by mutableStateOf(false)
    var isFirstUser by mutableStateOf(false)
    var latestNoticeId: Int? by mutableStateOf(null) //공지가 있을경우 true, 없을경우 Null

    var updateHistory: SnapshotStateList<Release> = mutableStateListOf()

    var isVisibleGeulRoquis by mutableStateOf(true)
    val isReAuthenticationRequired: StateFlow<Boolean> = tokenRepository.isReAuthenticationRequired

    private val _isUserDeleteApiFinished = MutableStateFlow(false)
    val isUserDeleteApiFinished: StateFlow<Boolean> = _isUserDeleteApiFinished
    private val _isUpdateUserNotificationApiFinished = MutableStateFlow(false)

    private val _isExistLatestUpdate = MutableStateFlow(false)
    val isExistLatestUpdate: StateFlow<Boolean> = _isExistLatestUpdate
    fun updateIsExistLatestUpdate(value: Boolean) {
        _isExistLatestUpdate.value = value
    }

    fun setApiStatusToFalse() {
        _isUserDeleteApiFinished.value = false
        _isUpdateUserNotificationApiFinished.value = false
    }

    fun setReAuthenticationRequired(value: Boolean) {
        tokenRepository.setReAuthenticationRequired(value)
    }

    fun initializeDetailEssay() {
        exampleItems.detailEssay = EssayApi.EssayItem()
    }

    fun setStorageEssay(essayItem: EssayApi.EssayItem) {
        exampleItems.storageEssay = essayItem
    }

    suspend fun requestMyInfo() {
        try {
            val response = userApi.getMyInfo()
            val userinfo = response.data.user.apply {
                essayStats = response.data.essayStats
            }

            userDataRepository.setUserInfo(userinfo)
            Log.i(TAG, "readMyInfo: ${userDataRepository.userInfo}")
            //첫유저인지 판별
            isFirstUser = response.data.user.isFirst == true

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMyInfo(): StateFlow<UserInfo> { // 함수 이름 변경
        return userDataRepository.userInfo
    }


    fun requestRegisterDevice(context: Context) {
        val ssaid = getSSAID(context)
        getFCMToken { token ->
            if (token != null) {
                // 서버에 토큰값 보내기 등의 작업을 여기서 처리할 수 있습니다.
                val body = SignUpApiImpl.RegisterDeviceRequest(ssaid, token)

                apiCall(
                    onSuccess = {
                        Log.i("FCM Token", "ssaid 값 : $ssaid \n FCM token 값 : $token")
                    }
                ) {
                    supportApi.requestRegisterDevice(
                        body
                    )
                }
            } else {
                Log.e("FCM Token", "Failed to fetch token")
                //토큰없으면 기기등록도 안됨
            }
        }
    }

    //최신공지 여부
    fun requestLatestNotice() {
        apiCall(
            onSuccess = { response ->
                latestNoticeId = response.data.newNotice
            },
            onError = { e ->
                Log.e("최신공지 확인", "확인 실패 ${e.message}")
            }
        ) { supportApi.requestLatestNotice() }
    }

    //최신 업데이트 여부
    fun requestLatestUpdate() {
        apiCall(
            onSuccess = { response ->
                _isExistLatestUpdate.value = response.data.newRelease
            },
            onError = { e ->
                Log.e("최신 업데이트 확인", "확인 실패 ${e.message}")
            }
        ) { supportApi.requestLatestUpdate() }
    }

    //업데이트 히스토리
    fun requestUpdatedHistory() {
        isLoading = true

        apiCall(
            onSuccess = { response ->
                updateHistory = response.data.releases.toMutableStateList()
            },
            finally = {
                isLoading = false
            }
        ) { supportApi.readUpdatedHistories() }
    }
    //튜토리얼 건너뛰기
    fun requestFirstUserToExistUser() {
        viewModelScope.launch {
            homeRepository.requestFirstUserToExistUser()
        }
    }

    var isCheckFinished by mutableStateOf(false)
    var isExistUnreadAlerts by mutableStateOf(false)
    fun requestUnreadAlerts() {
        apiCall(onSuccess = { response ->
            isExistUnreadAlerts = response.data
            Log.d(TAG, "안읽은 알림 여부: ${response.data}")
        }, finally = { isCheckFinished = true }) {
            supportApi.readUnreadAlerts()
        }
    }

    var geulRoquisUrl by mutableStateOf("")
    fun requestGuleRoquis() {
        apiCall(onSuccess =
        { response ->
            geulRoquisUrl = response.data.url
            Log.d("글로키 api", "성공: $geulRoquisUrl")
        },
            onError =
            { e ->
                Log.e("글로키 api", "에러 ${e.message}")
            }) { supportApi.readGeulroquis() }
    }

    fun requestUserReActivate() {
        apiCall { userApi.requestReactivate() }
    }

    fun requestUserDelete() {
        apiCall(onSuccess = {
            Log.d("유저 즉시 탈퇴", "성공")
            _isUserDeleteApiFinished.value = true
        }) {
            userApi.requestDeleteUser()
        }
    }
}


