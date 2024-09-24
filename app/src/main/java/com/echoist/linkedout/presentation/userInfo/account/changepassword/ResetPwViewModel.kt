package com.echoist.linkedout.presentation.userInfo.account.changepassword

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.SignUpApi
import com.echoist.linkedout.presentation.essay.write.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPwViewModel @Inject constructor(
    private val signUpApi: SignUpApi
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    var isSendEmailVerifyApiFinished by mutableStateOf(false)

    fun requestChangePw(email: String) {
        isLoading = true
        viewModelScope.launch {
            isLoading = true
            val userEmail = SignUpApi.EmailRequest(email)
            val response = signUpApi.requestChangePw(userEmail)

            if (response.code() == 201) { //성공
                Log.d("requestChangePw api header", "${response.headers()}")
                Log.d("requestChangePw api code", "${response.code()}")
                //헤더에 토큰이 없다.
                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken
            } else {
                // code == 400 잘못된 이메일주소
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")
            }
            isLoading = false
        }
    }

    fun verifyChangePw(token: String, newPw: String) {
        viewModelScope.launch {

            val response = signUpApi.verifyChangePw(token)

            if (response.code() == 304) { //성공
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
                resetPw(token, newPw)
            } else {
                // code == 404 유효하지 않은토큰. 토큰을 못받았거나 10분이 지났거나
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")
            }
        }
    }

    private suspend fun resetPw(token: String, newPw: String) {
        val body = SignUpApi.ResetPwRequest(newPw, token)
        val response = signUpApi.resetPw(body)

        if (response.code() == 200) { //성공
            Log.e("authApiSuccess2", "${response.code()}")
        } else {
            Log.e("authApiFailed2", "${response.code()}")
        }
    }
}