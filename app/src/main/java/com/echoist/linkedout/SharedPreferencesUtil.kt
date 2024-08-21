package com.echoist.linkedout

import android.content.Context
import android.content.SharedPreferences
import com.echoist.linkedout.page.home.TimeSelectionIndex


object SharedPreferencesUtil {
    private const val PREFS_NAME = "prefs"
    private const val KEY_PERIOD_INDEX = "period_index"
    private const val KEY_HOUR_INDEX = "hour_index"
    private const val KEY_MINUTE_INDEX = "minute_index"

    private const val ID_LOCAL_STORAGE = "id"
    private const val PW_LOCAL_STORAGE = "pw"

    private const val DISPLAY_INFO = DARK_MODE

    private val periodMap = mapOf(0 to "AM", 1 to "PM")
    private val hourMap = (0..11).associateWith { (it + 1).toString().padStart(2, '0') }
    private val minuteMap = (0..5).associateWith { (it * 10).toString().padStart(2, '0') }

    data class LocalAccountInfo(val id: String, val pw: String)
    //셀프알림 리마인드 true, false 설정
    fun saveWritingRemindNotification(context: Context, writingRemindNotification: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("writing_notification", writingRemindNotification)
            apply()
        }
    }

    fun getWritingRemindNotification(context: Context) : Boolean{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val displayInfo = sharedPreferences.getBoolean("writing_notification", false)
        return displayInfo
    }

    //디스플레이 화이트, 다크 버전
    fun saveDisplayInfo(context: Context, displayInfo: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(DISPLAY_INFO, displayInfo)
            apply()
        }
    }

    fun getDisplayInfo(context: Context) : String{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val displayInfo = sharedPreferences.getString(DISPLAY_INFO, "")
        return displayInfo!!
    }

    //자동로그인 아이디 비밀번호 저장
    fun getLocalAccountInfo(context: Context) : LocalAccountInfo{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val accountInfoId = sharedPreferences.getString(ID_LOCAL_STORAGE, "")
        val accountInfoPw = sharedPreferences.getString(PW_LOCAL_STORAGE, "")
        return LocalAccountInfo(accountInfoId!!,accountInfoPw!!)
    }

    fun saveLocalAccountInfo(context: Context, localAccountInfo: LocalAccountInfo) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(ID_LOCAL_STORAGE, localAccountInfo.id)
            putString(PW_LOCAL_STORAGE, localAccountInfo.pw)
            apply()
        }
    }

    //자동로그인 클릭 변수저장
    fun saveClickedAutoLogin(context: Context, isClickedAutoLogin: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isClickedAutoLogin", isClickedAutoLogin)
            apply()
        }
    }

    fun getClickedAutoLogin(context: Context) : Boolean{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isClickedAutoLogin = sharedPreferences.getBoolean("isClickedAutoLogin", false)
        return isClickedAutoLogin
    }

    //온보딩 일회성
    fun saveIsOnboardingFinished(context: Context, isOnboardingFinished: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isOnboardingFinished", isOnboardingFinished)
            apply()
        }
    }
    fun getIsOnboardingFinished(context: Context) : Boolean{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isOnboardingFinished = sharedPreferences.getBoolean("isOnboardingFinished", false)
        return isOnboardingFinished
    }
    //알림 시간 저장
    fun saveTimeSelection(context: Context, timeSelection: TimeSelectionIndex) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(KEY_PERIOD_INDEX, timeSelection.periodIndex)
            putInt(KEY_HOUR_INDEX, timeSelection.hourIndex)
            putInt(KEY_MINUTE_INDEX, timeSelection.minuteIndex)
            apply()
        }
    }

    fun getTimeSelection(context: Context): TimeSelectionIndex {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val periodIndex = sharedPreferences.getInt(KEY_PERIOD_INDEX, 0) // 기본값은 0
        val hourIndex = sharedPreferences.getInt(KEY_HOUR_INDEX, 0) // 기본값은 0
        val minuteIndex = sharedPreferences.getInt(KEY_MINUTE_INDEX, 0) // 기본값은 0
        return TimeSelectionIndex(periodIndex, hourIndex, minuteIndex)
    }

    fun getPeriodString(index: Int): String = periodMap[index] ?: "오전"
    fun getHourString(index: Int): String = hourMap[index] ?: "01"
    fun getMinuteString(index: Int): String = minuteMap[index] ?: "00"
}
