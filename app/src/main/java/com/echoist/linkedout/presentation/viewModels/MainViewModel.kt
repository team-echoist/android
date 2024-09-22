package com.echoist.linkedout.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.presentation.util.isDateAfterToday
import com.echoist.linkedout.presentation.mobile.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val supportApi: SupportApi,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _isVersionMatching = MutableStateFlow(true)
    val isVersionMatching: StateFlow<Boolean> get() = _isVersionMatching

    fun startDestination(): String {
        val isOnboardingFinished = userDataRepository.getIsOnboardingFinished()
        val isAutoLoginClicked = userDataRepository.getClickedAutoLogin()
        val tokenValidTime = userDataRepository.getRefreshTokenValidTime()
        val tokens = userDataRepository.getTokensInfo()

        return when {
            isAutoLoginClicked && isDateAfterToday(tokenValidTime) -> {
                Token.accessToken = tokens.accessToken
                Token.refreshToken = tokens.refreshToken
                "${Routes.Home}/200"
            }

            isOnboardingFinished -> Routes.LoginPage
            else -> Routes.OnBoarding
        }
    }

    fun requestAppVersion() {
        _isVersionMatching.value = true
        viewModelScope.launch {
            try {
                val response = supportApi.requestAppVersion()
                if (response.isSuccessful) {
                    val currentVersion = BuildConfig.VERSION_NAME
                    val latestVersion = response.body()!!.data.versions.android_mobile

                    if (currentVersion == latestVersion)
                        Log.d("버전 일치 확인", "버전 일치. 최신 버전 : $latestVersion\n현재 버전 : $currentVersion")
                    else {
                        //버전 불일치 시 플레이 스토어로 이동
                        Log.e("버전 일치 확인", "버전 불일치. 최신 버전 : $latestVersion\n현재 버전 : $currentVersion")
                        _isVersionMatching.value = false
                    }
                } else {
                    Log.e("앱 버전 체크 실패", "${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("앱 버전 체크 실패", e.message.toString())
            }
        }
    }
}