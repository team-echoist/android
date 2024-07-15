package com.echoist.linkedout.viewModels

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import com.echoist.linkedout.data.UserInfo
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

    var isLoading by mutableStateOf(false)
    var isErr by mutableStateOf(false)

    fun getMyInfo() : UserInfo{
        return exampleItems.myProfile
    }
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
    var isSignUpApiFinished by mutableStateOf(false)
    fun getUserEmailCheck(userEmail: String,navController: NavController) {

        viewModelScope.launch {
            isLoading = true
            try {
                val email = SignUpApi.EmailRequest(userEmail)
                val response = signUpApi.emailDuplicateConfirm(email)


                if (response.isSuccessful) {
                    Log.e("authApiSuccess1", "${response.code()}")

                    emailVerify(navController)
                } else {
                    isErr = true
                    Log.e("authApiFailed1", "Failed : ${response.errorBody()}")
                        //todo check 404 뜨는 에러 해결해야할것.
                    Log.e("authApiSuccess", "${response.code()}")
                    if (response.code() == 409) Log.e(TAG, "getUserEmailCheck: 이미 사용중인 이메일입니다.", )
                }

            } catch (e: Exception) {
                isErr = true
                // api 요청 실패
                Log.e("writeEssayApiFailed1", "Failed: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }


    //이메일 중복검사 2차
    private suspend fun emailVerify(navController: NavController) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로

        try {
            val userAccount = SignUpApi.UserAccount(userEmail, userPw)
            val response = signUpApi.emailVerify(userAccount)

            if (response.isSuccessful) {

                if (response.code() == 400) isErr = true
                else if (response.code() == 200) isErr = true
                Log.e("authApiSuccess2", "${response.raw()}")
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
                Log.e("authApiSuccess2", "$isErr")

                isSignUpApiFinished = true

                Log.d("tokentoken", response.headers()["authorization"].toString())
                Token.accessToken = (response.headers()["authorization"].toString())
            } else {
                isErr = true
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
            }

        } catch (e: Exception) {
            isErr = true
            // api 요청 실패
            Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
        }
        finally {
            isLoading = false

        }
    }

    var isSendEmailVerifyApiFinished by mutableStateOf(false)
    //이메일변경용 메일요청
    fun sendEmailVerificationForChange(email : String) { //코루틴스코프에서 순차적으로 수행된다. 뷰모델스코프는 위에걸로
        viewModelScope.launch {
            try {
                val userEmail = SignUpApi.EmailRequest(email)
                val response = signUpApi.sendEmailVerificationForChange(Token.accessToken,userEmail)

                if (response.isSuccessful) {
                    Log.e("authApiSuccess2", "${response.headers()}")
                    Log.e("authApiSuccess2", "${response.code()}")

                    Token.accessToken = (response.headers()["authorization"].toString())
                    //todo 회원가입 비밀번호 특수문자 대소문자 필요한거 Ui 변경해야함. 그리고 확인로딩시 대기표시 띄워줘야함.
                    readMyInfo()
                    isSendEmailVerifyApiFinished = true

                } else {
                    Log.e("authApiFailed2", "Failed : ${response.headers()}")
                    Log.e("authApiFailed2", "${response.code()}")
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed2", "Failed: ${e.message}")
            }
        }

    }

    fun requestChangePw(email : String){
        viewModelScope.launch {
            isLoading = true
            val userEmail = SignUpApi.EmailRequest(email)
            val response = signUpApi.requestChangePw(Token.accessToken,userEmail)

            if (response.code() == 201) { //성공
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")
                //헤더에 토큰이 없다.
                //Token.accessToken = (response.headers()["authorization"].toString())
                isSendEmailVerifyApiFinished = true
                isLoading = false

            }
            else{
                // code == 400 잘못된 이메일주소
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")
                isLoading = false
            }


        }
    }

    fun verifyChangePw(token : String,newPw: String){
        viewModelScope.launch {

            val response = signUpApi.verifyChangePw(Token.accessToken,token)

            if (response.code() == 304) { //성공
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")

                //Token.accessToken = (response.headers()["authorization"].toString()) 여기도 마찬가지일것
                resetPw(token,newPw)

            }
            else{
                // code == 404 유효하지 않은토큰. 토큰을 못받았거나 10분이 지났거나
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")
            }

        }
    }

    private suspend fun resetPw(token : String, newPw : String)
    {

            val body = SignUpApi.ResetPwRequest(newPw,token)
            val response = signUpApi.resetPw(Token.accessToken,body)

            if (response.code() == 200) { //성공
                Log.e("authApiSuccess2", "${response.headers()}")
                Log.e("authApiSuccess2", "${response.code()}")


                // Token.accessToken = (response.headers()["authorization"].toString()) 여기도마찬가지
                //변경 완료!

            }
            else{
                // code == 404 유효하지 않은토큰. 토큰을 못받았거나 10분이 지났거나
                Log.e("authApiFailed2", "Failed : ${response.headers()}")
                Log.e("authApiFailed2", "${response.code()}")
            }

        }
    }
