package com.echoist.linkedout.data.repository

import android.content.SharedPreferences
import com.echoist.linkedout.presentation.util.DISPLAY_INFO
import com.echoist.linkedout.presentation.util.KEY_HOUR_INDEX
import com.echoist.linkedout.presentation.util.KEY_MINUTE_INDEX
import com.echoist.linkedout.presentation.util.KEY_PERIOD_INDEX
import com.echoist.linkedout.data.dto.Tokens
import com.echoist.linkedout.presentation.home.TimeSelectionIndex
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun saveWritingRemindNotification(writingRemindNotification: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("writing_notification", writingRemindNotification)
            apply()
        }
    }

    fun getWritingRemindNotification(): Boolean {
        val displayInfo = sharedPreferences.getBoolean("writing_notification", false)
        return displayInfo
    }

    //디스플레이 화이트, 다크 버전
    fun saveDisplayInfo(displayInfo: String) {
        with(sharedPreferences.edit()) {
            putString(DISPLAY_INFO, displayInfo)
            apply()
        }
    }

    fun getDisplayInfo(): String {
        val displayInfo = sharedPreferences.getString(DISPLAY_INFO, "")
        return displayInfo!!
    }

    //자동로그인 refresh, accessToken 저장
    fun getTokensInfo(): Tokens {
        val refreshToken = sharedPreferences.getString("refresh_token", "")
        val accessToken = sharedPreferences.getString("access_token", "")

        return Tokens(accessToken!!, refreshToken!!)
    }

    fun saveTokensInfo(accessToken: String, refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("refresh_token", refreshToken)
            putString("access_token", accessToken)
            apply()
        }
    }

    //자동로그인 클릭 변수저장
    fun saveClickedAutoLogin(isClickedAutoLogin: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("isClickedAutoLogin", isClickedAutoLogin)
            apply()
        }
    }

    fun getClickedAutoLogin(): Boolean {
        val isClickedAutoLogin = sharedPreferences.getBoolean("isClickedAutoLogin", false)
        return isClickedAutoLogin
    }

    //온보딩 일회성
    fun saveIsOnboardingFinished(isOnboardingFinished: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("isOnboardingFinished", isOnboardingFinished)
            apply()
        }
    }

    fun getIsOnboardingFinished(): Boolean {
        val isOnboardingFinished = sharedPreferences.getBoolean("isOnboardingFinished", false)
        return isOnboardingFinished
    }

    //알림 시간 저장
    fun saveTimeSelection(timeSelection: TimeSelectionIndex) {
        with(sharedPreferences.edit()) {
            putInt(KEY_PERIOD_INDEX, timeSelection.periodIndex)
            putInt(KEY_HOUR_INDEX, timeSelection.hourIndex)
            putInt(KEY_MINUTE_INDEX, timeSelection.minuteIndex)
            apply()
        }
    }

    fun getTimeSelection(): TimeSelectionIndex {
        val periodIndex =
            sharedPreferences.getInt(KEY_PERIOD_INDEX, 0) // 기본값은 0
        val hourIndex = sharedPreferences.getInt(KEY_HOUR_INDEX, 0) // 기본값은 0
        val minuteIndex =
            sharedPreferences.getInt(KEY_MINUTE_INDEX, 0) // 기본값은 0
        return TimeSelectionIndex(periodIndex, hourIndex, minuteIndex)
    }

    //로그인 refresh token 유효기간
    fun saveRefreshTokenValidTime(validTime: String) {
        with(sharedPreferences.edit()) {
            putString("refreshTokenValidTime", validTime)
            apply()
        }
    }

    fun getRefreshTokenValidTime(): String { // yyyy-MM-dd
        val validTime = sharedPreferences.getString("refreshTokenValidTime", "1999-07-25")
        return validTime!!
    }

}