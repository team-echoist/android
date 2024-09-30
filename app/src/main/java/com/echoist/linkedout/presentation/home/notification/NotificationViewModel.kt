package com.echoist.linkedout.presentation.home.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.AlarmReceiver
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.dto.NotificationSettings
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.presentation.home.drawable.setting.TimeSelectionIndex
import com.echoist.linkedout.presentation.util.getHourString
import com.echoist.linkedout.presentation.util.getMinuteString
import com.echoist.linkedout.presentation.util.getPeriodString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
        private val userDataRepository: UserDataRepository,
        private val supportApi: SupportApi,
        private val userApi: UserApi
) : ViewModel() {

    private val _timeSelection = MutableStateFlow(userDataRepository.getTimeSelection())

    private val _writingRemindNotification =
            MutableStateFlow(userDataRepository.getWritingRemindNotification())
    val writingRemindNotification: StateFlow<Boolean> = _writingRemindNotification

    val hour: StateFlow<String> = _timeSelection.map { getHourString(it.hourIndex) }
            .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val min: StateFlow<String> = _timeSelection.map { getMinuteString(it.minuteIndex) }
            .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val period: StateFlow<String> = _timeSelection.map { getPeriodString(it.periodIndex) }
            .stateIn(viewModelScope, SharingStarted.Lazily, "")

    var viewedNotification by mutableStateOf(false)
    var reportNotification by mutableStateOf(false)
    var marketingNotification by mutableStateOf(false)
    var locationNotification by mutableStateOf(false)


    fun updateTimeSelection() {
        _timeSelection.value = userDataRepository.getTimeSelection()
    }

    fun updateWritingRemindNotification(isEnabled: Boolean) {
        _writingRemindNotification.value = isEnabled
    }

    fun getTimeSelection(): TimeSelectionIndex {
        return userDataRepository.getTimeSelection()
    }

    fun saveWritingRemindNotification(isChecked: Boolean) {
        userDataRepository.saveWritingRemindNotification(isChecked)
    }

    fun saveTimeSelection(time: TimeSelectionIndex) {
        userDataRepository.saveTimeSelection(time)
    }

    var isApifinished by mutableStateOf(false)

    private suspend fun requestMyInfo() {
        try {
            val response = userApi.getMyInfo()
            val userinfo = response.data.user.apply {
                essayStats = response.data.essayStats
            }

            userDataRepository.setUserInfo(userinfo)
            Log.i(ContentValues.TAG, "readMyInfo: ${userDataRepository.userInfo}")
            locationNotification = userinfo.locationConsent ?: false

            //첫유저인지 판별
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun readUserNotification() {
        try {
            val response = supportApi.readUserNotification()
            if (response.isSuccessful) {
                viewedNotification = response.body()!!.data.viewed
                reportNotification = response.body()!!.data.report
                marketingNotification = response.body()!!.data.marketing
                requestMyInfo()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("알림 설정 실패", "${e.message}")
        } finally {
            isApifinished = true
        }
    }
    //사용자 알림설정 update
    fun updateUserNotification(locationAgreement: Boolean) {
        val body =
                NotificationSettings(viewedNotification, reportNotification, marketingNotification)
        viewModelScope.launch {
            try {
                supportApi.updateUserNotification(body)
                Log.d(ContentValues.TAG, "알림 저장 성공: $body")

                val userInfo = UserInfo(locationConsent = locationAgreement)
                val response = userApi.userUpdate(userInfo)
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "위치서비스 동의 저장 성공: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("알림 설정 실패", "${e.message}")
            }
        }
    }

    fun setAlarmFromTimeString(context: Context, hour: String, min: String, period: String) {
        if (hour.isBlank() || min.isBlank() || period.isBlank()) {
            Log.e(ContentValues.TAG, "Hour, minute, or period is blank or null.")
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()

        try {
            // 시간과 분을 설정
            calendar.set(Calendar.HOUR_OF_DAY, convertTo24HourFormat(hour.toInt(), period))
            calendar.set(Calendar.MINUTE, min.toInt())
            calendar.set(Calendar.SECOND, 0)

            // 현재 시간보다 이전이면 다음 날로 설정
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            Log.d(ContentValues.TAG, "성공적으로 알람 세팅 완료: ${dateFormat.format(calendar.time)}")

            // 알람 설정 매일 울리게
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            )
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Failed to set alarm", e)
        }
    }

    private fun convertTo24HourFormat(hour: Int, period: String): Int {
        // 오전(AM)과 오후(PM)에 따라 24시간 형식으로 변환
        return when (period.uppercase(Locale.getDefault())) {
            "AM" -> if (hour == 12) 0 else hour // 오전 12시는 0시로 변환
            "PM" -> if (hour == 12) 12 else hour + 12 // 오후 12시는 그대로, 그 외는 12를 더함
            else -> throw IllegalArgumentException("Invalid period: $period")
        }
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,

                )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        // 알람이 해제되었음을 로그로 출력
        Log.d(ContentValues.TAG, "알람 취소")
    }

}