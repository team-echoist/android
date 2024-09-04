package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val signUpApi: SignUpApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    private suspend fun readMyInfo() {
        try {
            val response = userApi.getMyInfo()

            exampleItems.myProfile = response.data.user
            exampleItems.myProfile.essayStats = response.data.essayStats

        } catch (_: Exception) {
            Log.d(ContentValues.TAG, "readMyInfo: error err")
        }
    }

    fun getMyInfo(): UserInfo {
        return exampleItems.myProfile
    }

    var isSendEmailVerifyApiFinished by mutableStateOf(false)
    fun sendEmailVerificationForChange(email: String) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로
        isLoading = true
        viewModelScope.launch {
            try {
                val userEmail = SignUpApi.EmailRequest(email)
                val response = signUpApi.sendEmailVerificationForChange(userEmail)

                if (response.isSuccessful) {
                    Log.e("authApiSuccess2", "${response.headers()}")
                    Log.e("authApiSuccess2", "${response.code()}")

                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    readMyInfo()
                    isSendEmailVerifyApiFinished = true

                } else {
                    Log.e("authApiFailed2", "Failed : ${response.headers()}")
                    Log.e("authApiFailed2", "${response.code()}")
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
            } finally {
                isLoading = false
            }
        }

    }

}