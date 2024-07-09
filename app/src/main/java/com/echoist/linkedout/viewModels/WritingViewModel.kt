package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.page.myLog.Token
import com.echoist.linkedout.room.EssayStoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WritingViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems,
    private val essayStoreDao: EssayStoreDao
) : ViewModel() {



    var myProfile by mutableStateOf(exampleItems.myProfile)

    var accessToken by mutableStateOf("")

    val maxLength = 4000
    val minLength = 10

    var focusState = mutableStateOf(false)
    var titleFocusState = mutableStateOf(false)

    var title = mutableStateOf(TextFieldValue(""))
    var content by mutableStateOf(TextFieldValue(""))
    var ringTouchedTime by mutableIntStateOf(5)

    var latitude :Double? by mutableStateOf(null)
    var longitude :Double? by mutableStateOf(null)

    var imageBitmap: MutableState<Bitmap?> = mutableStateOf(null)
    var locationText by mutableStateOf("")
    var hashTagList by mutableStateOf(mutableStateListOf<String>())

    val storageEssaysList = exampleItems.exampleEmptyEssayList


    var date = mutableStateOf("")
    var isCanCelClicked = mutableStateOf(false)
    var isDeleteClicked = mutableStateOf(false)


    var isHashTagClicked by mutableStateOf(false)
    var hashTagText by mutableStateOf("")

    var locationList by mutableStateOf(mutableStateListOf<String>())
    var isLocationClicked by mutableStateOf(false)

    //todo image bitmap 레트로핏으로 보내는방법


    var isTextFeatOpened = mutableStateOf(false)


    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return currentDate.format(formatter)
    }
    fun setStorageEssay(essayItem: EssayApi.EssayItem){
        exampleItems.storageEssay = essayItem
    }

    fun getStorageEssay() : EssayApi.EssayItem{
        return exampleItems.storageEssay
    }

    fun storeEssay(essayItem: EssayApi.EssayItem) {
        viewModelScope.launch(Dispatchers.IO) {
            essayStoreDao.insertEssay(essayItem) //todo 공백도 허용할것?
        }
    }


    fun initialize() {
        titleFocusState.value = false
        focusState.value = false
        title.value = TextFieldValue("")
        content = TextFieldValue("")
        date.value = ""
        ringTouchedTime = 5
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

                val essayData = EssayApi.WritingEssayItem(
                    title.value.text,
                    content.text,
                    linkedOutGauge = ringTouchedTime,
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
                    exampleItems.detailEssay = response.body()!!.data!!
                    exampleItems.detailEssayBackStack.push(exampleItems.detailEssay)

                    when(exampleItems.detailEssay.status){

                        "private" -> {
                            exampleItems.myProfile.essayStats!!.totalEssays += 1
                        }
                        "published" -> {
                            exampleItems.myProfile.essayStats!!.publishedEssays += 1
                            exampleItems.myProfile.essayStats!!.totalEssays += 1
                        }
                        "linkedout" -> {
                            exampleItems.myProfile.essayStats!!.linkedOutEssays += 1
                            exampleItems.myProfile.essayStats!!.totalEssays += 1
                        }

                    }

                    Log.e("writeEssayApiSuccess 성공!", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", response.body()?.data?.title!!)

                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    navController.popBackStack("Home", false) //onboarding까지 전부 삭제.
                    navController.navigate("CompletedEssayPage")
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
                val essayData = EssayApi.WritingEssayItem(
                    title.value.text,
                    content.text,
                    linkedOutGauge = ringTouchedTime,
                    status = status,
                    latitude = latitude,
                    longitude = longitude,
                    location = locationText,
                    tags = hashTagList
                )


                val response = essayApi.modifyEssay( Token.accessToken,
                    essayData = essayData
                )
                accessToken = (response.headers()["authorization"].toString())
                Token.accessToken = accessToken

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