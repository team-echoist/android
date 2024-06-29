package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val userItem = exampleItems.userItem
    var myProfile by mutableStateOf(exampleItems.myProfile)

    fun updateProfile(userInfo: UserInfo){
        exampleItems.myProfile = userInfo
    }

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

            // 서버에 토큰값 보내기 등의 추가 작업 가능
            val msg = token.toString()
        }
    }

    fun requestRegisterDevice(context: Context) {

            val ssaid = getSSAID(context)
            getFCMToken { token ->
                if (token != null) {
                    // 서버에 토큰값 보내기 등의 작업을 여기서 처리할 수 있습니다.
                    val body = SignUpApi.RegisterDeviceRequest(ssaid,token)

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


    }

