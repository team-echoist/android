package com.echoist.linkedout.viewModels

import SignUpApiImpl
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.DeviceId
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.myLog.Token
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val signUpApi: SignUpApi) : ViewModel(){

    var myProfile by mutableStateOf(exampleItems.myProfile)

    var viewedNotification by mutableStateOf(false)
    var reportNotification by mutableStateOf(false)
    var writingRemindNotification by mutableStateOf(false)

    var isLoading by mutableStateOf(false)


    fun getMyInfo(): UserInfo { // 함수 이름 변경
        return exampleItems.myProfile
    }

    //고유식별자

    private fun getSSAID(context : Context) : String{
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("DeviceID", "Device ID: $deviceId")
        return deviceId
    }

    private fun getFCMToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
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
                            signUpApi.requestRegisterDevice(Token.accessToken,body)
                            Log.d("FCM Token", "suc to fetch token $ssaid \n $token")

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
    fun getUserNotification() {

        viewModelScope.launch {
            try {
                val response = signUpApi.getUserNotification(Token.accessToken, DeviceId.deviceId)
                Log.d(TAG, "getUserNotification: ${response.body()?.data!!}")

                if (response.isSuccessful){
                    Log.d(TAG, "getUserNotification: ${response.body()?.data!!}")
                    viewedNotification = response.body()?.data!!.viewed
                    reportNotification = response.body()?.data!!.report
                    writingRemindNotification = response.body()?.data!!.timeAllowed
                }
                else{
                    Log.d(TAG, "getUserNotification: ${response.code()}")
                }

            }catch (e:Exception){
                e.printStackTrace()
                Log.d(TAG, "failed: ${e.message}")
            }
        }
    }

    //사용자 알림설정 update
    fun updateUserNotification(navController: NavController){
        val body = SignUpApi.NotificationSettings(viewedNotification,reportNotification,writingRemindNotification,"10:10")
        viewModelScope.launch {
            try {
                signUpApi.updateUserNotification(Token.accessToken,DeviceId.deviceId,body)
                Log.d(TAG, "success: $body")
                navController.navigate("HOME")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "failed: ${e.message}")

                TODO("Not yet implemented")
            } finally {

            }
        }

    }


    }

