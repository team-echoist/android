package com.echoist.linkedout

import SignUpApiImpl
import android.content.ContentValues.TAG
import android.provider.Settings
import android.util.Log
import com.echoist.linkedout.page.myLog.Token
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DeviceId{
    var deviceId : String = ""
}
class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

    }
    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM_Token: $token")
        getSSAID()
        val body = SignUpApiImpl.RegisterDeviceRequest(DeviceId.deviceId,token)

        //뉴토큰 서버에 보내기

        CoroutineScope(Dispatchers.IO).launch {
            try {
                SignUpApiImpl().requestRegisterDevice(Token.accessToken, body)
                Log.d(TAG, "Token registration request successful")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to register token", e)
                // 실패 시 처리할 로직 추가
            }
        }
    }

    private fun getSSAID(){
        DeviceId.deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("DeviceID", "Device ID: ${DeviceId.deviceId}")
    }
}