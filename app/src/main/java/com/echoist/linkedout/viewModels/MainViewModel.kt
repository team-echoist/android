package com.echoist.linkedout.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.api.SupportApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val supportApi: SupportApi
) : ViewModel() {

    private val _isVersionMatching = MutableStateFlow(true)
    val isVersionMatching: StateFlow<Boolean> get() = _isVersionMatching

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