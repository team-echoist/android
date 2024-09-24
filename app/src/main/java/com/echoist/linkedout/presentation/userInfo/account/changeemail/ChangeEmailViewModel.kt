package com.echoist.linkedout.presentation.userInfo.account.changeemail

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.api.SignUpApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.essay.write.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val signUpApi: SignUpApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    var isSendEmailVerifyApiFinished by mutableStateOf(false)

    private val _navigateToComplete = MutableStateFlow(false)
    val navigateToComplete: StateFlow<Boolean> = _navigateToComplete

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

    fun sendEmailVerificationForChange(email: String) {
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

    fun postAuthChangeEmail(code: String) {
        viewModelScope.launch {
            try {
                val userEmail = SignUpApi.ChangeEmail(code)
                val response = signUpApi.postAuthChangeEmail(userEmail)
                if (response.isSuccessful) {
                    Log.e("authApiSuccess3", "${response.headers()}")
                    Log.e("authApiSuccess3", "${response.code()}")
                    readMyInfo()
                    _navigateToComplete.value = true
                } else {
                    Log.e("authApiFailed3", "Failed : ${response.headers()}")
                    Log.e("authApiFailed3", "${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("writeEssayApiFailed3", "Failed: ${e.message}")
            }
        }
    }
}