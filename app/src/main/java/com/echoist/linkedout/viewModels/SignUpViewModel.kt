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
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SignUpViewModel : ViewModel() {
    private var accessToken = mutableStateOf("") // 토큰값을 계속 갱신하며, 이 값을 헤더로 요청보낸다.

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

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


    private val authApi = Retrofit
        .Builder()
        .baseUrl("https://www.linkedoutapp.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SignUpApi::class.java)

    //이메일 중복검사 1차
    fun getUserEmailCheck(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = authApi.emailDuplicateConfirm(userEmail)

                if (response.code() == 200) {
                    Log.e("authApiSuccess", "${response.headers()}")
                    Log.e("authApiSuccess", "${response.code()}")

                    emailVerify(navController)
                } else {
                    Log.e("authApiFailed", "Failed : ${response.headers()}")
                    Log.e("authApiSuccess", "${response.code()}")
                    //todo header 파싱 ㄱㄱ 는 회원가입부터 작동
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed: ${e.message}")
            }
        }
    }


    //이메일 중복검사 2차
    private suspend fun emailVerify(navController: NavController) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로

            try {
                val userAccount = SignUpApi.UserAccount(userEmail, userPw)
                val response = authApi.emailVerify(userAccount)

                if (response.code() == 201) {
                    Log.e("authApiSuccess", "${response.headers()}")
                    Log.e("authApiSuccess", "${response.code()}")
                    navController.navigate("HOME")
                } else {
                    Log.e("authApiFailed", "Failed : ${response.headers()}")
                    Log.e("authApiSuccess", "${response.code()}")
                    //todo header 파싱 ㄱㄱ.
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed: ${e.message}")
            }

    }
}