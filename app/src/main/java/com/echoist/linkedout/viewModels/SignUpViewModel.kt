package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpApi : SignUpApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
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

    private suspend fun readMyInfo(){
        try {
            val response = userApi.readMyInfo(Token.accessToken)
            exampleItems.myProfile = response.data

        }catch (_: Exception){
            Log.d(ContentValues.TAG, "readMyInfo: error err")
        }
    }

    //이메일 중복검사 1차
    fun getUserEmailCheck(navController: NavController) {
        viewModelScope.launch {
            try {
                val email = SignUpApi.EmailRequest(userEmail)
                val response = signUpApi.emailDuplicateConfirm(email)


                if (response.isSuccessful) {
                    Log.e("authApiSuccess1", "${response.code()}")

                    emailVerify(navController)
                } else {
                    Log.e("authApiFailed1", "Failed : ${response.errorBody()}")
                        //todo check 404 뜨는 에러 해결해야할것.
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

            if (response.isSuccessful) {
                Log.e("authApiSuccess2", "${response.raw()}")
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")

                Log.d("tokentoken", response.headers()["authorization"].toString())
                Token.accessToken = (response.headers()["authorization"].toString())
                //todo 회원가입 비밀번호 특수문자 대소문자 필요한거 Ui 변경해야함. 그리고 확인로딩시 대기표시 띄워줘야함.
                readMyInfo()


                navController.navigate("SignUpComplete")
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