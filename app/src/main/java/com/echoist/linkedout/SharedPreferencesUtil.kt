package com.echoist.linkedout

import android.content.Context
import android.content.SharedPreferences
import com.echoist.linkedout.page.home.TimeSelectionIndex


object SharedPreferencesUtil {
    private const val PREFS_NAME = "prefs"
    private const val KEY_PERIOD_INDEX = "period_index"
    private const val KEY_HOUR_INDEX = "hour_index"
    private const val KEY_MINUTE_INDEX = "minute_index"

    private const val DISPLAY_INFO = DARK_MODE

    private val periodMap = mapOf(0 to "AM", 1 to "PM")
    private val hourMap = (0..11).associateWith { (it + 1).toString().padStart(2, '0') }
    private val minuteMap = (0..5).associateWith { (it * 10).toString().padStart(2, '0') }

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
