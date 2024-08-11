package com.echoist.linkedout.viewModels

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
import androidx.navigation.NavController
import com.echoist.linkedout.AlarmReceiver
import com.echoist.linkedout.DeviceId
import com.echoist.linkedout.Routes
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.SignUpApiImpl
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.History
import com.echoist.linkedout.data.NotificationSettings
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.myLog.Token
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val userApi: UserApi,
    private val supportApi: SupportApi) : ViewModel() {

    var myProfile by mutableStateOf(exampleItems.myProfile)

    var viewedNotification by mutableStateOf(false)
    var reportNotification by mutableStateOf(false)
    var marketingNotification by mutableStateOf(false)
    var locationNotification by mutableStateOf(false)



    var isLoading by mutableStateOf(false)
    var isFirstUser by mutableStateOf(false)
    var latestNoticeId : Int? by mutableStateOf(null) //공지가 있을경우 true, 없을경우 Null

    var updateHistory: SnapshotStateList<History> =  mutableStateListOf()

    var isVisibleGeulRoquis by mutableStateOf(true)

    fun readMyProfile() : UserInfo{
        return exampleItems.myProfile
    }

    fun initializeDetailEssay(){
        exampleItems.detailEssay = EssayApi.EssayItem()
    }
    fun setStorageEssay(essayItem: EssayApi.EssayItem){
        exampleItems.storageEssay = essayItem
    }

    suspend fun requestMyInfo(){
        try {

            val response = userApi.getMyInfo(Token.accessToken)
            Log.d("헤더 토큰", Token.accessToken)
            exampleItems.myProfile = response.data.user
            exampleItems.myProfile.essayStats = response.data.essayStats
            Log.i(TAG, "readMyInfo: ${exampleItems.myProfile}")
            locationNotification = exampleItems.myProfile.locationConsent ?: false

            //첫유저인지 판별
            isFirstUser = response.data.user.isFirst == true

        }catch (e: Exception){
            Log.d(TAG, "readMyInfo: error err")
            e.printStackTrace()
            Log.d(TAG, e.message.toString())
            Log.d(TAG, e.cause.toString())


        }
    }

    fun getMyInfo(): UserInfo { // 함수 이름 변경
        return exampleItems.myProfile
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
                viewModelScope.launch {
                    try {
                        supportApi.requestRegisterDevice(Token.accessToken, body)
                        Log.i("FCM Token", "ssaid 값 : $ssaid \n FCM token 값 : $token")

                    } catch (e: Exception) {
                        Log.e("FCM Token", "Failed to fetch token")

                    } finally {
                    }

                }

            } else {
                Log.e("FCM Token", "Failed to fetch token")
                //토큰없으면 기기등록도 안됨
            }
        }
    }

    //사용자 알림설정 get
    var isApifinished by mutableStateOf(false)
    fun readUserNotification() {
        viewModelScope.launch {
            try {
                val response = supportApi.readUserNotification(Token.accessToken)
                Log.d(TAG, "readUserNotification: ${response.body()?.data!!}")

                if (response.isSuccessful) {
                    Log.d(TAG, "readUserNotification: success${response.body()?.data!!}")
                    viewedNotification = response.body()?.data!!.viewed
                    reportNotification = response.body()?.data!!.report
                    marketingNotification = response.body()?.data!!.marketing

                    requestMyInfo()
                } else {
                    Log.e(TAG, "readUserNotification: err${response.code()}")
                    Log.e(TAG, "readUserNotification: err device id${DeviceId.ssaid}")

                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "readUserNotification: error ${e.message}")
                Log.e(TAG, "readUserNotification: error device id :  ${DeviceId.ssaid}\n token : ${Token.accessToken}")

            }
            finally {
                isApifinished = true
            }
        }
    }

    //사용자 알림설정 update
    fun updateUserNotification(navController: NavController,locationAgreement : Boolean) {
        val body = NotificationSettings(viewedNotification, reportNotification,marketingNotification)
        viewModelScope.launch {
            try {
                supportApi.updateUserNotification(Token.accessToken,  body)
                Log.d(TAG, "updateUserNotification success: $body")

                    val userInfo = UserInfo(locationConsent = locationAgreement)
                    val response = userApi.userUpdate(Token.accessToken,userInfo)
                    Log.d(TAG, "위치서비스 동의 저장 성공: ${response.code()}")
                    Log.d(TAG, "위치서비스 동의 저장 성공: $locationNotification")




                navController.navigate("${Routes.Home}/200")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "noti update failed: ${e.message}")

                TODO("Not yet implemented")
            } finally {
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
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

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
    fun requestLatestNotice(){
        viewModelScope.launch {
            try {
                val response = supportApi.requestLatestNotice(Token.accessToken)
                if (response.isSuccessful){
                    //공지가 있을경우 true, 없을경우 Null
                    latestNoticeId = response.body()!!.data.newNotice
                    Log.d("최신공지 확인", "확인 성공 newNotice : ${response.body()!!.data.newNotice}")
                }
                else{
                    Log.e("최신공지 확인", "확인 실패 ${response.code()}")
                }
            }catch (e:Exception){
                Log.e("최신공지 확인", "확인 실패 ${e.message}")
                e.printStackTrace()

            }
        }

    }

    fun readUpdateHistory(){
        viewModelScope.launch {
            try {
                isLoading = true
                val response = supportApi.readUpdatedHistories(Token.accessToken)
                if (response.isSuccessful){
                    updateHistory = response.body()!!.data.histories.toMutableStateList()
                    Token.accessToken = (response.headers()["authorization"].toString())

                }
            }catch (e:Exception){
                e.printStackTrace()

            }
            finally {
                isLoading = false
            }
        }

    }

    var essayCount by mutableStateOf(mutableListOf(0,0,0,0,0))

    fun requestUserGraphSummaryResponse(){
        viewModelScope.launch {
            try {
                val response = userApi.requestUserGraphSummary(Token.accessToken)
                if (response.isSuccessful){

                    repeat(5){
                        essayCount[it] = response.body()!!.data.weeklyEssayCounts!![it].count
                    }
                    Log.d(TAG, "유저 주간 링크드아웃 지수 호출 성공 $essayCount")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "유저 주간 링크드아웃 지수: ${e.message}", )
            } finally {
            }
        }

    }

    fun setFirstUserToExistUser(){
        viewModelScope.launch {
            try {
                val isNotFirst = UserInfo(isFirst = false)
                val response = userApi.userUpdate(Token.accessToken,isNotFirst)
                Log.d(TAG, "setFirstUserToExistUser: ${response.code()} ${isNotFirst.isFirst}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("error","set user first to exist error ")
            }
        }

    }

    var isCheckFinished by mutableStateOf(false)
    var isExistUnreadAlerts by mutableStateOf(false)
    fun requestUnreadAlerts(){

        viewModelScope.launch {
            try {
                val response = supportApi.readUnreadAlerts(Token.accessToken)
                if (response.isSuccessful){
                    isExistUnreadAlerts =
                        response.body()!!.data
                    Log.d(TAG, "안읽은 알림 여부: ${response.body()!!.data}")
                }
            } catch (e: Exception) {
                Log.e("안읽은 알림 여부","에러")
            } finally {
                isCheckFinished = true
            }
        }
    }
    var geulRoquisUrl by mutableStateOf("")
    fun requestGuleRoquis(){
        viewModelScope.launch {
            try {
                val response = supportApi.readGeulroquis(Token.accessToken)
                if (response.isSuccessful){
                    Log.d("글로키 api", "성공: ${response.body()!!.data.url}")
                    geulRoquisUrl = response.body()!!.data.url
                    Log.d("글로키 api", "성공: $geulRoquisUrl")

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("글로키 api","에러 ${e.message}")
            }
            finally {
                isApifinished = true
            }
        }
    }

    fun requestUserReActivate(){
        viewModelScope.launch {
            try {
                val response = userApi.requestReactivate(Token.accessToken)
                if (response.isSuccessful){
                    Log.d("유저 탈퇴 취소", "성공: ${response.body()}")
                }
                else{
                    Log.e("유저 탈퇴 취소", "실패: ${response.code()}", )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("유저 탈퇴 취소", "실패: ${e.message}", )
            }
        }
    }

    fun requestUserDelete(navController: NavController){
        viewModelScope.launch {
            try {
                val response = userApi.requestDeleteUser(Token.accessToken)
                if (response.isSuccessful){
                    Log.d("유저 즉시 탈퇴", "성공: ${response.body()}")
                    navController.popBackStack(Routes.OnBoarding,false)
                    navController.navigate(Routes.LoginPage)
                }
                else{
                    Log.e("유저 즉시 탈퇴", "실패: ${response.code()}", )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("유저 즉시 탈퇴", "실패: ${e.message}", )
            }
        }
    }
}


