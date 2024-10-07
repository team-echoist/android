package com.echoist.linkedout.presentation.essay.write

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
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.ExampleItems
import com.echoist.linkedout.data.room.EssayStoreDao
import com.mohamedrejeb.richeditor.model.RichTextState
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

    var isModifyClicked by mutableStateOf(false)
    var modifyEssayid by mutableIntStateOf(0)

    var myProfile by mutableStateOf(exampleItems.myProfile)

    var accessToken by mutableStateOf("")
    var isStored by mutableStateOf(false)

    val maxLength = 10000
    val minLength = 10

    var focusState = mutableStateOf(false)
    var titleFocusState = mutableStateOf(false)

    //ftb 를 통해 들어가면 storedDetailEssay를 null값으로 만들면됨
    var title = mutableStateOf(TextFieldValue(""))
    var content by mutableStateOf((""))
    var state by mutableStateOf(RichTextState)
    var hint by mutableStateOf("10자 이상의 내용을 입력해 주세요")
    var ringTouchedTime by mutableIntStateOf(5)
    var essayPrimaryId : Int? by mutableStateOf(null)

    var latitude :Double? by mutableStateOf(null)
    var longitude :Double? by mutableStateOf(null)

    var imageUri : Uri? by mutableStateOf(null)
    var imageUrl : String? by mutableStateOf(null)

    var locationText by mutableStateOf("")
    var hashTagList by mutableStateOf(mutableStateListOf<String>())

    val storageEssaysList = mutableStateListOf<EssayApi.EssayItem>()


    var date = mutableStateOf("")
    var isCanCelClicked = mutableStateOf(false)
    var isDeleteClicked = mutableStateOf(false)


    var isHashTagClicked by mutableStateOf(false)
    var hashTagText by mutableStateOf("")

    var locationList by mutableStateOf(mutableStateListOf<String>())
    var isLocationClicked by mutableStateOf(false)

    //todo image bitmap 레트로핏으로 보내는방법
    fun readDetailEssay() : EssayApi.EssayItem {
        return exampleItems.detailEssay
    }


    var isTextFeatOpened = mutableStateOf(false)
    init {
        isModifyClicked = false
    }

    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return currentDate.format(formatter)
    }

    fun getEssayById(id: Int,navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {

            storedDetailEssay = essayStoreDao.getEssayById(id)!!
            title.value = TextFieldValue(storedDetailEssay.title.toString())
            content = storedDetailEssay.content.toString()
            longitude = storedDetailEssay.longitude
            latitude = storedDetailEssay.latitude
            essayPrimaryId = storedDetailEssay.essayPrimaryId

            Log.d(TAG, "getEssayById: $storedDetailEssay")
            withContext(Dispatchers.Main) {
                navController.navigate("WritingPage")
            }

        }
    }

    fun deleteEssay(essayId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                essayStoreDao.deleteEssayById(essayId)
                getAllStoredData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun deleteEssays(essayIds: List<Int?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                essayStoreDao.deleteEssaysByIds(essayIds)
                getAllStoredData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateOrInsertEssay(essayItem: EssayApi.EssayItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                essayStoreDao.updateOrInsertEssay(essayItem)
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
        content = ""
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
        imageUrl = null
        isTextFeatOpened.value = false
        essayPrimaryId = null
        hint = "10자 이상의 내용을 입력해 주세요"
        exampleItems.detailEssay = EssayApi.EssayItem()
        exampleItems.storageEssay = EssayApi.EssayItem()
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
                    content,
                    linkedOutGauge = ringTouchedTime,
                    //categoryId = 0, 이값도 넣어야할것
                    thumbnail = imageUrl,
                    status = status,
                    latitude = latitude,
                    longitude = longitude,
                    location = locationText.ifEmpty { null },
                    tags = hashTagList
                )
                val response = essayApi.writeEssay(essayData = essayData)
                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken

                if (response.isSuccessful) {
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
                    Log.e("에세이 작성 에러", "${response.code()}", )
                    Log.e("에러 헤더 엑세스 토큰", Token.accessToken, )
                    Log.e("에러 헤더 리프레시 토큰", Token.refreshToken, )

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
                    content,
                    linkedOutGauge = ringTouchedTime,
                    thumbnail = imageUrl,
                    status = status,
                    latitude = latitude,
                    longitude = longitude,
                    location = locationText.ifEmpty { null },
                    tags = hashTagList
                )
                Log.d(TAG, "modifyEssay: $modifyEssayid")
                val response = essayApi.modifyEssay( modifyEssayid, essayData = essayData)
                if (response.isSuccessful){
                    Log.e("수정 성공", "수정 성공!: ${response.code()}", )

                    navController.navigate("HOME/200")
                    navController.popBackStack("OnBoarding", false) //onboarding까지 전부 삭제.

                    initialize()
                }
                else{
                    Log.e("modifyEssayError", "modifyEssayError: ${response.code()}", )
                    Log.e("modifyEssayError", "modifyEssayError: ${Token.accessToken}", )
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

    fun uploadThumbnail(uri: Uri, context: Context) { //서버에 이미지 업로드하고 url을 반환
        viewModelScope.launch {
            try {
                val file = getFileFromUri(uri, context)
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                // 서버로 업로드 요청 보내기
                val response = essayApi.uploadThumbnail( body, exampleItems.detailEssay.id)

                if (response.isSuccessful){
                    imageUrl = response.body()!!.data.imageUrl
                    Log.d(TAG, "uploadImage: $imageUrl")
                }
                else{

                }


                // 서버 응답에서 URL 추출
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "uploadImage: ${e.message}")


            } finally {

            }
        }



    }
}