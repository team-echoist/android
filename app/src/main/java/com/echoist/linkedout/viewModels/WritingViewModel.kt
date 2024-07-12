package com.echoist.linkedout.viewModels

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
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
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WritingViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val exampleItems: ExampleItems,
    private val essayStoreDao: EssayStoreDao
) : ViewModel() {

    var storedDetailEssay by mutableStateOf(EssayApi.EssayItem()) //이 값으로 다시 writingpage로 이동시키기


    var myProfile by mutableStateOf(exampleItems.myProfile)

    var accessToken by mutableStateOf("")

    val maxLength = 4000
    val minLength = 10

    var focusState = mutableStateOf(false)
    var titleFocusState = mutableStateOf(false)

    //ftb 를 통해 들어가면 storedDetailEssay를 null값으로 만들면됨
    var title = mutableStateOf(TextFieldValue(""))
    var content by mutableStateOf(TextFieldValue(""))
    var ringTouchedTime by mutableIntStateOf(5)
    var essayPrimaryId : Int? by mutableStateOf(null)

    var latitude :Double? by mutableStateOf(null)
    var longitude :Double? by mutableStateOf(null)

    var imageUri : Uri? by mutableStateOf(null)
    var imageUrl : String by mutableStateOf("")

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

    fun getEssayById(id: Int,navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            storedDetailEssay = essayStoreDao.getEssayById(id)!!
            title.value = TextFieldValue(storedDetailEssay.title.toString())
            content = TextFieldValue(storedDetailEssay.content.toString())
            longitude = storedDetailEssay.longitude
            latitude = storedDetailEssay.latitude
            essayPrimaryId = storedDetailEssay.essayPrimaryId


            Log.d(TAG, "getEssayById: $storedDetailEssay")
            withContext(Dispatchers.Main) {
                navController.navigate("WritingPage")
            }

        }
    }


    fun deleteEssays(essayIds: List<Int?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                essayStoreDao.deleteEssaysByIds(essayIds!!) //todo 공백도 허용할것?
                getAllStoredData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateOrInsertEssay(essayItem: EssayApi.EssayItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                essayStoreDao.updateOrInsertEssay(essayItem) //todo 공백도 허용할것?

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllStoredData(){
        viewModelScope.launch(Dispatchers.IO) {
            val storedData = essayStoreDao.getAllReadData()
            Log.d(TAG, "getAllStoredData: $storedData")
            storageEssaysList.clear()
            storageEssaysList.addAll(storedData)
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
        imageUri = null
        imageUrl = ""
        isTextFeatOpened.value = false
        essayPrimaryId = null
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
                    thumbnail = imageUrl,
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
                    navController.popBackStack("Home", false) //onboarding까지 전부 삭제.
                    navController.navigate("CompletedEssayPage")
                    initialize()
                } else {
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
                    thumbnail = imageUrl,
                    status = status,
                    latitude = latitude,
                    longitude = longitude,
                    location = locationText,
                    tags = hashTagList
                )


                val response = essayApi.modifyEssay( Token.accessToken,
                    exampleItems.detailEssay.id!!,
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

    private fun getFileFromUri(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val fileName = getFileName(uri, contentResolver)
        val file = File(context.cacheDir, fileName.toString())
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
        var name: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    suspend fun uploadThumbnail(uri: Uri, context: Context): String? { //서버에 이미지 업로드하고 url을 반환

        try {
            val file = getFileFromUri(uri, context)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            // 서버로 업로드 요청 보내기
            val response = essayApi.uploadThumbnail(Token.accessToken, body, exampleItems.detailEssay.id)

            if (response.isSuccessful){
                imageUrl = response.body()!!.data.imageUrl
                Log.d(TAG, "uploadImage: $imageUrl")
                return response.body()!!.data.imageUrl
            }else
                return  null

            // 서버 응답에서 URL 추출
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "uploadImage: ${e.message}")

            return null
        } finally {

        }

    }
}