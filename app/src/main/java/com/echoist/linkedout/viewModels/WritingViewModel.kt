package com.echoist.linkedout.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.essay.EssayApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@HiltViewModel
class WritingViewModel @Inject constructor()
    : ViewModel() {

    private var accessToken = mutableStateOf("") // 토큰값을 계속 갱신하며, 이 값을 헤더로 요청보낸다.

    var focusState = mutableStateOf(false)
    var title = mutableStateOf(TextFieldValue(""))
    var content = mutableStateOf(TextFieldValue(""))
    var date = mutableStateOf("")
    var ringTouchedTime = mutableStateOf(5)

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


    private val api = Retrofit
        .Builder()
        .baseUrl("https://www.linkedoutapp.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(EssayApi::class.java)


    //에세이 작성 후 서버에 post
    fun writeEssay(navController: NavController) {
        viewModelScope.launch {
            try {

                val response = api.writeEssay(/*todo 토큰값. 매번변경*/ accessToken.value,
                    title.value.text,
                    content.value.text
                )

                navController.navigate("home") {
                    popUpTo("home") {
                        inclusive = false
                    }
                }
                if (response.code() == 202) {
                    //todo 블랙리스트 코드 이동
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    //에세이 수정 후 서버에 put

    fun modifyEssay(navController: NavController) {
        viewModelScope.launch {
            try {

                val response = api.modifyEssay(/*todo 토큰값. 매번변경*/ accessToken.value,
                    title.value.text,
                    content.value.text
                )

                navController.navigate("home") {
                    popUpTo("home") {
                        inclusive = false
                    }
                }
                if (response.code() == 202) {
                    //블랙리스트 코드 이동
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun modifyDelete(navController: NavController) {
        viewModelScope.launch {
            try {

                val response = api.deleteEssay(accessToken.value,)/*todo 토큰값. 매번변경*/

                navController.navigate("home") {
                    popUpTo("home") {
                        inclusive = false
                    }
                }
                if (response.code() == 202) {
                    //블랙리스트 코드 이동
                }

            } catch (e: Exception) {
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }

    }
}