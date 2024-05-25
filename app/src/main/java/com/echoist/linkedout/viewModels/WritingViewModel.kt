package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.page.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WritingViewModel @Inject constructor(private val essayApi: EssayApi) : ViewModel() {

    var accessToken by mutableStateOf("")

    val maxLength = 4000
    val minLength = 10

    var focusState = mutableStateOf(false)
    var titleFocusState = mutableStateOf(false)

    var title = mutableStateOf(TextFieldValue(""))
    var content = mutableStateOf(TextFieldValue(""))
    var ringTouchedTime = mutableStateOf(5)

    var latitude :Double? by mutableStateOf(null)
    var longitude :Double? by mutableStateOf(null)

    var imageBitmap: MutableState<Bitmap?> = mutableStateOf(null)
    var locationText by mutableStateOf("")
    var hashTagList by mutableStateOf(mutableStateListOf<String>())




    var date = mutableStateOf("")
    var isCanCelClicked = mutableStateOf(false)
    var isDeleteClicked = mutableStateOf(false)


    var isHashTagClicked by mutableStateOf(false)
    var hashTagText by mutableStateOf("")

    var locationList by mutableStateOf(mutableStateListOf<String>())
    var isLocationClicked by mutableStateOf(false)

    //todo image bitmap 레트로핏으로 보내는방법


    var isTextFeatOpened = mutableStateOf(false)
    fun initialize() {
        titleFocusState.value = false
        focusState.value = false
        title.value = TextFieldValue("")
        content.value = TextFieldValue("")
        date.value = ""
        ringTouchedTime.value = 5
        isCanCelClicked.value = false
        isDeleteClicked.value = false
        latitude = null
        longitude = null
        isHashTagClicked = false
        hashTagText = ""
        hashTagList = mutableStateListOf()
        locationText = ""
        locationList = mutableStateListOf()
        isLocationClicked = false
        imageBitmap = mutableStateOf(null)
        isTextFeatOpened.value = false
    }


    //에세이 작성 후 서버에 post
    //api 통신 성공했을시에만 화면 이동
    fun writeEssay(
        navController: NavController, //저장할래요는 기본 둘다 false
        status : String
    ) {
        viewModelScope.launch {
            try {

                val essayData = EssayApi.EssayItem(
                    title.value.text,
                    content.value.text,
                    linkedOutGauge = ringTouchedTime.value,
                    //categoryId = 0, 이값도 넣어야할것
                    //thumbnail = imageBitmap, bitmap -> url
                    status = status,
                    latitude = latitude,
                    longitude = longitude,
                    location = locationText.ifEmpty { null },
                    tags = hashTagList
                )
                val response = essayApi.writeEssay(
                    Token.accessToken,
                    essayData = essayData
                )
                if (response.isSuccessful) {
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    Log.e("writeEssayApiSuccess 성공!", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", response.body()?.data?.title!!)

                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    navController.popBackStack("OnBoarding", false) //onboarding까지 전부 삭제.
                    navController.navigate("HOME/$accessToken")
                    initialize()
                } else {
                    Log.e("writeEssayApiFailed token", "Failed to write essay: $accessToken")
                    Log.e("writeEssayApiFailed1", "Failed to write essay: ${response.code()}")
                    Log.e("writeEssayApiFailed1", "Failed to write essay: ${response.body()}")
                    Log.e(TAG, "writeEssay: $latitude , $longitude, $locationText 로케이션텍스트 널 ??", )

                }

            } catch (e: Exception) {
                // api 요청 실패
                e.printStackTrace()

                Log.e("writeEssayApiFailed 아예", "Failed to write essay: ${e.printStackTrace()}")
                Log.e("writeEssayApiFailed 아예", "Failed to write essay: ${e.message}")
            }
        }
    }

    //에세이 수정 후 서버에 put

    fun modifyEssay(navController: NavController,status: String) {
        viewModelScope.launch {
            try {
                val essayData = EssayApi.EssayItem(
                    title.value.text,
                    content.value.text,
                    linkedOutGauge = ringTouchedTime.value,
                    status = status,
                    latitude = latitude,
                    longitude = longitude,
                    location = locationText,
                    tags = hashTagList
                )

                val response = essayApi.modifyEssay(/*todo 토큰값. 매번변경*/ accessToken,
                    essayData = essayData
                )

                navController.navigate("HOME") {
                    popUpTo("HOME") {
                        inclusive = false
                    }
                }
                if (response.code() == 202) {
                    //블랙리스트 코드 이동
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }
}