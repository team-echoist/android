package com.echoist.linkedout.viewModels

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.SignUpApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpApi : SignUpApi
) : ViewModel() {

    var userEmail by mutableStateOf("")
    var userEmailError by mutableStateOf(false)
    var userPw by mutableStateOf("")

    var agreement_service by mutableStateOf(false)
    var agreement_collection by mutableStateOf(false)
    var agreement_teen by mutableStateOf(false)
    var agreement_marketing by mutableStateOf(false)

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //이메일 중복검사 1차
    fun getUserEmailCheck(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = signUpApi.emailDuplicateConfirm(userEmail)

                if (response.code() == 200) {
                    Log.e("authApiSuccess1", "${response.code()}")

                    emailVerify(navController)
                } else {
                    Log.e("authApiFailed1", "Failed : ${response.errorBody()}")

                    Log.e("authApiSuccess", "${response.code()}")
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed1", "Failed: ${e.message}")
            }
        }
    }


    //이메일 중복검사 2차
    private suspend fun emailVerify(navController: NavController) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로

        try {
            val userAccount = SignUpApi.UserAccount(userEmail, userPw)
            val response = signUpApi.emailVerify(userAccount)

            if (response.code() == 201) {
                Log.e("authApiSuccess2", "${response.raw()}")
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")

                navController.navigate("LoginPage")
            } else {
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
            }

        } catch (e: Exception) {
            // api 요청 실패
            Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
        }

    }


}