package com.echoist.linkedout.presentation.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.AlarmReceiver
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.api.SignUpApiImpl
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.api.apiCall
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.NotificationSettings
import com.echoist.linkedout.data.dto.Release
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.data.repository.HomeRepository
import com.echoist.linkedout.data.repository.TokenRepository
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.presentation.essay.write.Token
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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

    var viewedNotification by mutableStateOf(false)
    var reportNotification by mutableStateOf(false)
    var marketingNotification by mutableStateOf(false)
    var locationNotification by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var isFirstUser by mutableStateOf(false)
    var latestNoticeId: Int? by mutableStateOf(null) //공지가 있을경우 true, 없을경우 Null

    var updateHistory: SnapshotStateList<Release> = mutableStateListOf()

    var isVisibleGeulRoquis by mutableStateOf(true)
    val isReAuthenticationRequired: StateFlow<Boolean> = tokenRepository.isReAuthenticationRequired

    private val _isUserDeleteApiFinished = MutableStateFlow(false)
    val isUserDeleteApiFinished: StateFlow<Boolean> = _isUserDeleteApiFinished
    private val _isUpdateUserNotificationApiFinished = MutableStateFlow(false)
    val isUpdateUserNotificationApiFinished: StateFlow<Boolean> =
        _isUpdateUserNotificationApiFinished

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

    fun readMyProfile(): UserInfo {
        return exampleItems.myProfile
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

            Log.d("헤더 토큰", Token.accessToken)
            exampleItems.myProfile = response.data.user
            exampleItems.myProfile.essayStats = response.data.essayStats

            val userinfo = response.data.user.apply {
                essayStats = response.data.essayStats
            }

            userDataRepository.setUserInfo(userinfo)
            Log.i(TAG, "readMyInfo: ${exampleItems.myProfile}")
            locationNotification = exampleItems.myProfile.locationConsent ?: false

            //첫유저인지 판별
            isFirstUser = response.data.user.isFirst == true

        } catch (e: Exception) {
            Log.d(TAG, "readMyInfo: error err")
            e.printStackTrace()
        }
    }

    fun getMyInfo(): StateFlow<UserInfo> { // 함수 이름 변경
        return userDataRepository.userInfo
    }

    //고유식별자

    private fun getSSAID(context: Context): String {
        val deviceId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("DeviceID", "Device ID: $deviceId")
        return deviceId
    }

    private fun getFCMToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                callback(null) // 작업 실패 시 null 반환
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            callback(token)
        }
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

    //사용자 알림설정 get
    var isApifinished by mutableStateOf(false)

    //HomeRepository 도입
    fun readUserNotification() {

        viewModelScope.launch {
            homeRepository.requestUserNotification({ response ->
                viewedNotification = response.data.viewed
                reportNotification = response.data.report
                marketingNotification = response.data.marketing
                requestMyInfo()
            })
            {
                isApifinished = true
            }
        }

    }

    //사용자 알림설정 update
    fun updateUserNotification(locationAgreement: Boolean) {
        val body =
            NotificationSettings(viewedNotification, reportNotification, marketingNotification)
        viewModelScope.launch {
            try {
                supportApi.updateUserNotification(body)
                Log.d(TAG, "알림 저장 성공: $body")

                val userInfo = UserInfo(locationConsent = locationAgreement)
                val response = userApi.userUpdate(userInfo)
                if (response.isSuccessful) {
                    Log.d(TAG, "위치서비스 동의 저장 성공: ${response.code()}")

                    _isUpdateUserNotificationApiFinished.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("알림 설정 실패", "${e.message}")
            }
        }
    }

    fun setAlarmFromTimeString(context: Context, hour: String, min: String, period: String) {
        if (hour.isBlank() || min.isBlank() || period.isBlank()) {
            Log.e(TAG, "Hour, minute, or period is blank or null.")
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
            Log.d(TAG, "성공적으로 알람 세팅 완료: ${dateFormat.format(calendar.time)}")

            // 알람 설정 매일 울리게
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to set alarm", e)
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
        Log.d(TAG, "알람 취소")
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarmAfter10(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 현재 시간에 10초를 추가하여 알람 시간을 설정
        val triggerTime = System.currentTimeMillis() + 10000 // 10초 후의 시간

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

        Log.d(TAG, "Alarm set for 10 seconds later.")
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


